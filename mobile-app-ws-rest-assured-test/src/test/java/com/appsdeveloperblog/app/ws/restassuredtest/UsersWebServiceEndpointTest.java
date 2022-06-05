package com.appsdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.MethodName.class)
class UsersWebServiceEndpointTest {

	private final String CONTEXT_PATH = "/mobile-app-ws";
	
	private final String EMAIL = "david@test.com";
	private final String PASSWORD = "123";
	
	private static String authorizationHeader;
	private static String userId;
	private static List<Map<String, String>> addresses;
	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}
	
	/*
	 * test the user login api
	 * */
	@Test
	void a() {
		Map<String, String> loginDetails = new HashMap<>();
		
		loginDetails.put("email", EMAIL);
		loginDetails.put("password", PASSWORD);
		
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.body(loginDetails)
							.when()
								.post(CONTEXT_PATH + "/users/login")
							.then()
								.statusCode(200)
								.extract().response();
		
		authorizationHeader = response.header("Authorization");
		userId = response.header("UserID");
		
		assertNotNull(authorizationHeader);
		assertNotNull(userId);
	}

	/*
	 * tests the get user details
	 * */
	@Test
	void b() {
		Response response = given()
								.pathParam("publicUserId", userId)
								.header("Authorization", authorizationHeader)
								.accept(ContentType.JSON)
							.when()
								.get(CONTEXT_PATH + "/users/{publicUserId}")
							.then()
								.statusCode(200)
								.contentType(ContentType.JSON)
								.extract()
								.response();
		
		String userPublicId = response.jsonPath().getString("userId");
		String userEmail = response.jsonPath().getString("email");
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		addresses = response.jsonPath().getList("addresses");
		String addressId = addresses.get(0).get("addressId");
		
		assertNotNull(userPublicId);
		assertNotNull(userEmail);
		assertNotNull(firstName);
		assertNotNull(lastName);
		assertEquals(EMAIL, userEmail);
		
		assertTrue(addresses.size() == 2);
		assertTrue(addressId.length() == 30);
		
	}
	
	/*
	 * test the update user details
	 * */
	@Test
	void c() {
		Map<String, Object> user  = new HashMap<>();
		user.put("firstName", "david alexis");
		user.put("lastName", "suico dev");
		
		Response response = given()
								.contentType(ContentType.JSON)
								.accept(ContentType.JSON)
								.header("Authorization", authorizationHeader)
								.pathParam("publicUserId", userId)
								.body(user)
							.when()
								.put(CONTEXT_PATH + "/users/{publicUserId}")
							.then()
								.statusCode(200)
								.contentType(ContentType.JSON)
								.extract()
								.response();
		
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		
		List<Map<String, String>> storedAddresses = response.jsonPath().getList("addresses");
		
		assertEquals("david alexis", firstName);
		assertEquals("suico dev", lastName);
		assertNotNull(addresses);
		assertTrue(addresses.size() == storedAddresses.size());
		assertEquals(addresses.get(0).get("street"), storedAddresses.get(0).get("street"));
	}
	
	/*
	 * Test the delete user details
	 * */
	@Test
	void d() {
		Response response = given()
								.header("Authorization", authorizationHeader)
								.accept(ContentType.JSON)
								.pathParam("publicUserId", userId)
							.when()
								.delete(CONTEXT_PATH + "/users/{publicUserId}")
							.then()
								.statusCode(200)
								.contentType(ContentType.JSON)
								.extract()
								.response();
		
		String operationResult = response.jsonPath().getString("operationResult");
		
		assertEquals("SUCCESS", operationResult);
	}
}
