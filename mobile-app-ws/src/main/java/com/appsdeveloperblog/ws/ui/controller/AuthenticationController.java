package com.appsdeveloperblog.ws.ui.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.ws.ui.model.request.LoginRequestModel;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class AuthenticationController {

	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Response Headers",
				headers = {
					@Header(name = "Authorization", description="Bearer < JWT value here >", schema=@Schema(type="String")),
					@Header(name = "UserID", description="< Public User Id value here >", schema=@Schema(type="String"))
				}
				
		)
	})
	@PostMapping("/users/login")
	public void login(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException("this method should not be called; this method is implemented by Spring Security");
	}
}
