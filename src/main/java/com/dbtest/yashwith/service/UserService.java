package com.dbtest.yashwith.service;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.UpdatePassword;
import com.dbtest.yashwith.repository.UserRepository;
import com.dbtest.yashwith.response.Wip_responseLogin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Get all users
     * @return List of Users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Create a new user.
     * @param user User information required for posting
     * @return User id of created user
     */
    public String createUser(User user){
        User newUser =  userRepository.save(user);
        return newUser.getId();
    }

    /**
     * User login via email and password
     *
     * @param email email id used for login
     * @param password password required to login
     * @return LoginResponse is returned.
     */
    public Wip_responseLogin userLogin(String email, String password){
        User findUser = userRepository.findByEmailAndPassword(email, password);
        if (findUser == null) return null;
        return new Wip_responseLogin(findUser.getRole(), findUser.getId(), findUser.getName());
    }

    /**
     * Get User Information of User by id
     * @param id userId
     * @return USER user related Information.
     */
    public Optional<User> getUserInformationById(String id){
        return userRepository.findById(id);
    }

    /**
     * TODO: Update User information
     * @param usernamePassword
     * @return
     */
    public Boolean UpdateUserInfomation(){}

    /**
     * Update password
     * @param userId id of the user.
     * @param updatePassword old and new password.
     * @return
     */
    public Boolean UpdatePassword(UpdatePassword password){
        //
    }



}
