package com.dbtest.yashwith.utils;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.user.TokenPayload;
import com.dbtest.yashwith.model.user.UserProfile;
import com.dbtest.yashwith.response.AuthResponse;
import com.dbtest.yashwith.security.TokenUtils;
import com.dbtest.yashwith.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthUtil {
    private final RefreshTokenUtil refreshTokenUtil;
    private final TokenUtils tokenUtils;

    public AuthUtil(RefreshTokenUtil refreshTokenUtil, TokenUtils tokenUtils) {
        this.refreshTokenUtil = refreshTokenUtil;
        this.tokenUtils = tokenUtils;
    }

    /**
     * get new access token with relevant
     *
     * @param user - user
     * @param sessionId - refreshToken objectId.
     * @return AuthResponse with accessToken and userProfile.
     */
    public AuthResponse getAccessToken(User user, String sessionId) {
        System.out.println("Get access token");
        UserInfo userInfo = new UserInfo();
        TokenPayload tokenPayload = new TokenPayload();
        tokenPayload.setUserId(user.getId());
        userInfo.setTokenPayload(tokenPayload);

        System.out.println("Added tokenPayload to userInfo");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtils.createToken(user, sessionId);

        System.out.println("Token: " + token);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(StringUtils.capitalize(user.getFirstName()));
        userProfile.setLastName(StringUtils.capitalize(user.getLastName()));
        userProfile.setPhoneNumber(user.getPhoneNumber());

        System.out.println("Completed basic setup");

        if (user.getProfilePicture() != null) {
            userProfile.setProfilePicture(user.getProfilePicture());
        }
        if (user.getAge() != null && user.getAge() != 0) {
            userProfile.setAge(userProfile.getAge());
        }
        if (user.getGender() != null) {
            userProfile.setGender(user.getGender());
        }
        userProfile.setCreatedAt(user.getCreatedAt().getTime());
        log.info(userProfile.toString());
        System.out.println(userProfile.toString());
        authResponse.setUser(userProfile);
        return authResponse;
    }
}
