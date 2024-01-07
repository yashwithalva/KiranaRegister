package com.dbtest.yashwith.service;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.mappers.UserMapper;
import com.dbtest.yashwith.model.user.LoginRequest;
import com.dbtest.yashwith.model.user.UserProfile;
import com.dbtest.yashwith.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Get all users
     *
     * @return List of Users
     */
    public List<UserProfile> getAllUsers() {
        List<UserProfile> userInfos = new ArrayList<UserProfile>();
        // userRepository.findAll().forEach((user) -> userInfos.add(userMapper.toDto(user)));
        return userInfos;
    }

    /**
     * Create a new user.
     *
     * @param user User information required for posting
     * @return User id of created user
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * User login via email and password
     *
     * @param loginRequest consist of both username and password.
     * @return LoginResponse is returned.
     */
    public User userLogin(LoginRequest loginRequest) {
        return null;
    }

    /**
     * Get User Information of User by id
     *
     * @param id userId
     * @return USER user related Information.
     */
    public Optional<User> getUserInformationById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Update User information
     *
     * @param updateInformation Fields that can be updated
     * @return if update is successful or not.
     */
    public Boolean updateUserInfomation(String userId) {
        // User information has to be updated.
        return true;
    }

    /**
     * Update password by userId.
     *
     * @param userId id of the user.
     * @param passwords consist of old and new password.
     * @return True if password is updated successfully.
     */
    public Boolean updateUserPassword(String userId) {
        return true;
    }

    /**
     * Admin is allowed to update Role
     *
     * @param updateRole Consist of userId and role.
     * @return True if successfull update.
     */
    public Boolean updateUserRole() {
        return true;
    }
}
