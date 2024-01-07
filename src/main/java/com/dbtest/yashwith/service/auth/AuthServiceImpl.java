package com.dbtest.yashwith.service.auth;

import com.dbtest.yashwith.core_auth.model.BasicAuthResponse;
import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.mappers.UserMapper;
import com.dbtest.yashwith.model.auth.AuthRequest;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.repository.UserRepository;
import com.dbtest.yashwith.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    public UserRepository userRepository;
    public PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse createUser(UserCreateRequest userCreateRequest) {
        BasicAuthResponse basicAuthResponse = new BasicAuthResponse();
        User user = UserMapper.INSTANCE.createRequestToModel(userCreateRequest);
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
        return null;
    }
}
