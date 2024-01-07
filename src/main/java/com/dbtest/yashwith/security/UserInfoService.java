package com.dbtest.yashwith.security;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.user.TokenPayload;
import com.dbtest.yashwith.repository.UserRepository;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    /**
     * Loads userInfo from the token
     *
     * @param token - jwt token of the request.
     * @return UserInfo
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        UserInfo userInfo = new UserInfo();
        TokenPayload tokenPayload = tokenUtils.getUserFromTokenPayload(token);
        userInfo.setTokenPayload(tokenPayload);
        return userInfo;
    }

    /**
     * Get username from userId.
     *
     * @param userId - user id
     * @return fullname(if available) or phoneNumber or email.
     */
    public String getName(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? getName(user) : "";
    }

    public long getUserCreatedAt(String userId) {
        User user = getUser(userId);
        return user == null ? new Date().getTime() : user.getCreatedAt().getTime();
    }

    /**
     * Get name if available else provide email or phoneNumber.
     *
     * @param user User Object.
     * @return fullname or phoneNumber or email
     */
    public String getName(User user) {
        if (user.getFirstName() != null && user.getLastName() != null) {
            return user.getFirstName() + " " + user.getLastName();
        } else if (user.getFirstName() != null) {
            return user.getFirstName();
        } else if (user.getPhoneNumber() != null) {
            return user.getPhoneNumber();
        } else {
            return user.getEmail();
        }
    }

    public String getFirstName(User user) {
        return user.getFirstName();
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
