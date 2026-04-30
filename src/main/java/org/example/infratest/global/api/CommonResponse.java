package org.example.infratest.global.api;

import lombok.Getter;
import lombok.NonNull;
import org.example.infratest.global.exception.ErrorCode;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public class CommonResponse<T> extends ResponseEntity<@NonNull T> {
    private final ErrorResponse error;

    public CommonResponse(HttpStatusCode status, ErrorResponse error) {
        super(status);
        this.error = error;
    }

    public CommonResponse(@Nullable T body, HttpStatusCode status, ErrorResponse error) {
        super(body, status);
        this.error = error;
    }

    public CommonResponse(HttpHeaders headers, HttpStatusCode status, ErrorResponse error) {
        super(headers, status);
        this.error = error;
    }

    public CommonResponse(@Nullable T body, @Nullable HttpHeaders headers, int rawStatus, ErrorResponse error) {
        super(body, headers, rawStatus);
        this.error = error;
    }

    public CommonResponse(@Nullable T body, @Nullable HttpHeaders headers, HttpStatusCode statusCode,
                          ErrorResponse error) {
        super(body, headers, statusCode);
        this.error = error;
    }

    public static <T> CommonResponse<T> success(T body) {
        return new CommonResponse<>(body, HttpStatus.OK, null);
    }

    public static <T> CommonResponse<T> success(T body, HttpStatus status) {
        return new CommonResponse<>(body, status, null);
    }

    public static CommonResponse<Void> successVoid() {
        return new CommonResponse<>(HttpStatus.OK, null);
    }

    public static CommonResponse<Void> successVoid(HttpStatus status) {
        return new CommonResponse<>(status, null);
    }

    public static CommonResponse<Void> fail(ErrorCode errorCode) {
        return new CommonResponse<>(
                errorCode.getHttpStatus(),
                ErrorResponse.of(errorCode)
        );
    }
}
