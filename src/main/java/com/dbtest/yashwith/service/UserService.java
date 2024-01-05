package com.dbtest.yashwith.service;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.*;
import com.dbtest.yashwith.repository.UserRepository;
import com.dbtest.yashwith.response.Wip_responseLogin;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    public User createUser(User user){
        return userRepository.save(user);
    }


    /**
     * User login via email and password
     *
     * @param loginRequest consist of both username and password.
     * @return LoginResponse is returned.
     */
    public User userLogin(LoginRequest loginRequest){
        return userRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
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
     * Update User information
     * @param updateInformation Fields that can be updated
     * @return if update is successful or not.
     */
    public Boolean updateUserInfomation(String userId, UpdateUserInfo updateInformation){
        // User information has to be updated.
        Optional<User> user = getUserInformationById(userId);
        if(user.isEmpty()) {
            return false;
        }

        // TODO: Implement mapper to set details in user.
        if(updateInformation.getAge() != 0){
            user.get().setAge(updateInformation.getAge());
        }

        if(updateInformation.getName() != null){
            user.get().setName(updateInformation.getName());
        }

        if(updateInformation.getPhone() != null){
            user.get().setPhone(updateInformation.getName());
        }

        userRepository.save(user.get());

        return true;
    }


    /**
     * Update password by userId.
     * @param userId id of the user.
     * @param passwords consist of old and new password.
     * @return True if password is updated successfully.
     */
    public Boolean updateUserPassword(String userId, UpdatePassword passwords){
        Optional<User> user = getUserInformationById(userId);
        if(user.isEmpty()){
            return false;
        }

        if(user.get().getPassword().contentEquals(passwords.getOldpassword())){
            user.get().setPassword(passwords.getNewpassword());
        }
        else{
            return false;
        }

        return true;
    }


    /**
     * Admin is allowed to update Role
     * @param updateRole Consist of userId and role.
     * @return True if successfull update.
     */
    public Boolean updateUserRole(UpdateUserRole updateRole){
        //
    }

}
