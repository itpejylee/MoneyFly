package com.kakaopay.moneyfly.repository;


import com.kakaopay.moneyfly.domain.SendMoney;
import com.kakaopay.moneyfly.dto.SendMoneyDto;
import com.querydsl.core.QueryResults;

import java.util.Optional;

/**
 * 뿌린 정보 Repository
 */
public interface SendRepositoryCustom {


    /**
     * 토큰 동일 존재 여부 파악
     * @param token
     * @return
     */
    public boolean isTokenDuplicated (String token);

    /**
     * 자신이 뿌린 정보 조회
     * @param userId 사용자 ID
     * @param token 방 ID
     * @return 자신이 뿌린 정보
     */
    public Optional<SendMoney> findByToken(Long userId, String token);

    /**
     * 대상 여부 판단(자신뿌린건 못받음, 동일대화박 사용자대상)
     * @param userId 사용자 ID
     * @param roomId 방 ID
     * @param token  토큰값
     * @return 대상 뿌린 정보
     */
    public Optional<SendMoney> findTargetByToken(Long userId, String roomId, String token);

    /**
     * 자신이 뿌린 정보 상세조회
     * @param sendId 뿌린정보 ID
     * @param userId 사용자 ID
     * @param token 토큰값
     * @return 자신이 뿌린 상세 정보
     */
    public Optional<SendMoneyDto> findDetailByToken(Long sendId, Long userId, String token);

}
