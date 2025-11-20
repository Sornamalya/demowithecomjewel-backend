package com.example.demowithecomjewel.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demowithecomjewel.model.*;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	  User findByEmail(String email);

}
