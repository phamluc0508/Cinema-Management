package com.cinema_management.user.service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.cinema_management.user.dto.AuthenticationDTO;
import com.cinema_management.user.dto.IntrospectDTO;
import com.cinema_management.user.model.InvalidatedToken;
import com.cinema_management.user.model.User;
import com.cinema_management.user.repository.InvalidatedTokenRepo;
import com.cinema_management.user.repository.UserRepo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.management.InvalidApplicationException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final InvalidatedTokenRepo invalidatedTokenRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.signer.key}")
    private String SIGNER_KEY;

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }

            });
        }
        return stringJoiner.toString();
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("localhost:8080")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return jwsObject.serialize();
    }
    public String authenticate(AuthenticationDTO request){
        var user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("not-found-with-username"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("wrong-password");
        }

        return generateToken(user);
    }

    public void logout(String token) throws ParseException, JOSEException {
        var signedToken = verifyToken(token);

        String jid = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jid);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepo.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if(!verified && !expityTime.after(new Date())){
            throw new RuntimeException("User-is-not-authenticated");
        }

        if(invalidatedTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new RuntimeException("User-is-not-authenticated");
        };

        return signedJWT;
    }

    public Boolean introspect(String token) throws ParseException, JOSEException {

        try{
            verifyToken(token);
        } catch (Exception ex){
            return false;
        }

        return true;
    }

    public String refreshToken(String token) throws ParseException, JOSEException {
        var signJwt = verifyToken(token);

        logout(token);

        var username = signJwt.getJWTClaimsSet().getSubject();
        var user = userRepo.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("not-found-with-username"));

        return generateToken(user);
    }

}
