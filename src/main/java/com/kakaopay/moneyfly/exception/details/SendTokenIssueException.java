package com.kakaopay.moneyfly.exception.details;

import com.kakaopay.moneyfly.exception.ErrorCode;
import com.kakaopay.moneyfly.exception.MoneyFlyException;

/**
 *  SEND_TKN_ISSUE(400,"K101", "뿌리기중 에러가 발생하였습니다. 재시도 바랍니다.")
 */
public class SendTokenIssueException extends MoneyFlyException {
    public SendTokenIssueException() {
        super(ErrorCode.SEND_TKN_ISSUE);
    }
}
