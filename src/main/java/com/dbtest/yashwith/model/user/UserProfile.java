package com.dbtest.yashwith.model.user;

import lombok.Data;

@Data
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
}
