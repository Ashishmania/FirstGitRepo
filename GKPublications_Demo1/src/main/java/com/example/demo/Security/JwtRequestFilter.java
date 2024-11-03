package com.example.demo.Security;

import java.io.IOException;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Security.CustomUserDetailsService;
import com.example.demo.Security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final CustomUserDetailsService customuserDetailsService;
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Get Authorization Header From Request - Client will send a request with token
		// (as an authorization bearer token)
		final String authHeaderVal = request.getHeader("Authorization"); // Key - Authorization
		// when this request is going to server, filter is going to execute and we can
		// access header value with the help of key
		// extract the username from token
		String username = null;
		String jwt = null;

		// Authorization = Bearer <token>
		if (authHeaderVal != null && authHeaderVal.startsWith("Bearer ")) {
			jwt = authHeaderVal.substring(7); // actual jwt token
			username = jwtUtil.extractUsername(jwt);
		}

		// Load userData based on userName using userDetailsService
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
			// if security-authencation is not happening then we are loading the  user data with username
			
			UserDetails userDetails = this.customuserDetailsService.loadUserByUsername(username);
			
			// and after then we Validate token with credentials/userdetails
			if (jwtUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
	}

}

//==================================================================================================
/*1.) Security Context holder - 
 * a central component of Spring Security that holds 
 * security context for the current thread of execution.
 * 
 * SecurityContextHolder is thread-local, meaning each request has its own context. 
 * This ensures that authentication details do not leak between requests.
 * 
 * It provides a straightforward way to access authentication information 
 * from anywhere in the application (controllers, services, etc.) using
 * SecurityContextHolder.getContext().getAuthentication().
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * /
 */

