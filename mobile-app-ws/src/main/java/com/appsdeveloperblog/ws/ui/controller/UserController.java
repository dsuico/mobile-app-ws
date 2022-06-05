package com.appsdeveloperblog.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.ws.service.AddressService;
import com.appsdeveloperblog.ws.service.UserService;
import com.appsdeveloperblog.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.ws.shared.dto.UserDto;
import com.appsdeveloperblog.ws.ui.model.request.PasswordResetModel;
import com.appsdeveloperblog.ws.ui.model.request.PasswordResetRequestModel;
import com.appsdeveloperblog.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.ws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.ws.ui.model.response.RequestOperationName;
import com.appsdeveloperblog.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	@GetMapping(
			path="/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest getUser(@PathVariable String id)
	{

		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto,  UserRest.class);
		
		return returnValue;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception
	{
		
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(path="/{id}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	{
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		UserDto userDto = new UserDto();
		userDto = new ModelMapper().map(userDetails,  UserDto.class);
		
		UserDto updatedUser = userService.updateUser(id, userDto);
		returnValue = new ModelMapper().map(updatedUser, UserRest.class);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public OperationStatusModel deleteUser(@PathVariable String id)
	{
		userService.deleteUser(id);
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="20") int limit
			)
	{
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		
		for(UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	@GetMapping(path="/{userId}/addresses",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String userId)
	{
		List<AddressesRest> addressesList = new ArrayList<>();
		List<AddressDto> addressesDto = addressService.getAddresses(userId);
		if(addressesDto != null && !addressesDto.isEmpty()) {
			java.lang.reflect.Type listType = new  TypeToken<List<AddressesRest>>() {}.getType();
			addressesList =new ModelMapper().map(addressesDto, listType);
			
			for(AddressesRest addressRest : addressesList) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressRest.getAddressId())).withSelfRel();
				Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
				addressRest.add(addressLink);
				addressRest.add(userLink);
			}
		}

		
		return  CollectionModel.of(addressesList);
	}
	
	@GetMapping(path="/{userId}/addresses/{addressId}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId)
	{
		AddressDto addressesDto = addressService.getAddress(addressId);

		AddressesRest addressesRestModel = new ModelMapper().map(addressesDto, AddressesRest.class);
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(addressId)).withRel("addresses");
		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);
		return  EntityModel.of(addressesRestModel);
	}
	
	@GetMapping(path="/email-verification",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token)
	{
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		
		boolean isVerified = userService.verifyEmailToken(token);
		
		returnValue.setOperationResult( isVerified ? RequestOperationStatus.SUCCESS.name() : RequestOperationStatus.ERROR.name() );
		
		return returnValue;
	}
	
	@PostMapping(path = "/reset-password-request",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) throws Exception
	{
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
		
		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if(operationResult)
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue;
	}
	
	@PostMapping(path = "/reset-password",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) throws Exception
	{
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());
		
		returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if(operationResult)
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue;
	}
}
