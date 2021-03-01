package com.kakaopay.moneyfly.domain;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * 받는 정보
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@IdClass(RecvMoneyPK.class)

public class RecvMoney {
    @Id
    private Long recvId;    //받기 ID

    @Id
    private int recvSeq;    //받기 Seq

    private long eachAmt;   //받기 금액

    private Long userId;    //받은 사용자 ID

    private LocalDateTime recvTime;     //받은 시각



    @Builder
    public RecvMoney(Long recvId, int recvSeq, long eachAmt) {
        this.recvId = recvId;
        this.recvSeq = recvSeq;
        this.eachAmt = eachAmt;
    }
}
