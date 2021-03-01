package com.kakaopay.moneyfly.service;

import com.kakaopay.moneyfly.domain.RecvMoney;
import com.kakaopay.moneyfly.domain.SendMoney;
import com.kakaopay.moneyfly.dto.RecvMoneyDto;
import com.kakaopay.moneyfly.dto.SendMoneyDto;

import com.kakaopay.moneyfly.exception.details.InqrCmmErrException;
import com.kakaopay.moneyfly.exception.details.InqrExpTimeException;
import com.kakaopay.moneyfly.exception.details.InqrNotAuthException;
import com.kakaopay.moneyfly.exception.details.SendTokenIssueException;
import com.kakaopay.moneyfly.repository.RecvRepository;
import com.kakaopay.moneyfly.repository.SendRepository;
import com.kakaopay.moneyfly.util.CommUtil;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 보내기 처리
 */
@RequiredArgsConstructor
@Service
public class SendService {

    final private SendRepository sendRepository;
    final private RecvRepository recvRepository;

    /**
     * 토큰발급
     * @param userId 사용자ID
     * @param roomId 방 ID
     * @param sendAmt 보내는금액
     * @param sendCnt 받는 수
     * @return 토큰
     */
    @Transactional
    public String genToken(final long userId,
                           final String roomId,
                           final long sendAmt,
                           final int sendCnt) {

        //금액산정( 1원씩 더 지급하여 공평하게 나눔)

        final String genToken;

        try {
            final long eachAmt = CommUtil.calAmt(sendAmt,sendCnt);

            //토큰 중복 검사
            String tempToken;
            do {
                tempToken = CommUtil.generateToken();
            } while (sendRepository.isTokenDuplicated(tempToken));

            genToken = tempToken;
            //보낸정보
            SendMoney sendInfo = SendMoney.builder()
                    .userId(userId)
                    .roomId(roomId)
                    .amtCnt(sendCnt)
                    .token(genToken)
                    .sendAmt(sendAmt).build();

            sendInfo = sendRepository.save(sendInfo);
            if(sendInfo.getSendId() < 1){
                throw new SendTokenIssueException();
            }

            //받을수 있는정보
            List<RecvMoney> recvMoneyList = new ArrayList<>();
            for(int i=0;i<sendCnt;i++){
                RecvMoney recvMoney = RecvMoney.builder()
                        .recvId(sendInfo.getSendId())
                        .recvSeq(i+1)
                        .eachAmt(eachAmt).build();
                recvMoneyList.add(recvMoney);

            }
            recvRepository.saveAll(recvMoneyList);

        } catch (SendTokenIssueException e){
            throw e;
        }

        return genToken;
    }

    /**
     * 보낸정보 상세 조회
     * @param userId 사용자 ID
     * @param token 토큰값
     * @return 보낸정보 상세
     */
    @Transactional(readOnly = true)
    public SendMoneyDto findDetailByToken(final long userId,
                                          final String token)  {
        //대상 유효성 검사(뿌린사용자 여부)
        SendMoney sendInfo = sendRepository
                .findByToken(userId, token)
                .orElseThrow(()->new InqrNotAuthException());


        if(sendInfo.isSearchTermExpired()) {
          throw new InqrExpTimeException();
        }


        List<RecvMoneyDto> recvList = recvRepository
                .findCmpListByRecvId(sendInfo.getSendId())
                .orElseThrow(()->new InqrCmmErrException());

        SendMoneyDto sendDetail = sendRepository
                .findDetailByToken(sendInfo.getSendId(), userId, token)
                .orElseThrow(()->new InqrCmmErrException());

        sendDetail.setCompleList(recvList);


        return sendDetail;
    }
}
