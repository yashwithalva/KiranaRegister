package com.dbtest.yashwith.core_auth.service;

import com.dbtest.yashwith.core_auth.model.BasicAuthResponse;
import com.dbtest.yashwith.core_auth.model.RefreshTokenModel;
import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.mappers.UserMapper;
import com.dbtest.yashwith.model.auth.AuthRequest;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.repository.UserRepository;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.response.AuthResponse;
import com.dbtest.yashwith.utils.AuthUtil;
import com.dbtest.yashwith.utils.RefreshTokenUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final RefreshTokenUtil refreshTokenUtil;
    public final AuthUtil authUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenUtil refreshTokenUtil,
            AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenUtil = refreshTokenUtil;
        this.authUtil = authUtil;
    }

    @Override
    public AuthResponse createUser(UserCreateRequest userCreateRequest) {
        User user = UserMapper.INSTANCE.requestToUser(userCreateRequest);

        List<Role> roles = new ArrayList<>();
        if (userCreateRequest.getRole() == null) {
            roles.add(Role.ADMIN);
        } else {
            roles.add(userCreateRequest.getRole());
        }
        user.setRoles(roles);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        log.debug(user.toString());

        String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());
        user.setPassword(encodedPassword);
        User newUser = userRepository.save(user);
        return getAuthTokens(newUser);
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
                // If yes remove it and then add new ones.
                AuthResponse authResponse = getAuthTokens(user.get());

                apiResponse.setData(authResponse);
                apiResponse.setErrorMessage("Authenticated");
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

    /**
     * Get Login Auth Tokens.
     *
     * @return Access token and refreshToken.
     */
    private AuthResponse getAuthTokens(User user) {
        RefreshTokenModel refreshTokenModel =
                refreshTokenUtil.saveRefreshToken(
                        user.getId(), user.getPhoneNumber(), user.getEmail());
        log.debug(refreshTokenModel.toString());

        BasicAuthResponse basicAuthResponse = new BasicAuthResponse();
        basicAuthResponse.setRefreshToken(refreshTokenModel.getRefreshToken());
        AuthResponse authResponse = authUtil.getAccessToken(user, refreshTokenModel.getSessionId());
        authResponse.setRefreshToken(refreshTokenModel.getRefreshToken());
        authResponse.setPhoneNumber(user.getPhoneNumber());
        return authResponse;
    }
}
