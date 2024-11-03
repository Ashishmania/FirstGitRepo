package com.example.demo.Service;

import java.time.LocalDateTime;

import java.util.Optional;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DTO.AuthRequest;
import com.example.demo.Entity.PasswordResetToken;
import com.example.demo.Entity.User;
import com.example.demo.Repository.PasswordTokenRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.CustomUserDetails;
import com.example.demo.Security.CustomUserDetailsService;
import com.example.demo.Security.JwtUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	//
	@Autowired
	private AuthenticationManager authenticationManager;
	// coming from SecurityConfig class - Bean is already defined there with name
	// Authentication Manager

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordTokenRepository passwordTokenRepository;

	private final CustomUserDetailsService customUserDetailsService;

	public UserService(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	public User registerUser(User userobj) {

		String email = userobj.getEmail();
		String username = email.substring(0, email.indexOf("@"));
		String password = UUID.randomUUID().toString();

		User user = new User();

		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		// with this step we can also encode the password provided we have implemented
		user.setEnabled(true);
		user.setRole(userobj.getRole());
		// setting the user role
		userRepository.save(user);
		// saving the user registration details to the repository and hence afterwards
		// it will be persisted to the database
		// Next step would be to send these details to the user's email for that we need
		// emailservice object.
		emailService.sendRegistrationEmail(email, username, password);
		return user;

	}

	public String login(AuthRequest authRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			// UsernamePasswordAuthenticationToken: authentication token that contains
			// credentials (typically username and password) and authorities
			// (roles/permissions).
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Invalid credentials provided");
		}
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
		String role = ((CustomUserDetails) userDetails).getRole().name();
		//extracting the role of user from CustomUserDetails object
		return jwtUtil.generateToken(userDetails.getUsername(), role);
		//generating the token 
	}

	public void createPasswordTokenForUser(User user) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(1));
		passwordTokenRepository.save(passwordResetToken);

		// sending email with reset Link
		String resetLink = "http://localhost:8080/api/reset-password?token=" + token;
		emailService.sendEmail(user.getEmail(), "password Reset Request",
				"To reset your Password, Click the link below:\n" + resetLink);
	}

	public Optional<PasswordResetToken> findByToken(String token) {
		return passwordTokenRepository.findByToken(token);
	}

	@Transactional
	public void resetPassword(User user, String newPassword) {
		user.setPassword(newPassword);
		// user.setPassword(PasswordEncoder.encode(newPassword); - in case encoding is
		// being done
		userRepository.save(user);
		// to invalidate the token after successful password reset
		passwordTokenRepository.deleteByUser(user);
	}
}
