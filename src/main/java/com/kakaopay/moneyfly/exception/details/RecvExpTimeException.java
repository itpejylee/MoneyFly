package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * RECV_EXP_TIME(400, "K203", "받을 수 있는 시간이 만료되었습니다(10분)")
 */
public class RecvExpTimeException extends MoneyFlyException {

    public RecvExpTimeException() {
        super(ErrorCode.RECV_EXP_TIME);
    }
}
