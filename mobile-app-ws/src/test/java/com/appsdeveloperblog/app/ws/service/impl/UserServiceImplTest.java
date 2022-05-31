package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.ws.io.entity.UserEntity;
import com.appsdeveloperblog.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.ws.shared.AmazonSES;
import com.appsdeveloperblog.ws.shared.Utils;
import com.appsdeveloperblog.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.ws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Utils utils;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	AmazonSES amazonSES;
	
	String userId = "ghgh123";
	String encryptedPassword = "43qq#q43";
	
	UserEntity userEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("david");
		userEntity.setLastName("suico");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("asdewqwe");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	void testGetUsers() {
		
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
	
	@Test
	final void testCreateUser_CreateUserServiceException()
	{
		when( userRepository.findByEmail( anyString() ) ).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setEmail("test@test.com");
		userDto.setFirstName("david");
		userDto.setLastName("suico");
		userDto.setPassword("12345");
		userDto.setAddresses(getAddressesDto());
		
		assertThrows(UserServiceException.class,
				
			() -> {
				userService.createUser(userDto);
			}
		);
	}
	
	@Test
	final void testCreateUser() {
		when( userRepository.findByEmail( anyString() ) ).thenReturn(null);
		when( utils.generateAddressId( anyInt() ) ).thenReturn("gfgf123");
		when( utils.generateUserId( anyInt() ) ).thenReturn(userId);
		when( bCryptPasswordEncoder.encode( anyString() ) ).thenReturn(encryptedPassword);
		when( userRepository.save( any(UserEntity.class) ) ).thenReturn(userEntity);
		Mockito.doNothing().when(amazonSES).verifyEmail( any(UserDto.class) );
		
		UserDto userDto = new UserDto();
		userDto.setEmail("test@test.com");
		userDto.setFirstName("david");
		userDto.setLastName("suico");
		userDto.setPassword("12345");
		userDto.setAddresses(getAddressesDto());
		
		UserDto storedUser = userService.createUser(userDto);
		
		assertNotNull(storedUser);
		assertEquals(userEntity.getFirstName(), storedUser.getFirstName());
		assertEquals(userEntity.getLastName(), storedUser.getLastName());
		assertNotNull(storedUser.getUserId());
		assertEquals(storedUser.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils, times(storedUser.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("12345");
		verify(userRepository, times(1)).save( any(UserEntity.class) );
	}

	private List<AddressDto> getAddressesDto()
	{
		AddressDto shippingAddressDto = new AddressDto();
		shippingAddressDto.setType("shipping");
		shippingAddressDto.setCity("Talisay");
		shippingAddressDto.setCountry("Philippines");
		shippingAddressDto.setPostalCode("ABASD");
		shippingAddressDto.setStreet("123 Street");
		
		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("Talisay");
		billingAddressDto.setCountry("Philippines");
		billingAddressDto.setPostalCode("ABASD");
		billingAddressDto.setStreet("123 Street");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(shippingAddressDto);
		addresses.add(billingAddressDto);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntity()
	{
		List<AddressDto> addresses = getAddressesDto();
		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		
		return new ModelMapper().map(addresses, listType);
	}
}
