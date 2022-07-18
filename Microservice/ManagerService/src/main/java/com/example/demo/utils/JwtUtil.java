package com.example.demo.utils;

import com.example.demo.entity.Manager;
import groovy.util.logging.Slf4j;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    //    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
//    private static final String USER = "user";
//    private static final String SECRET = "huy";
//
//    public String generateToken(Manager manager) {
//        String token = null;
//        try {
//            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
//            builder.claim(USER, user);
//            builder.expirationTime(generateExpirationDate());
//            JWTClaimsSet claimsSet = builder.build();
//            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
//            JWSSigner signer = new MACSigner(SECRET.getBytes());
//            signedJWT.sign(signer);
//            token = signedJWT.serialize();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//        return token;
//    }
//
//    private Date generateExpirationDate() {
//        return new Date(System.currentTimeMillis() + 864000000);
//    }

    private Logger log = LoggerFactory.getLogger(JwtUtil.class);
    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private final String JWT_SECRET = "huyaaaaaaaaaaaa";

    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 604800000L;

    // Tạo ra jwt từ thông tin user
    public String generateToken(Manager manager) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        // Tạo chuỗi json web token từ userName của user.
        return Jwts.builder()
                .setSubject(manager.getUserName())
                .setIssuedAt(now)
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
    }

    // Lấy thông tin user từ jwt
    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
