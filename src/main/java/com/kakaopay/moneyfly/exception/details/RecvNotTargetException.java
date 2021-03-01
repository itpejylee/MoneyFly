package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 *  RECV_NOT_TARGET(400,"K201","받을 수 있는 대상이 아닙니다.")
 */
public class RecvNotTargetException extends MoneyFlyException {
    public RecvNotTargetException() {
        super(ErrorCode.RECV_NOT_TARGET);
    }
}
