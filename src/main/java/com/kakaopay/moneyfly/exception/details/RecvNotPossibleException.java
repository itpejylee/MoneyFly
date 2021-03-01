package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 * RECV_NOT_POSSIBLE(400, "K204", "모든 받기가 완료되었습니다.")
 */
public class RecvNotPossibleException extends MoneyFlyException {
    public RecvNotPossibleException() {
        super(ErrorCode.RECV_NOT_POSSIBLE);
    }
}
