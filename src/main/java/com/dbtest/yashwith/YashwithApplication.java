package com.dbtest.yashwith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class YashwithApplication {

    public static void main(String[] args) {
        SpringApplication.run(YashwithApplication.class, args);
    }
}
