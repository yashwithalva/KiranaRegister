package com.dbtest.yashwith.exception;

import com.dbtest.yashwith.response.ApiResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TokenException extends RuntimeException {

    private ApiResponse apiResponse;

    public TokenException(String error, String message, String code) {
        ApiResponse response = new ApiResponse();
        response.setErrorMessage(error);
        response.setError(message);
        response.setErrorCode(code);
        this.setApiResponse(response);
    }
}
