package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * INQR_EXP_TIME(400, "K302", "조회할 수 있는 기간이 만료되었습니다.")
 */
public class InqrExpTimeException extends MoneyFlyException {
    public InqrExpTimeException() {
        super(ErrorCode.INQR_EXP_TIME);
    }
}
