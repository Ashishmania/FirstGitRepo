package com.example.demo.RestController;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTO.AuthRequest;
import com.example.demo.Entity.PasswordResetToken;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		//String email = user.getEmail();
		userService.registerUser(user);
		return ResponseEntity.ok("Registration Successful. Check your email for Credentials");
	}
    
	@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = userService.login(authRequest);
        return ResponseEntity.ok(token);
        //Returns an HTTP response with a status of 200 OK and 
        //includes the generated JWT token in the response body.
    }
	
	 @GetMapping("/getAuthors")
	    @PreAuthorize("hasRole('Admin')") // Only Admin can access this endpoint
	    public ResponseEntity<String> getAuthors() {
	        return ResponseEntity.ok("List of All the Authors will be Displayed to Admin");
	    }
	 
	 @GetMapping("/showcart")
	    @PreAuthorize("hasRole('User')") // Only Admin can access this endpoint
	    public ResponseEntity<String>  viewCart() {
	        return ResponseEntity.ok("List of books available in the cart of User");
	    }
	 

	 //=======================================================================================================
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			userService.createPasswordTokenForUser(user.get());
			return ResponseEntity.ok("Password Reset Link has been sent to your email");
		} else {
			return ResponseEntity.status(404).body("User not found");
		}

	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newpassword) {
		Optional<PasswordResetToken> passwordResetToken = userService.findByToken(token);
		if (passwordResetToken.isPresent()) {// && !passwordResetToken.get().isExpired()) - this logic needs to be
												// implemented
			userService.resetPassword(passwordResetToken.get().getUser(), newpassword);
			return ResponseEntity.ok("Password Reset Successful");
		} else {
			return ResponseEntity.status(400).body("Invalid or Expired token");
		}
	}
	
		

}