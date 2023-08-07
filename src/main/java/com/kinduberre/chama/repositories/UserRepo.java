package com.kinduberre.chama.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kinduberre.chama.models.auth.User;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
//	@Query("FROM User u WHERE u.userId = ?1")
//	User findByUserId(String userId);
	
	@Query("FROM User u WHERE u.email = ?1")
	User findByEmail(String email);
	
	@Query("FROM User u WHERE u.resetPassword =?1")
	User findByResetPasswordToken(String token);
}
