package com.dbtest.yashwith.core_auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenModel {
    private String refreshToken;
    private String sessionId;
}
