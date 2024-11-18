package com.sparta.gaeppa.global.util;

import lombok.Getter;

public class ApiResponseUtil {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response);
    }

    public static ApiResult<ApiError> error(Throwable throwable) {
        return new ApiResult<>(false, new ApiError(throwable));
    }

    public static ApiResult<ApiError> error(String message) {
        return new ApiResult<>(false, new ApiError(message));
    }

    @Getter
    public static class ApiError {
        private final String message;

        ApiError(Throwable throwable) {
            this(throwable.getMessage());
        }

        ApiError(String message) {
            this.message = message;
        }

    }

    public static class ApiResult<T> {
        private final boolean success;
        private final T response;

        public ApiResult(boolean success, T response) {
            this.success = success;
            this.response = response;
        }

        public boolean isSuccess() {
            return success;
        }

        public T getResponse() {
            return response;
        }
    }
}