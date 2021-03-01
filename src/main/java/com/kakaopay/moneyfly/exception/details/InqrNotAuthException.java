package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * INQR_NOT_AUTH(400,"K301","조회할 수 있는 권한이 없습니다.")
 */
public class InqrNotAuthException extends MoneyFlyException {
    public InqrNotAuthException() {
        super(ErrorCode.INQR_NOT_AUTH);
    }
}
