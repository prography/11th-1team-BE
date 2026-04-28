package org.example.infratest.global;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {
    private final int code;
    private final T data;
    private final Object error;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(200, data, null);
    }

    public static <T> CommonResponse<T> success(HttpStatus status, T data) {
        return new CommonResponse<T>(status.value(), data, null);
    }

    public static CommonResponse<Void> successVoid() {
        return new CommonResponse<Void>(200, null, null);
    }

    public static CommonResponse<Void> successVoid(HttpStatus status) {
        return new CommonResponse<Void>(200, null, null);
    }
}
