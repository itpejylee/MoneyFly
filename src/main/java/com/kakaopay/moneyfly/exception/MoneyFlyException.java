package com.kakaopay.moneyfly.exception;

import lombok.AccessLevel;

import lombok.Getter;

/**
 * 커스텀 에러처리
 */
@Getter
public class MoneyFlyException extends RuntimeException{

    private ErrorCode errorCode;

    public MoneyFlyException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
