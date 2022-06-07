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
	
	static boolean recordsCreated = false;

	@BeforeEach
	void setUp() throws Exception {
		if(!recordsCreated) createRecords();
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

	@Test
	final void testFindUserByFirstName() {
		String firstName = "david";
		List<UserEntity> users = userRepository.findUsersByFirstName(firstName);
		
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		UserEntity user = users.get(0);
		assertTrue(user.getFirstName().equals(firstName));
	}
	
	@Test
	final void testFindUserByLastName() {
		String lastName = "suico";
		List<UserEntity> users = userRepository.findUsersByLastName(lastName);
		
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		UserEntity user = users.get(0);
		assertTrue(user.getLastName().equals(lastName));
	}
	
	@Test
	final void testFindUsersByKeyword() {
		String keyword = "avi";
		List<UserEntity> users = userRepository.findUsersByKeyword(keyword);
		
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		UserEntity user = users.get(0);
		assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
	}
	
	@Test
	final void testFindUserFirstNameAndLastNameByKeyword() {
		String keyword = "avi";
		List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
		
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		Object[] user = users.get(0);
		
		assertTrue(user.length == 2);
		String userFirstName = String.valueOf(user[0]);
		String userLastName = String.valueOf(user[1]);
		
		assertNotNull(userFirstName);
		assertNotNull(userLastName);
		
		System.out.println("First name = " + userFirstName);
		System.out.println("Last name = " + userLastName);
	}
	
	@Test
	final void testUpdateUserEmailVerificationStatus() {
		boolean newEmailVerificationStatus = true;
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "123123");
		
		UserEntity storedUser = userRepository.findByUserId("123123");
		
		assertTrue(storedUser.getEmailVerificationStatus() == newEmailVerificationStatus);
	}
	
	@Test
	final void testFindUserEntityByUserId() {
		String userId = "123123";
		UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
		
		assertNotNull(userEntity);
		
		assertTrue(userEntity.getUserId().equals(userId));
	}
	
	@Test
	final void testGetUserEntityFullNameById() {
		String userId = "123123";
		List<Object[]> records = userRepository.getUserEntityFullNameById(userId);
		
		assertNotNull(records);
		assertTrue(records.size() == 1);
		
		Object[] userDetail = records.get(0);
		
		String firstName = String.valueOf(userDetail[0]);
		String lastName = String.valueOf(userDetail[1]);
		
		assertNotNull(firstName);
		assertNotNull(lastName);
	}
	
	@Test
	final void testUpdatUserEntityEmailVerificationStatus() {
		boolean newEmailVerificationStatus = true;
		userRepository.updatUserEntityEmailVerificationStatus(newEmailVerificationStatus, "123123");
		
		UserEntity storedUser = userRepository.findByUserId("123123");
		
		assertTrue(storedUser.getEmailVerificationStatus() == newEmailVerificationStatus);
	}
	
	private void createRecords() {
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
		
		recordsCreated = true;
	}
}
