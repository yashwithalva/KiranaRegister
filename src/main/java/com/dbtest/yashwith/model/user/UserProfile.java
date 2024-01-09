package com.dbtest.yashwith.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfile {
    private String firstName;
    private String lastName;
    private Integer age;
    private String profilePicture;
    private UserProfile.Gender gender;
    private String phoneNumber;

    public enum Gender {
        MALE,
        FEMALE,
        OTHERS,
        N,
        OTHER,
        PREFER_NOT_TO_SAY
    }

    public long createdAt;
}
