package com.example.demo.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.*;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	private String secret = "ahwqdwqofjwpofjepjfpoewwjgoewjwgpooewjgpiewjpgojewpgwegiewojgpoewjgpoewjgpopew"; // Use environment variable in production
	
	public String generateToken(String username, String role) {
		Map<String, Object> claims = new HashMap<>(); //payload data while generating the token 
		claims.put("role", role); // adding the role as a claim to the payload
		return Jwts.builder()
				.setClaims(claims) //here the payload data claims is passed 
				.setSubject(username) //
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
				.signWith(SignatureAlgorithm.HS256, secret) //signing the token with a secret key ensures token can't be tampered with
				.compact(); //finalize and generates the JWT as a String 
	}
	
	// called during the login process when a user successfully authenticates 
	// generates a JWT token with provided username and role as part of payload claims
	//also token is then signed with a secret key using HS256 algorithm 

	public Claims extractClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secret) // Secret key used for validation 
				.parseClaimsJws(token) //parses the token
				.getBody(); // returns the token if valid 
	}
	
	//called during token validation process to extract the payload from the provided JWT token
	// it parses JWT token and return the payload(claims) which contais username, role and other info such as iat, exp

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}
	
	// called during token validation to retrieve the username from JWT token (used in req filter)
	//extracts the sub field from JWt token - username of the user
	
	
	public String extractRole(String token) {
		return extractClaims(token).get("role", String.class);
	}
	
	//called during token validation to retrieve the user's role
	// to check the role based authorization

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	//called when validating the tokejn - in JwtRequestFilter to verify the provided token for given user

	private boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}
	
	//calle inside the isTokenValid method to check if the JWT token has expired.
}


//================================================================================
/*1.) What is the role of Secret key 
 * 
 * Ans.) * Signing the token 
 * 		* Validating the token 
 * 
 * ------------ Signing the token ----------------------------------------------
 * 
 * The secret key is used to sign the JWT when it is created. 
 * This ensures that the token cannot be easily tampered with.
 * 
 * When the JWT is generated, the header and payload are combined 
 * and signed using the specified signing algorithm (e.g., HS256) and the secret key.
* This generates a signature that is appended to the token. 
* The token consists of three parts: Header, Payload, and Signature.
 * 
 * ----------------- Validating the token ---------------------------------------
 * 
 * When a server receives a JWT, it can verify the signature by using the same secret key 
 * that was used to sign the token. If the signature matches, it confirms that the token 
 * is valid and has not been altered.
 * 
 * 
 * 
 * 
 * 
 * 
 * /
 */

