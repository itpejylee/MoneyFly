package com.kakaopay.moneyfly.repository;

import com.kakaopay.moneyfly.domain.RecvMoney;

import com.kakaopay.moneyfly.domain.RecvMoneyPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface RecvRepository  extends JpaRepository<RecvMoney, RecvMoneyPK>,  RecvRepositoryCustom {
}
