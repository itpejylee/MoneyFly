package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * RECV_CMM_ERROR(400, "K204", "받는중 에러가 발생하였습니다. 재시도 바랍니다.")
 */
public class RecvCmmErrException extends MoneyFlyException {
    public RecvCmmErrException() {
        super(ErrorCode.RECV_CMM_ERROR);
    }
}
