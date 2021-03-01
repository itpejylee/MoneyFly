package com.kakaopay.moneyfly.service;

import com.kakaopay.moneyfly.exception.ErrorResponse;
import com.kakaopay.moneyfly.exception.MoneyFlyException;
import com.kakaopay.moneyfly.exception.details.RecvDupActException;
import com.kakaopay.moneyfly.exception.details.RecvExpTimeException;
import com.kakaopay.moneyfly.exception.details.RecvNotPossibleException;
import com.kakaopay.moneyfly.exception.details.RecvNotTargetException;
import com.kakaopay.moneyfly.repository.RecvRepository;
import com.kakaopay.moneyfly.repository.SendRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class RecvServiceTest {
    @Autowired
    RecvService recvService;
    @Autowired
    SendService sendService;

    @Autowired
    SendRepository sendRepository;
    @Autowired
    RecvRepository recvRepository;

    @Test
    @DisplayName("01. (정상)돈받기 ")
    public void test_01() throws Exception{
        //given (뿌리기 토큰생성)
        Long userId = 1l;
        String roomId = "ROOMA";
        long sendAmt = 100;
        int sendCnt = 3;
        String token = sendService.genToken(userId, roomId, 100, 3);

        //when (돈 받기)
        userId = 2l;
        long eachAmt = recvService.getMoney(userId, roomId, token);

        //then (받은돈 확인 34)
        assertEquals(eachAmt, 34l);
    }

    @Test
    @DisplayName("02. (오류)받을수 없는 대상-같은사람이 받기")
    public void test_02() throws Exception{
        //given (뿌리기 토큰생성)
        Long userId = 1l;
        String roomId = "ROOMA";
        long sendAmt = 100;
        int sendCnt = 3;
        String token = sendService.genToken(userId, roomId, 100, 3);

        //when (같은 사람 돈 받기)
        RecvNotTargetException exception = assertThrows(RecvNotTargetException.class, ()
                -> recvService.getMoney(userId, roomId, token));

        //then (받은돈 확인 K201)
        assertEquals("K201", exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("03. (오류)한번만 받을수 있음-두번 받기")
    public void test_03() throws Exception{
        //given (뿌리기 토큰생성)
        Long userId = 1l;
        String roomId = "ROOMA";
        long sendAmt = 100;
        int sendCnt = 3;
        String token = sendService.genToken(userId, roomId, 100, 3);

        Long newUserId = 2l;


        //when (같은 사람 돈 받기)
        //1번
        recvService.getMoney(newUserId, roomId, token);

        //2번 (예외)
        RecvDupActException exception = assertThrows(RecvDupActException.class, ()
                -> recvService.getMoney(newUserId, roomId, token));

        //then (중복 받기 K202)
        assertEquals("K202", exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("04. (오류)받을수 있는 만료시간 지남")
    public void test_04() throws Exception{
        //given (뿌리기 토큰생성)
        Long userId = 2l;
        String roomId = "ROOMA";
        String token =  "SLg";  //10분경과 토큰

        //when (받을수 있는 시간 지난 건에 대해 요청)
        //2번 (예외)
        RecvExpTimeException exception = assertThrows(RecvExpTimeException.class, ()
                -> recvService.getMoney(userId, roomId, token));

        //then (10분 초과 K203)
        assertEquals("K203", exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("05. (오류)모든 받기 완료")
    public void test_05() throws Exception{
        //given (뿌리기 토큰생성)
        Long userId = 4l;
        String roomId = "ROOMA";
        String token =  "Vup";  //모든 받기 완료된 토큰 (임의)

        //when (같은 사람 돈 받기)

        //2번 (예외)
        RecvNotPossibleException exception = assertThrows(RecvNotPossibleException.class, ()
                -> recvService.getMoney(userId, roomId, token));

        //then (모든 받기 완료 K204)
        assertEquals("K204", exception.getErrorCode().getCode());
    }
}