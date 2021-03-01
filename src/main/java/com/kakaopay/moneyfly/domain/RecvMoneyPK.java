package com.kakaopay.moneyfly.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import javax.persistence.Id;
import java.io.Serializable;

/**
 * RecvMoney 기본키 정의
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class RecvMoneyPK implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    private Long recvId;        //받기 ID

    @Id
    @EqualsAndHashCode.Include
    private int recvSeq;        //받기 Seq

}
