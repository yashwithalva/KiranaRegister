package com.dbtest.yashwith.core_auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenModel {
    private String refreshToken;

    // ObjectId of the Stored refresh token
    private String sessionId;
}
