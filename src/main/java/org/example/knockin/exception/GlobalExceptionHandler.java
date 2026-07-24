package org.example.knockin.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.global.api.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResponse<?> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());
        return handleExceptionInternal(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());
        return handleExceptionInternal(CommonErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class
    })
    public CommonResponse<?> handleBadRequestException(Exception e) {
        log.warn("Bad request: {}", e.getMessage());
        return handleExceptionInternal(CommonErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(", "));
        return handleExceptionInternal(CommonErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public CommonResponse<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("Data integrity violation: {}", e.getMostSpecificCause().getMessage());
        return handleExceptionInternal(CommonErrorCode.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResponse<?> handleMethodNotAllowedException(
            HttpRequestMethodNotSupportedException e
    ) {
        log.warn("Method not allowed: {}", e.getMessage());
        return handleExceptionInternal(CommonErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public CommonResponse<?> handleUnsupportedMediaTypeException(
            HttpMediaTypeNotSupportedException e
    ) {
        log.warn("Unsupported media type: {}", e.getMessage());
        return handleExceptionInternal(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    private CommonResponse<?> handleExceptionInternal(ErrorCode errorCode) {
        return CommonResponse.status(errorCode.getHttpStatus()).error(ErrorResponse.of(errorCode));
    }

    private CommonResponse<?> handleExceptionInternal(ErrorCode errorCode, String message) {
        return CommonResponse.status(errorCode.getHttpStatus()).error(ErrorResponse.of(errorCode, message));
    }
}
