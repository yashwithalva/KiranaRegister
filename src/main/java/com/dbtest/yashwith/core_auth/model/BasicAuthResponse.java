package com.dbtest.yashwith.core_auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BasicAuthResponse {
    String refreshToken;
    String accessToken;
    String phoneNumber;
}
