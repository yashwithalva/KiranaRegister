package com.dbtest.yashwith.response;

import com.dbtest.yashwith.core_auth.model.BasicAuthResponse;
import com.dbtest.yashwith.model.user.UserProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse extends BasicAuthResponse {

    private UserProfile user;

    public enum AuthType {
        LOGIN,
        SIGNUP
    }
}
