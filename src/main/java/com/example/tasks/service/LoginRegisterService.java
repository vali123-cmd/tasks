package com.example.tasks.service;

import com.example.tasks.domain.User;
import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.repository.UserRepository;
import com.example.tasks.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.security.Credential;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;



@Slf4j
@Service
public class LoginRegisterService {
    private final UserRepository userRepository;
    @Value("${jwt.secret}") String jwtSecret;
    @Value("${jwt.expiration.ms}") String jwtExpiration;

    public LoginRegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public ResponseEntity login(CredentialsDTO credentialsDTO) throws JoseException {

        if (credentialsDTO == null || credentialsDTO.getEmail() == null || credentialsDTO.getPassword() == null) {
            throw new IllegalArgumentException("Email and password credentials cannot be null");
        }


        credentialsDTO.setEmail(new String(Base64.getDecoder().decode(credentialsDTO.getEmail())));
        credentialsDTO.setPassword(new String(Base64.getDecoder().decode(credentialsDTO.getPassword())));

        String hashPassword = Credential.MD5.digest(credentialsDTO.getPassword()).replaceFirst("MD5:","");
        log.info("Hash password: {}", hashPassword);

        User user = userRepository.findByEmail(credentialsDTO.getEmail()).orElse(null);
        log.info("User found: {}", user.getPassword());
        if (user != null && user.getPassword().equals(hashPassword)) {

            return ResponseEntity.ok(createJWTToken(credentialsDTO.getEmail()));
        } else {
            return ResponseEntity.status(403).build();
        }
    }
    private String createJWTToken(String email) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture((float) Long.parseLong(jwtExpiration)/(1000*60));
        claims.setSubject(email);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(new AesKey(jwtSecret.getBytes(StandardCharsets.UTF_8)));
        return jws.getCompactSerialization();
    }

    public ResponseEntity register(UserDTO userDTO) throws JoseException{
        userDTO.setEmail(new String(Base64.getDecoder().decode(userDTO.getEmail())));
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
        if(user != null)
            return ResponseEntity.status(403).build();

        userDTO.setUsername(new String(Base64.getDecoder().decode(userDTO.getUsername())));
        userDTO.setPassword(new String(Base64.getDecoder().decode(userDTO.getPassword())));
        userDTO.setBirthDate(userDTO.getBirthDate());
        String hashPassword = Credential.MD5.digest(userDTO.getPassword()).replaceFirst("MD5:","");
        userDTO.setPassword(hashPassword);

        UserMapper userMapper = new UserMapper();
        userRepository.save(userMapper.toEntity(userDTO));
        log.info("User created: {}", userDTO);


        return ResponseEntity.ok(createJWTToken(userDTO.getEmail()));
    }




}
