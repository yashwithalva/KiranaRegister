package com.dbtest.yashwith.utils;

import com.dbtest.yashwith.repository.UserRepository;
import com.dbtest.yashwith.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SystemUtils {

    @Getter private static SystemUtils instance;
    @Getter private final UserRepository userRepository;
    @Getter private final UserService userService;
    @Getter private final ObjectMapper objectMapper = new ObjectMapper();
    @Getter private final ObjectMapper ksfObjectMapper = new ObjectMapper();

    @Getter private final ObjectMapper qwikCilverObejectMapper = new ObjectMapper();

    public SystemUtils(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        qwikCilverObejectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    @PostConstruct
    public void init() {
        instance = this;
    }
}
