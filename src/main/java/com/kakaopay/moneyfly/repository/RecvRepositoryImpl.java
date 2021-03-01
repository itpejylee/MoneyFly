package com.kakaopay.moneyfly.repository;

import com.kakaopay.moneyfly.domain.RecvMoney;
import com.kakaopay.moneyfly.dto.RecvMoneyDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.kakaopay.moneyfly.domain.QRecvMoney.recvMoney;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 받은정보 Repository 구현
 */
@RequiredArgsConstructor
@Repository
public class RecvRepositoryImpl implements RecvRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    //중복 받기 조회
    @Override
    public boolean isDuplicatedByUserId(Long recvId, Long userId) {

        RecvMoney recvInfo = queryFactory.selectFrom(recvMoney)
                .where(recvMoney.recvId.eq(recvId),
                        recvMoney.userId.eq(userId))
                .fetchOne();
        if(recvInfo == null){
            return false;
        }
        return true;
    }

    //받을 수 있는 항목 조회
    @Override
    public Optional<RecvMoney> findPossibleByRecvId(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(recvMoney)
                .where(recvMoney.recvId.eq(id),
                        recvMoney.userId.isNull(),
                        recvMoney.recvTime.isNull())
                .fetchFirst());
    }

    //받기
    @Override
    public Optional<Long> updateCmpState(Long id, int seq, Long userId) {
        return Optional.of(queryFactory.update(recvMoney)
                .where(recvMoney.recvId.eq(id),
                        recvMoney.recvSeq.eq(seq))
                .set(recvMoney.userId, userId)
                .set(recvMoney.recvTime, LocalDateTime.now())
                .execute());
    }

    //받기 완료된 목록 조회
    @Override
    public Optional<List<RecvMoneyDto>> findCmpListByRecvId(Long id) {
        return Optional.ofNullable(queryFactory.select(Projections.fields(RecvMoneyDto.class,
                recvMoney.eachAmt,
                recvMoney.userId
        ))
                .from(recvMoney)
                .where(recvMoney.recvId.eq(id),
                        recvMoney.userId.isNotNull(),
                        recvMoney.recvTime.isNotNull())
                .fetch());
    }
}
