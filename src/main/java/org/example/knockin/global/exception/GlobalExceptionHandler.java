package org.example.knockin.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.global.api.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResponse<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("[{}] {} - BusinessException: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return handleExceptionInternal(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResponse<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("[{}] {} - IllegalArgumentException: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return handleExceptionInternal(CommonErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class
    })
    public CommonResponse<?> handleBadRequestException(Exception e, HttpServletRequest request) {
        log.warn("[{}] {} - Bad request: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return handleExceptionInternal(CommonErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(", "));
        log.warn("[{}] {} - Validation failed: {}", request.getMethod(), request.getRequestURI(), message);
        return handleExceptionInternal(CommonErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResponse<?> handleMethodNotAllowedException(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        log.warn("[{}] {} - Method not allowed: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return handleExceptionInternal(CommonErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public CommonResponse<?> handleUnsupportedMediaTypeException(
            HttpMediaTypeNotSupportedException e,
            HttpServletRequest request
    ) {
        log.warn("[{}] {} - Unsupported media type: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return handleExceptionInternal(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e, HttpServletRequest request) {
        log.error("[{}] {} - Unhandled exception", request.getMethod(), request.getRequestURI(), e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    private CommonResponse<?> handleExceptionInternal(ErrorCode errorCode) {
        return CommonResponse.status(errorCode.getHttpStatus()).error(ErrorResponse.of(errorCode));
    }

    private CommonResponse<?> handleExceptionInternal(ErrorCode errorCode, String message) {
        return CommonResponse.status(errorCode.getHttpStatus()).error(ErrorResponse.of(errorCode, message));
    }
}
