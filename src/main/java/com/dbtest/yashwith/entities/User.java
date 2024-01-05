package com.dbtest.yashwith.entities;

import com.dbtest.yashwith.enums.Role;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document("users")
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private int age;
    private String phone;
    private Role role;

    public User(String id, String name, Role role, String password, String phone, int age) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
        this.phone = phone;
        this.age = age;
    }
}
