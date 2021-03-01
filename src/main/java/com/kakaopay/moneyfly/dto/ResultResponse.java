package com.kakaopay.moneyfly.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ResultResponse {
    private LocalDateTime timestamp = LocalDateTime.now();  //시간

    private int status; // HTTP 상태값

    private String message; //예외 메시지 저장

    private Object result;

    public ResultResponse(Object result) {
        this.status = HttpStatus.OK.value();
        this.message = "정상적으로 처리되었습니다.";
        this.result = result;

    }
}
