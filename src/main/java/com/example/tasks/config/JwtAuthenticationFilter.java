package com.example.tasks.config;

import com.example.tasks.repository.PermissionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.AesKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Value("${jwt.secret}")
    private String jwtSecret;
    private final PermissionRepository permissionRepository;

    public JwtAuthenticationFilter(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        System.out.println("--- REQUEST CĂTRE: " + request.getRequestURI() + " ---");
        System.out.println("HEADER PRIMIT: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                        .setRequireExpirationTime()
                        .setVerificationKey(new AesKey(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                        .build();

                var claims = jwtConsumer.processToClaims(token);

                String email = claims.getSubject();
                System.out.println("TOKEN VALIDAT CU SUCCES! Email extras: " + email);


                Long roleId = claims.getClaimValue("roleId", Long.class);

                List<GrantedAuthority> authorities = new ArrayList<>();

                if (roleId != null) {



                    List<String> permissions = permissionRepository.findPermissionByRoleId(roleId);


                    authorities = permissions.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());



                }
                System.out.println("PERMISIUNI (AUTHORITIES) PUSE ÎN CONTEXT: " + authorities);

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        )
                );
            } catch (Exception e) {
                System.out.println("!!! EROARE LA VALIDAREA TOKEN-ULUI !!!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            System.out.println("Header-ul nu are prefixul 'Bearer '");
        }

        filterChain.doFilter(request, response);
    }
}