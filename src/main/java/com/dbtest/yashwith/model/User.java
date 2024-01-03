package com.dbtest.yashwith.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String description;

    // Additional constructor, getter and setter
    public User(String id, String username, String password, String description){
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
