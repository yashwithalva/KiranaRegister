package com.dbtest.yashwith.model.user;

import com.dbtest.yashwith.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPayload {
    private String userId;
    private String phoneNumber;
    private User.Role role;
    private List<User.Role> allowedRoles = new ArrayList<>();
    private String email;
}
