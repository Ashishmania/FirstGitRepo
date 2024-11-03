package com.example.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Does Not Exist"));
		return new CustomUserDetails(user); //subtype of Userdetails 
	}

}


// CustomUserDetailsService is responsible for fetching 
//user data from the database based on the username.

//The UserDetailsService interface is implemented in the CustomUserDetailsService class 
//to allow Spring Security to retrieve user information 
//from the database for authentication purposes.

