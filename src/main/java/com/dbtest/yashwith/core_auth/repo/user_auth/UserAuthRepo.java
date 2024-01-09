package com.dbtest.yashwith.core_auth.repo.user_auth;

import com.dbtest.yashwith.core_auth.entities.UserAuth;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAuthRepo extends MongoRepository<UserAuth, String> {
    //
}
