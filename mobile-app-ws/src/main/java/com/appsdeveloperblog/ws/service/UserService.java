package com.appsdeveloperblog.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	public UserDto createUser(UserDto user);
	
	public UserDto updateUser(String userId, UserDto user);
	
	public UserDto getUser(String email);
	
	public UserDto getUserByUserId(String id);
	
	public void deleteUser(String userId);
	
	public List<UserDto> getUsers(int page, int limit);
	
	public boolean verifyEmailToken(String token);

	public boolean requestPasswordReset(String email);
}
