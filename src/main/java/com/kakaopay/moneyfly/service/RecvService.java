package com.kakaopay.moneyfly.service;


import com.kakaopay.moneyfly.domain.RecvMoney;
import com.kakaopay.moneyfly.domain.SendMoney;
import com.kakaopay.moneyfly.exception.details.*;
import com.kakaopay.moneyfly.repository.RecvRepository;
import com.kakaopay.moneyfly.repository.SendRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 받기처리
 */
@RequiredArgsConstructor
@Service
public class RecvService {
    private final SendRepository sendRepository;
    private final RecvRepository recvRepository;

    /**
     * 받기
     * @param userId 사용자 ID
     * @param roomId 방 ID
     * @param token 토큰값
     * @return 받은 금액
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public long getMoney(final Long userId, final String roomId, final String token)  {

        //자기자신이 아니고, 같은방인지 확인
        SendMoney sendMoneyInfo = sendRepository
                .findTargetByToken(userId, roomId, token)
                .orElseThrow(()->new RecvNotTargetException());

        //10분 유효 검증
        if (sendMoneyInfo.isRecvTermExpired()) {
            throw new RecvExpTimeException();
        }

        //중복 받기
        if(recvRepository.isDuplicatedByUserId(sendMoneyInfo.getSendId(), userId)){
            throw new RecvDupActException();
        }


        //받을 대상 조회
        RecvMoney recvMoneyInfo = recvRepository
                .findPossibleByRecvId(sendMoneyInfo.getSendId())
                .orElseThrow(()->new RecvNotPossibleException());

        //받기 및 기록
        recvRepository
                .updateCmpState(recvMoneyInfo.getRecvId(), recvMoneyInfo.getRecvSeq(), userId)
                .orElseThrow(()->new RecvCmmErrException());

        return recvMoneyInfo.getEachAmt();

    }
}
