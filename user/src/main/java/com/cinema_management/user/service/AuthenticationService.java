package com.cinema_management.user.service;

import com.cinema_management.user.dto.AuthenticationDTO;
import com.cinema_management.user.dto.IntrospectDTO;
import com.cinema_management.user.repository.UserRepo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.signer.key}")
    private String SIGNER_KEY;

    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("localhost:8080")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customClaim", "Custom")
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

        return generateToken(request.getUsername());
    }

    public Boolean introspect(IntrospectDTO request){
        var token = request.getToken();

        JWSVerifier verifier = null;
        try {
            verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier);

            return verified && expityTime.after(new Date());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
