package com.example.backedapi.Repository;

import com.example.backedapi.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select u from User u where u.email = ?1")
   List<User> findByEmail(String email);
//
//    User findByPhone(String phone);
//
//    User findByEmailAndPassword(String email, String password);
//
//    User findByPhoneAndPassword(String phone, String password);
//
//    User findByEmailOrPhone(String email, String phone);

}
