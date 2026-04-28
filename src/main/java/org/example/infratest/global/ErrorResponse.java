package org.example.infratest.global;

public record ErrorResponse(
        String code,
        String message
) {
    public ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }
}
