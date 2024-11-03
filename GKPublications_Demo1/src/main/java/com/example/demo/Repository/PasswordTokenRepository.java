package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.PasswordResetToken;
import java.util.Optional;
import com.example.demo.Entity.User;



@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
			
				
			Optional<PasswordResetToken>findByToken(String token);
			
			void deleteByUser(User user); // to delete/invalidate 
	
}
