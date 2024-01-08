package com.dbtest.yashwith.model.auth;

import com.dbtest.yashwith.enums.Role;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserCreateRequest {
    private final String firstName;
    private final String lastName;
    private final String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please Enter a valid email")
    private final String email;

    @NotBlank(message = "Phone number cannot be blank")
    private final String phoneNumber;

    Role role;
}
