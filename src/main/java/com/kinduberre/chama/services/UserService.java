package com.kinduberre.chama.services;

import com.kinduberre.chama.exception.RecordNotFoundException;
import com.kinduberre.chama.models.auth.User;

public interface UserService {
	
	public void saveUser(User user);
	
	public boolean isUserAlreadyPresent(User user);
	
	public void updateResetPasswordToken(String token, String email) throws RecordNotFoundException;
	
	public User getByResetPasswordToken(String token);
	
	public void updatePassword(User user, String newPassword);

}
