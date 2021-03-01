package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * RECV_DUP_ACTION(400,"K202","한번만 받을수 있습니다.")
 */
public class RecvDupActException extends MoneyFlyException {
    public RecvDupActException() {
        super(ErrorCode.RECV_DUP_ACTION);
    }
}
