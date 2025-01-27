
package com.example.backedapi.Controller;

import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    // This is a simple controller class that
    // will be used to handle the user requests

    private final UserService userService;

    Object getUser() {
        // This method will be used to get the user
        // from the database
        return null;
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public boolean createUser(@RequestBody User user) {
        UUID key = user.getKey();
        if (key != null) {
            throw new IllegalArgumentException("User already exists");
        }
        userService.createUser(user);
        // This method will be used to create a new user
        return true;
    }

    @GetMapping("/getAllUser")
    public List<User> getUsers() {
        // This method will be used to get all the users
        // from the database
//        userRepository.findAll();
        List<User> users = userService.getUser();
        System.out.println(users);
        return users;
    }


}