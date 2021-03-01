package com.kakaopay.moneyfly.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 에러형식 정의
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now();  //시간

    private int status; // HTTP 상태값

    private String code; // 사용자 지정 코드,

    private String message; //예외 메시지 저장






    static public ErrorResponse create(){
        return new ErrorResponse();
    }

    public ErrorResponse code(String code){
        this.code = code;
        return this;
    }
    public ErrorResponse status(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }

    public String getCode() {
        return StringUtils.defaultString(code);
    }
}
