package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * INQR_CMM_ERROR(400, "K303", "조회도중 에러가 발생하였습니다. 재시도 바랍니다.")
 */
public class InqrCmmErrException extends MoneyFlyException {
    public InqrCmmErrException() {
        super(ErrorCode.INQR_CMM_ERROR);
    }
}
