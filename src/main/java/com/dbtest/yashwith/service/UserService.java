package com.dbtest.yashwith.service;

import antlr.Token;
import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.mappers.UserMapper;
import com.dbtest.yashwith.model.user.LoginRequest;
import com.dbtest.yashwith.model.user.UserProfile;
import com.dbtest.yashwith.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.security.TokenUtils;
import org.aspectj.weaver.patterns.IToken;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    @Autowired
    public UserService(UserRepository userRepository, TokenUtils tokenUtils) {
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
    }

    /**
     * Get all users
     * @return List of Users
     */
    public ApiResponse getAllUsers() {
        ApiResponse apiResponse = new ApiResponse();
        List<UserProfile> userProfiles = new ArrayList<>();
        userRepository.findAll().forEach(user -> userProfiles.add(UserMapper.INSTANCE.userToUserProfile(user)));
        apiResponse.setSuccess(true);
        apiResponse.setErrorCode("200");
        if(!userProfiles.isEmpty()) {
            apiResponse.setData(userProfiles);
        }
        apiResponse.setStatus("SUCCESS");
        return apiResponse;
    }

    /**
     * Get user profile from userId in the token
     * @param token jwt token from the request
     * @return
     */
    public ApiResponse getUserProfile(String token){
        String jwt = token.substring(7);
        String userId = tokenUtils.extractUserId(jwt);
        System.out.println("User Id: " + userId);

        UserProfile userProfile = new UserProfile();
        ApiResponse apiResponse = new ApiResponse();
        if(userRepository.findById(userId).isPresent()){
            userProfile = UserMapper.INSTANCE.userToUserProfile(userRepository.findById(userId).get());
            apiResponse.setSuccess(true);
            apiResponse.setStatus("SUCCESS");
            apiResponse.setErrorCode("200");
        }
        else{
            userProfile = null;
            apiResponse.setSuccess(false);
            apiResponse.setErrorCode("404");
            apiResponse.setStatus("DATA NOT FOUND");
            apiResponse.setError("No user information exist");
        }
        apiResponse.setData(userProfile);
        return apiResponse;
    }


    /**
     * Get userid using email -> Admin access only.
     * @param email email of the user
     * @return
     */
    public ApiResponse getUserId(String email){
        return new ApiResponse();
    }

    /**
     * Delete user by userId -> Admin access only
     * @param userId UserId of the user
     * @return
     */
    public ApiResponse deleteUser(String userId){
        ApiResponse apiResponse = new ApiResponse();
        try{
            userRepository.deleteById(userId);
            apiResponse.setStatus("SUCCESS");
            apiResponse.setErrorCode("200");
        }
        catch(Exception e){
            apiResponse.setStatus("NOT FOUND");
            apiResponse.setErrorCode("User Not Found");
            apiResponse.setErrorMessage("Cannot delete User. UserId not found.");
        }
        return apiResponse;
    }

    /**
     * Update User information
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
