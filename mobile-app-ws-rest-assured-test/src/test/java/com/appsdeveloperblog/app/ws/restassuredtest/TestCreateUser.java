package com.appsdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


class TestCreateUser {
	
	private final String CONTEXT_PATH = "/mobile-app-ws";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	void testCreateUser() {
		
		List<Map<String, Object>> userAddresses = new ArrayList<>();
		
		Map<String, Object> shippingAddress = new HashMap<>();
		shippingAddress.put("city", "Talisay");
		shippingAddress.put("country", "Philippines");
		shippingAddress.put("street", "San Isasidro Street");
		shippingAddress.put("postalCode", "6000");
		shippingAddress.put("type", "shipping");

		Map<String, Object> billingAddress = new HashMap<>();
		billingAddress.put("city", "Talisay");
		billingAddress.put("country", "Philippines");
		billingAddress.put("street", "San Isasidro Street");
		billingAddress.put("postalCode", "6000");
		billingAddress.put("type", "billing");
		
		userAddresses.add(shippingAddress);
		userAddresses.add(billingAddress);
		
		Map<String, Object> user = new HashMap<>();
		user.put("firstName", "david");
		user.put("lastName", "suico");
		user.put("email", "david@test.com");
		user.put("password", "123");
		user.put("addresses", userAddresses);
		
		Response response = given()
				.log().all()
		.contentType("application/json; charset=UTF-8")
		.accept(ContentType.JSON)
		.body(user)
		.when()
		.post(CONTEXT_PATH + "/users")
		.then()
		.statusCode(200)
		.contentType("application/json")
		.log().all()
		.extract()
		.response();
		
		String userId = response.jsonPath().getString("userId");
		
		assertNotNull(userId);
		assertTrue(userId.length() == 30);
		
		String bodyString = response.body().asString();
		try {
			JSONObject responseJson = new JSONObject(bodyString);
			JSONArray addresses = responseJson.getJSONArray("addresses");
			assertNotNull(addresses);
			assertTrue(addresses.length() == 2);
			
			String addressId = addresses.getJSONObject(0).getString("addressId");
			assertNotNull(addressId);
			assertTrue(addressId.length() == 30);
		} catch (JSONException e) {
			fail(e.getMessage());
		}
	}

}
