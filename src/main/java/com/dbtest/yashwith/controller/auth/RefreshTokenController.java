package com.dbtest.yashwith.controller.auth;

import com.dbtest.yashwith.core_auth.model.BasicAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/refresh")
@Slf4j
public class RefreshTokenController {

    @PostMapping("/getAccessToken")
    ResponseEntity<BasicAuthResponse> getAccessToken(
            @RequestBody BasicAuthResponse basicAuthRequest) {
        log.debug(basicAuthRequest.getAccessToken());
        log.debug(basicAuthRequest.getRefreshToken());
        log.debug(basicAuthRequest.getPhoneNumber());

        // TODO : Get Access Token
        BasicAuthResponse response = new BasicAuthResponse();
        response.setRefreshToken("New refresh token given out");
        response.setAccessToken("New access token");
        return ResponseEntity.ok(response);
    }
}
