package org.example.infratest.global.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.infratest.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {
    private final int status;
    private final T data;
    private final ErrorResponse error;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(200, data, null);
    }

    public static <T> CommonResponse<T> success(T data, HttpStatus status) {
        return new CommonResponse<>(status.value(), data, null);
    }

    public static CommonResponse<Void> successVoid() {
        return new CommonResponse<>(200, null, null);
    }

    public static CommonResponse<Void> successVoid(HttpStatus status) {
        return new CommonResponse<>(status.value(), null, null);
    }

    public static CommonResponse<Void> fail(ErrorCode errorCode) {
        return new CommonResponse<>(
                errorCode.getHttpStatus().value(),
                null,
                ErrorResponse.of(errorCode)
        );
    }
}
