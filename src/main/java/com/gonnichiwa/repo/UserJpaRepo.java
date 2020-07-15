package com.gonnichiwa.repo;

import com.gonnichiwa.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {
	Optional<User> findByUid(String email);
}
