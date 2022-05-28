package com.appsdeveloperblog.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{

}
