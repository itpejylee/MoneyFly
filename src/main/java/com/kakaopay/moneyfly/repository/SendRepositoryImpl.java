package com.kakaopay.moneyfly.repository;

import com.kakaopay.moneyfly.domain.RecvMoney;
import com.kakaopay.moneyfly.domain.SendMoney;
import com.kakaopay.moneyfly.dto.SendMoneyDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.kakaopay.moneyfly.domain.QSendMoney.sendMoney;
import static com.kakaopay.moneyfly.domain.QRecvMoney.recvMoney;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * 뿌린 정보 Repository 구현
 */
@RequiredArgsConstructor
@Repository

public class SendRepositoryImpl  implements SendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    //토큰 동일 존재 여부 파악
    @Override
    public boolean isTokenDuplicated(String token) {
        SendMoney sendInfo = queryFactory.selectFrom(sendMoney)
                .where(sendMoney.token.eq(token))
                .fetchOne();
        if(sendInfo == null){
            return false;
        }
        return true;
    }

    //자신이 뿌린 정보
    @Override
    public Optional<SendMoney> findByToken(Long userId, String token) {
        return Optional.ofNullable(queryFactory.selectFrom(sendMoney)
                .where(sendMoney.token.eq(token),
                        sendMoney.userId.eq(userId))
                .fetchOne());
    }

    //대상 여부 판단(자신뿌린건 못받음, 동일대화박 사용자대상)
    @Override
    public Optional<SendMoney> findTargetByToken(Long userId, String roomId, String token) {

       return Optional.ofNullable(queryFactory.select(sendMoney)
               .from(sendMoney)
               .where(sendMoney.userId.ne(userId),
                       sendMoney.roomId.eq(roomId),
                       sendMoney.token.eq(token))
               .fetchOne());
    }


    //자신이 뿌린 정보 상세조회
    @Override
    public Optional<SendMoneyDto> findDetailByToken(Long sendId, Long userId, String token) {
        return Optional.ofNullable(queryFactory.select(
                Projections.fields(SendMoneyDto.class,
                        sendMoney.sendTime,
                        sendMoney.sendAmt,
                        ExpressionUtils.as(
                                JPAExpressions.select(recvMoney.eachAmt.sum())
                                        .from(recvMoney)
                                        .where(recvMoney.recvId.eq(sendId),
                                                recvMoney.userId.isNotNull(),
                                                recvMoney.recvTime.isNotNull()),
                                "cmpAmt")
                ))
                .from(sendMoney)
                .where(sendMoney.token.eq(token),
                        sendMoney.userId.eq(userId))
                .fetchOne());
    }


}
