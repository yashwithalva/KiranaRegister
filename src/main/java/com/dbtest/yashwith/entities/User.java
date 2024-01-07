package com.dbtest.yashwith.entities;

import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.model.user.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@CompoundIndex(def = "{'phoneNumber' : 1, 'countryCode': '1'}", name = "phoneNumber_countryCode")
public class User extends DateAudit {

    @Id private String id;
    private String firstName;
    private String lastName;

    public enum Role {
        USER,
        ADMIN,
        EMPLOYEE
    };

    private List<Role> roles;

    @Indexed(unique = true)
    private String email;

    private String password;
    private Integer age;

    @Indexed(unique = true)
    private String phoneNumber;

    private String countryCode;

    private String profilePicture;
    private boolean isDeleted;
    private UserProfile.Gender gender;
}
