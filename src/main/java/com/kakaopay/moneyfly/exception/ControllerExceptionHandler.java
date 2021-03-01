package com.kakaopay.moneyfly.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

/**
 * 에러처리 핸들러
 * http 메소드 검증
 * 값검증
 * 커스텀 에러
 * 기본에러
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    //메소드 체크
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.error("handleHttpRequestMethodNotSupportedException : "+ e.getMessage());

        final ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(ErrorCode.COMM_INV_REQUEST.getCode())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
    //요청값 검증
    @ExceptionHandler(ConstraintViolationException.class)
    protected  ResponseEntity<ErrorResponse>  handleConstraintViolationException(ConstraintViolationException e){
        log.error("handleConstraintViolationException : " + e.getMessage());

        final  ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(ErrorCode.COMM_INV_PARAM.getCode())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //요청값 검증
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        log.error("handleMissingServletRequestParameterException : " + e.getMessage());

        final ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(ErrorCode.COMM_INV_PARAM.getCode())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    //요청값 검증
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        log.error("handleMethodArgumentTypeMismatchException : " + e.getMessage());

        final ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.BAD_REQUEST.value())
                .code(ErrorCode.COMM_INV_PARAM.getCode())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //런타임 정의된 에러
    @ExceptionHandler(MoneyFlyException.class)
    protected ResponseEntity<ErrorResponse> handleMoneyFlyException(MoneyFlyException e){
        StringBuilder errMsg = new StringBuilder()
                .append("handleMoneyFlyException : ")
                .append(e.getErrorCode())
                .append(" (").append(e.getErrorCode().getCode()).append(")")
                .append("-").append(e.getMessage());

        log.error(errMsg.toString());

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .create()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(e.toString());

        return new ResponseEntity<>(response, HttpStatus.resolve(errorCode.getStatus()));
    }

    //모든예외
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("handleException : ", e.getMessage());

        ErrorResponse response = ErrorResponse
                .create()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code(ErrorCode.COMM_ERR_OCCUR.getCode())
                .message(e.toString());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
