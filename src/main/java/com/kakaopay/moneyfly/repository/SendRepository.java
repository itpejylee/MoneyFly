package com.kakaopay.moneyfly.repository;


import com.kakaopay.moneyfly.domain.SendMoney;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SendRepository extends JpaRepository<SendMoney, Long>,  SendRepositoryCustom {

}
