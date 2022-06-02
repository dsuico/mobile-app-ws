package com.appsdeveloperblog.app.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.appsdeveloperblog.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.ws.shared.dto.UserDto;
import com.appsdeveloperblog.ws.ui.controller.UserController;
import com.appsdeveloperblog.ws.ui.model.response.UserRest;

class UserControllerTest {

	@InjectMocks
	UserController userController;
	
	@Mock
	UserServiceImpl userService;
	
	UserDto userDto;
	
	final String USER_ID = "asdqwea23zxc21";
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		userDto = new UserDto();
		
		userDto.setFirstName("david");
		userDto.setLastName("suico");
		userDto.setEmail("david@test.com");
		userDto.setEmailVerificationToken(null);
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setUserId(USER_ID);
		userDto.setEncryptedPassword("asdzxc123");
		userDto.setAddresses(getAddressesDto());
	}

	@Test
	void testGetUser() {
		when( userService.getUserByUserId(anyString()) ).thenReturn(userDto);
		
		UserRest userRest = userController.getUser(USER_ID);
		
		assertNotNull(userRest);
		assertEquals(userDto.getUserId(), userRest.getUserId());
		assertEquals(userDto.getFirstName(), userRest.getFirstName());
		assertEquals(userDto.getLastName(), userRest.getLastName());
		assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
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

}
