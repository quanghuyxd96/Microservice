package com.example.demo.util;

import com.example.demo.exception.JwtTokenMalformedException;
import com.example.demo.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
//
//	@Value("${jwt.secret}")
//	private String jwtSecret;
//
//	@Value("${jwt.token.validity}")
//	private long tokenValidity;
//
//	public Claims getClaims(final String token) {
//		try {
//			Claims body = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//			return body;
//		} catch (Exception e) {
//			System.out.println(e.getMessage() + " => " + e);
//		}
//		return null;
//	}
//
//	public String generateToken(String id) {
//		Claims claims = Jwts.claims().setSubject(id);
//		long nowMillis = System.currentTimeMillis();
//		long expMillis = nowMillis + tokenValidity;
//		Date exp = new Date(expMillis);
//		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
//				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
//	}
//
//	public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
//		try {
//			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//		} catch (SignatureException ex) {
//			throw new JwtTokenMalformedException("Invalid JWT signature");
//		} catch (MalformedJwtException ex) {
//			throw new JwtTokenMalformedException("Invalid JWT token");
//		} catch (ExpiredJwtException ex) {
//			throw new JwtTokenMalformedException("Expired JWT token");
//		} catch (UnsupportedJwtException ex) {
//			throw new JwtTokenMalformedException("Unsupported JWT token");
//		} catch (IllegalArgumentException ex) {
//			throw new JwtTokenMissingException("JWT claims string is empty.");
//		}
//	}

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("jsghdsajdsahdskaljkdjskldjsakdsaudjksanbmksadnjasdiiudsdsydtjlioidsjabdsnqjumssdjhdsjadjahdjhdsdjsadhjdshjd" +
			"dshdajdhjsadhjhdjhsadjhdksadusadhadjdjadhjadhjdhjsakdhjkasdysaudausdhdasndadsndnijiodasjidsajdsakldjskasjdj" +
			"djksahsdjahdjsahduasdydjndmnmnjdshjdhuydsnasbdnsbndsabdhiuhdusaidpqopsakjdsakmdsaddskakjdksadjasdjkldjdasldj" +
			"dsdsjadsaioqiopidslakldsdjiadjdskldskmsdkalduiuqnmnmsasda")
	private String secret;

	private Claims getClaims(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
		return claims;
	}

	public String getUsernameFromToken(String token) {
//		return getClaimFromToken(token, Claims::getSubject;
		Claims claims = getClaims(token);
		return claims.getSubject();

	}

	public Date getIssuedAtDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getIssuedAt);
		Claims claims = getClaims(token);
		Date issuedAt = claims.getIssuedAt();
		System.out.println("2: " + issuedAt);
		return issuedAt;
	}

	public Date getExpirationDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getExpiration);
		Claims claims = getClaims(token);
		Date expiration = claims.getExpiration();
		System.out.println("3: " + expiration);
		return expiration;
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userName);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, String userName) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userName) && !isTokenExpired(token));
	}

}
