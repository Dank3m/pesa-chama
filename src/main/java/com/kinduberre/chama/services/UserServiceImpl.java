package com.kinduberre.chama.services;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kinduberre.chama.exception.RecordNotFoundException;
import com.kinduberre.chama.models.auth.Role;
import com.kinduberre.chama.models.auth.User;
import com.kinduberre.chama.repositories.RoleRepo;
import com.kinduberre.chama.repositories.UserRepo;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	RoleRepo roleRepository;
	
	@Autowired
	UserRepo userRepository;

	@Override
	public void saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setStatus("VERIFIED");
		user.setEmail(user.getEmail().toUpperCase());
		Role userRole = roleRepository.findByRole("SITE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public boolean isUserAlreadyPresent(User user) {
		boolean isUserAlreadyExists = false;
		User existingUser = userRepository.findByEmail(user.getEmail());
		// If user is found in database, then then user already exists.
		if(existingUser != null){
			isUserAlreadyExists = true; 
		}
		return isUserAlreadyExists;
	}

	@Override
	public void updateResetPasswordToken(String token, String email) throws RecordNotFoundException {
		User user = userRepository.findByEmail(email.toUpperCase());
		if (user != null) {
			user.setResetPassword(token);
			userRepository.save(user);
        } else {
            throw new RecordNotFoundException("Could not find any user with the email: " + email);
        }
		
	}

	@Override
	public User getByResetPasswordToken(String token) {
		return userRepository.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(User user, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPassword(null);
        userRepository.save(user);
	}
}
