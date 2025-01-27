package com.example.backedapi.Service;

import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final   UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUser() {
        return userRepository.findAll();
    }
}
