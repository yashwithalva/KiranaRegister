package com.dbtest.yashwith.repository;

import com.dbtest.yashwith.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);
    User findByEmailAndPassword(String email, String password);

    List<User> findUsersByRole(String role);
}
