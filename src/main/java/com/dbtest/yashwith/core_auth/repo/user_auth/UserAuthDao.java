package com.dbtest.yashwith.core_auth.repo.user_auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAuthDao {
    private final UserAuthRepo userAuthRepo;

    @Autowired
    public UserAuthDao(UserAuthRepo userAuthRepo) {
        this.userAuthRepo = userAuthRepo;
    }
}
