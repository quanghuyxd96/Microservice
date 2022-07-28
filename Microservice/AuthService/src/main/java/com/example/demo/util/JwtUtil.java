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

import static com.example.demo.util.Constants.JWT_TOKEN_VALIDITY;
import static com.example.demo.util.Constants.SCERET;

@Component
public class JwtUtil {

	private static final long serialVersionUID = -2550185165626007488L;

	private Claims getClaims(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SCERET)
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
		Claims claims = getClaims(token);
		Date expiration = claims.getExpiration();
		return expiration;
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SCERET).parseClaimsJws(token).getBody();
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
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512, SCERET).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, String userName) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userName) && !isTokenExpired(token));
	}

}
