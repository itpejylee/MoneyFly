package com.kakaopay.moneyfly.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 받은 금액
 */
@Getter
public class CmpRecvMoneyDto {

    private long recvAmt; //받은 금액

    public CmpRecvMoneyDto(long recvAmt) {
        this.recvAmt = recvAmt;
    }
}

