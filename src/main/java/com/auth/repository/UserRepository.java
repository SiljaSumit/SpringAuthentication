package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
		public boolean existsByUsername(@Param("username") String name);
		public UserEntity findByUsername(@Param("username") String name);
}
