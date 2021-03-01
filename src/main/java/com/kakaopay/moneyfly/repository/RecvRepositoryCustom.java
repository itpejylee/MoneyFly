package com.kakaopay.moneyfly.repository;

import com.kakaopay.moneyfly.domain.RecvMoney;
import com.kakaopay.moneyfly.dto.RecvMoneyDto;

import java.util.List;
import java.util.Optional;

/**
 * 받은정보 Repository
 */
public interface RecvRepositoryCustom {


    /**
     * 중복 받기 시도 조회
     * @param recvId
     * @param userId
     * @return
     */
        public boolean isDuplicatedByUserId(Long recvId, Long userId);

    /**
     *  받을 수 있는 항목 조회
     * @param id
     * @return 받을수 있는항목
     */
    public Optional<RecvMoney> findPossibleByRecvId(Long id);

    /**
     * 받기
     * @param id
     * @param seq
     * @param userId
     * @return
     */
    public Optional<Long> updateCmpState(Long id, int seq, Long userId);

    /**
     * 받기 완료된 목록 조회
     * @param id
     * @return 받기 완료 목록
     */
    public Optional<List<RecvMoneyDto>> findCmpListByRecvId(Long id);

}
