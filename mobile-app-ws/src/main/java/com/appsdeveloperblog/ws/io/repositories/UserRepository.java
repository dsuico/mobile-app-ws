package com.appsdeveloperblog.ws.io.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token);
	
	@Query(
			value="SELECT * FROM users u WHERE u.email_verification_status = 1",
			countQuery="SELECT COUNT(*) FROM users u WHERE u.email_verification_status = 1",
			nativeQuery = true
		)
	Page<UserEntity> findAllUsersWithVerifiedEmailAddress(Pageable pageableRequest);
}
