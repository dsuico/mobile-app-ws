package com.appsdeveloperblog.ws.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Query(value="SELECT * FROM users u WHERE u.first_name = ?1", nativeQuery=true)
	List<UserEntity> findUsersByFirstName(String firstName);
	
	@Query(value="SELECT * FROM users u WHERE u.last_name = :lastName", nativeQuery=true)
	List<UserEntity> findUsersByLastName(@Param("lastName") String lastName);
	
	@Query(value="SELECT * FROM users u WHERE u.first_name LIKE %:keyword% OR u.last_name LIKE %:keyword%", nativeQuery=true)
	List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);
	
	@Query(value="SELECT u.first_name,u.last_name FROM users u WHERE u.first_name LIKE %:keyword% OR u.last_name LIKE %:keyword%", nativeQuery=true)
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE users u SET u.email_verification_status = :emailVerificationStatus WHERE u.user_id = :userId", nativeQuery=true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,@Param("userId") String userId);
}
