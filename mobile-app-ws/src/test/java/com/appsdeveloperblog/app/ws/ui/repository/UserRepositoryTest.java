package com.appsdeveloperblog.app.ws.ui.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appsdeveloperblog.ws.MobileAppWsApplication;
import com.appsdeveloperblog.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.ws.io.entity.UserEntity;
import com.appsdeveloperblog.ws.io.repositories.UserRepository;

@ContextConfiguration(classes = MobileAppWsApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		UserEntity userEntity1 = new UserEntity();
		userEntity1.setUserId("123123");
		userEntity1.setFirstName("david");
		userEntity1.setLastName("suico");
		userEntity1.setEmail("david1@test.com");
		userEntity1.setEncryptedPassword("zxc123qwe");
		userEntity1.setEmailVerificationToken("azxcs1213");
		userEntity1.setEmailVerificationStatus(Boolean.TRUE);
		
		AddressEntity addressEntity1 = new AddressEntity();
		addressEntity1.setType("shipping");
		addressEntity1.setAddressId("zxc123qweWW");
		addressEntity1.setCity("Talisay");
		addressEntity1.setCountry("Philippines");
		addressEntity1.setStreet("street 123 name");
		addressEntity1.setPostalCode("SSV122");
		
		List<AddressEntity> addresses1 = new ArrayList<>();
		addresses1.add(addressEntity1);
		
		userEntity1.setAddresses(addresses1);
		
		userRepository.save(userEntity1);
		
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setUserId("222222");
		userEntity2.setFirstName("david");
		userEntity2.setLastName("suico");
		userEntity2.setEmail("david2@test.com");
		userEntity2.setEncryptedPassword("zxc123qwe");
		userEntity2.setEmailVerificationToken("azxcs1213");
		userEntity2.setEmailVerificationStatus(Boolean.TRUE);
		
		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("zxc123qwe");
		addressEntity2.setCity("Talisay");
		addressEntity2.setCountry("Philippines");
		addressEntity2.setStreet("street 123 name");
		addressEntity2.setPostalCode("SSV122");
		
		List<AddressEntity> addresses2 = new ArrayList<>();
		addresses2.add(addressEntity2);
		
		userEntity2.setAddresses(addresses2);
		
		userRepository.save(userEntity2);
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(1,  1);
		Page<UserEntity> page = userRepository.findAllUsersWithVerifiedEmailAddress(pageableRequest);
		
		assertNotNull(page);
		
		List<UserEntity> userEntities = page.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
	}

}
