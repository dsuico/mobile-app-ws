package com.appsdeveloperblog.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	public UserDto createUser(UserDto user);
}
