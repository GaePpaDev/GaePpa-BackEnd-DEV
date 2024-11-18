package com.sparta.gaeppa.global.exception;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.error;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiError;
import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.members.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ApiResult<ApiError>> handleControllerException(ControllerException e) {
        log.error("ControllerException: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), new HttpHeaders(), e.getStatus());
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResult<ApiError>> handleServiceException(ServiceException e) {
        log.error("ServiceException: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), new HttpHeaders(), e.getStatus());
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<ApiResult<ApiError>> handleRepositoryException(RepositoryException e) {
        log.error("RepositoryException: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), new HttpHeaders(), e.getStatus());
    }

    @ExceptionHandler(AIException.class)
    public ResponseEntity<ApiResult<ApiError>> handleAIException(AIException e) {
        log.error("AIException: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), new HttpHeaders(), e.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<ApiError>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberService.ExistingMemberException.class)
    public ResponseEntity<ApiResult<ApiError>> handleExistingMemberException(MemberService.ExistingMemberException e) {
        log.error("Existing member error: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberService.DuplicateMemberException.class)
    public ResponseEntity<ApiResult<ApiError>> handleDuplicateMemberException(MemberService.DuplicateMemberException e) {
        log.error("Duplicate member error: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberService.EmailNotFoundException.class)
    public ResponseEntity<ApiResult<ApiError>> handleEmailNotFoundException(MemberService.EmailNotFoundException e) {
        log.error("not founded member email error: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ApiResult<ApiError>> handleAuthenticationServiceException(AuthenticationServiceException e) {
        log.error("Authentication service error: {}", e.getMessage());

        // Content-Type 관련 예외 처리
        if (e.getMessage().contains("지원하지 않는 Content-Type")) {
            return new ResponseEntity<>(error(ExceptionStatus.AUTHENTICATION_INVALID_CONTENT_TYPE.getMessage()), HttpStatus.BAD_REQUEST);
        }
        // JSON 파싱 오류 예외 처리
        if (e.getMessage().contains("요청 본문을 파싱할 수 없습니다")) {
            return new ResponseEntity<>(error(ExceptionStatus.AUTHENTICATION_JSON_PARSE_ERROR.getMessage()), HttpStatus.BAD_REQUEST);
        }
        // 이메일 미입력 예외 처리
        if (e.getMessage().contains("이메일이 입력되지 않았습니다")) {
            return new ResponseEntity<>(error(ExceptionStatus.AUTHENTICATION_EMAIL_NOT_PROVIDED.getMessage()), HttpStatus.BAD_REQUEST);
        }
        // 비밀번호 미입력 예외 처리
        if (e.getMessage().contains("비밀번호가 입력되지 않았습니다")) {
            return new ResponseEntity<>(error(ExceptionStatus.AUTHENTICATION_PASSWORD_NOT_PROVIDED.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(error(ExceptionStatus.AUTHENTICATION_FAILED.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResult<ApiError>> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Authentication failed: {}", e.getMessage());
        return new ResponseEntity<>(error(ExceptionStatus.AUTHENTICATION_INVALID_CREDENTIALS.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ApiResult<ApiError>> handleStorageException(StorageException e) {
        log.error("StorageException: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), new HttpHeaders(), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<ApiError>> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage());
        return new ResponseEntity<>(error(e.getMessage()), new HttpHeaders(),
                ExceptionStatus.INTERNAL_SERVER_ERROR.getStatus());
    }
}
