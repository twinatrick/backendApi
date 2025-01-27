package com.example.backedapi.Repository;

import com.example.backedapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
//    User findByEmail(String email);
//
//    User findByPhone(String phone);
//
//    User findByEmailAndPassword(String email, String password);
//
//    User findByPhoneAndPassword(String phone, String password);
//
//    User findByEmailOrPhone(String email, String phone);

}
