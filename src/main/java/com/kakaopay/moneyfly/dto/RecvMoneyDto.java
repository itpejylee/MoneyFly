package com.kakaopay.moneyfly.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 받기 완료된 정보
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecvMoneyDto {

    private long eachAmt;   //받은금액
    private  Long userId;    //사용자 아이디
}
