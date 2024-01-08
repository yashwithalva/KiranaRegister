package com.dbtest.yashwith.core_auth.service.auth;

import com.dbtest.yashwith.core_auth.model.BasicAuthResponse;
import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.mappers.UserMapper;
import com.dbtest.yashwith.model.auth.AuthRequest;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.repository.UserRepository;
import com.dbtest.yashwith.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse createUser(UserCreateRequest userCreateRequest) {
        System.out.println("Creating new user");
        BasicAuthResponse basicAuthResponse = new BasicAuthResponse();
        basicAuthResponse.setAccessToken("AccessToken-1234");
        basicAuthResponse.setRefreshToken("RefreshToken-1234");

        User user = UserMapper.INSTANCE.requestToUser(userCreateRequest);

        List<Role> roles = new ArrayList<>();
        if (userCreateRequest.getRole() == null) {
            roles.add(Role.ADMIN);
        } else {
            roles.add(userCreateRequest.getRole());
        }
        user.setRoles(roles);

        log.debug(user.toString());

        String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // TODO : Create tokens and add it to BasicAuthResponse.

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setErrorCode("200");
        apiResponse.setSuccess(true);
        apiResponse.setDisplayMessage("Successfully created user");
        apiResponse.setData(basicAuthResponse);

        return apiResponse;
    }

    @Override
    public ApiResponse loginUser(AuthRequest loginRequest) {
        ApiResponse apiResponse = new ApiResponse();
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {

            apiResponse.setErrorCode("400");
            apiResponse.setError("User not found");
            apiResponse.setSuccess(false);
        } else {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                apiResponse.setData("authenticated");
                apiResponse.setErrorMessage("no error");
                apiResponse.setSuccess(true);
                apiResponse.setErrorCode("200");
            } else {
                apiResponse.setErrorCode("400");
                apiResponse.setError("Password is wrong");
                apiResponse.setSuccess(false);
            }
        }

        return apiResponse;
    }
}
