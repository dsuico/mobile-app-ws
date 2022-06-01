package com.appsdeveloperblog.app.ws.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appsdeveloperblog.ws.MobileAppWsApplication;
import com.appsdeveloperblog.ws.shared.Utils;

@ContextConfiguration(classes = MobileAppWsApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userId1 = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
		
		assertNotNull(userId1);
		assertNotNull(userId2);
		assertTrue(userId1.length() == 30);
		assertTrue(!userId1.equalsIgnoreCase(userId2));
	}

	@Test
	void testHasTokenNotExpired() {
		String token = utils.generateEmailVerificationToken("asdwq");
		
		assertNotNull(token);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		
		assertFalse(hasTokenExpired);
	}
	
	@Test
	final void testHasTokenExpired()
	{
		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2R3cSIsImV4cCI6MTQyOTY2NX0.n8ottQiUor6RHN3jhYZQHssu9VSSa0twKdaSO1tu8cpqtzvlQtguqEIloDrXxX-yJkHAjkUqqEU4WtQMPhNVzg";
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		
		assertTrue(hasTokenExpired);
	}

}
