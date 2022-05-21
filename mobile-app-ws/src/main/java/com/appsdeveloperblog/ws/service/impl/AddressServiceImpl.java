package com.appsdeveloperblog.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.ws.io.entity.UserEntity;
import com.appsdeveloperblog.ws.io.repositories.AddressRepository;
import com.appsdeveloperblog.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.ws.service.AddressService;
import com.appsdeveloperblog.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.ws.ui.model.response.ErrorMessages;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);

		if(userEntity == null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		
		for(AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity == null)
			throw new RuntimeException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		return new ModelMapper().map(addressEntity, AddressDto.class);
	}

}
