package com.dbtest.yashwith.service;

import com.dbtest.yashwith.model.User;
import com.dbtest.yashwith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User getUserDescription(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
