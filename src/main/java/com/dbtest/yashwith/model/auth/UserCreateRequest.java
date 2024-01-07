package com.dbtest.yashwith.model.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String firstName;
    private String lastName;
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please Enter a valid email")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
