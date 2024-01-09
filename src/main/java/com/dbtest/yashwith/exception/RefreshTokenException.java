package com.dbtest.yashwith.exception;

import com.dbtest.yashwith.response.ApiResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenException extends RuntimeException {

    private ApiResponse apiResponse;

    public RefreshTokenException(String error, String message, String code) {
        ApiResponse response = new ApiResponse();
        response.setErrorMessage(message);
        response.setError(error);
        response.setErrorCode(code);
        this.setApiResponse(response);
    }
}
