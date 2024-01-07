package com.dbtest.yashwith.config;

import com.dbtest.yashwith.core_auth.repo.refreshtoken.RefreshTokenMongoDao;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemDaoHelper {

    @Getter private static SystemDaoHelper instance = null;

    @Getter private final RefreshTokenMongoDao refreshTokenMongoDao;

    @Autowired
    public SystemDaoHelper(RefreshTokenMongoDao refreshTokenMongoDao) {
        this.refreshTokenMongoDao = refreshTokenMongoDao;
    }

    @PostConstruct
    public void init() {
        instance = this;
    }
}
