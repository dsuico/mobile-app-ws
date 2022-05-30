package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.appsdeveloperblog.ws.io.entity.UserEntity;
import com.appsdeveloperblog.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.ws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetUsers() {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1);
		userEntity.setFirstName("david");
		userEntity.setUserId("21wew3s3");
		userEntity.setEncryptedPassword("43qq#q43");
		
		when( userRepository.findByEmail( anyString() ) ).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("david", userDto.getFirstName());
	}
	
	@Test
	final void testGetUser_UsernameNotFoundException() {
		when( userRepository.findByEmail( anyString() ) ).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class,
				
			() -> {
				userService.getUser("test@test.com");
			}
		);
	}

}
