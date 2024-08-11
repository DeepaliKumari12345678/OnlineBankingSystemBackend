package com.banking.backend.repository;

import com.banking.backend.Entity.LoginHistory;
import com.banking.backend.Entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

	Optional<LoginHistory> findTopByUserOrderByLoginTimeDesc(User user);

}
