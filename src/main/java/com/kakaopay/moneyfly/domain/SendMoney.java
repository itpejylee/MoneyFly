package com.kakaopay.moneyfly.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 뿌린정보
 */
@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SendMoney {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sendId;        //뿌린 ID

    private Long userId;        //뿌린 사용자 ID

    private String roomId;      //뿌린 방 ID

    private int amtCnt;         //뿌린 대상 인원수

    @Column(unique = true)
    private String token;       //발급 토큰

    private long sendAmt;       //뿌린 금액

    @CreationTimestamp
    private LocalDateTime sendTime;     //뿌린 시각

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recvId")
    private List<RecvMoney> compleList = new ArrayList<>();     //받은 정보



    @Builder
    public SendMoney(Long userId, String roomId, int amtCnt, String token, long sendAmt) {
        this.userId = userId;
        this.roomId = roomId;
        this.amtCnt = amtCnt;
        this.token = token;
        this.sendAmt = sendAmt;
        this.sendTime = sendTime;
    }

    //10분경 파악
    public boolean isRecvTermExpired(){
        if(LocalDateTime.now().isAfter(this.getSendTime().plusMinutes(10))){
            return true;
        }
        return false;
    }

    //7일경과 파악
    public boolean isSearchTermExpired(){
        if(LocalDateTime.now().isAfter(this.getSendTime().plusDays(7))){
            return true;
        }
        return false;
    }









}
