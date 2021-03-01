package com.kakaopay.moneyfly.service;

import com.kakaopay.moneyfly.dto.SendMoneyDto;
import com.kakaopay.moneyfly.exception.details.InqrExpTimeException;
import com.kakaopay.moneyfly.exception.details.InqrNotAuthException;
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
class SendServiceTest {
    @Autowired
    RecvService recvService;
    @Autowired
    SendService sendService;

    @Autowired
    SendRepository sendRepository;
    @Autowired
    RecvRepository recvRepository;


    @Test
    @DisplayName("01. (정상)토큰 발급 ")
    public void test_01() throws Exception{
        //given (데이터 작성)
        Long userId = 1l;
        String roomId = "ROOMA";
        long sendAmt = 100;
        int sendCnt = 3;


        //when (조회요청)
        String token = sendService.genToken(userId, roomId, 100, 3);

        //then (확인)
        assertTrue(token.length() == 3);


    }

    @Test
    @DisplayName("02. (정상)정상조회 ")
    public void test_02() throws Exception{
        //given (데이터 작성)
        Long userId = 1l;
        String token = "Vup"; // 발급된 임시 토큰;

        //when (조회요청)
        SendMoneyDto detail = sendService.findDetailByToken(userId, token);

        //then (확인)
        assertAll(
                () -> assertNotNull(detail),    //null아님
                ()-> assertEquals(100, detail.getSendAmt()), //금액 일치
                ()-> assertTrue(detail.getCompleList().size() > 0)  //받은사람 존재
        );
    }

    @Test
    @DisplayName("03. (오류) 권한없음-다른ID조회  ")
    public void test_03() throws Exception{
        //given (데이터 작성)
        Long userId = 2l;
        String token = "Vup"; // 발급된 임시 토큰;


        //when (다른사람 ID로 조회)
        InqrNotAuthException exception = assertThrows(InqrNotAuthException.class, ()
                -> sendService.findDetailByToken(userId, token));

        //then (권한없음 K301)
        assertEquals("K301", exception.getErrorCode().getCode());
    }

    @Test
    @DisplayName("04.(오류) 만료된 조회-7일 경과  ")
    public void test_04() throws Exception{
        //given (데이터 작성)
        Long userId = 1l;
        String token = "nLS"; // 발급된 임시 토큰;


        //when (다른사람 ID로 조회)
        InqrExpTimeException exception = assertThrows(InqrExpTimeException.class, ()
                -> sendService.findDetailByToken(userId, token));

        //then (권한없음 K301)
        assertEquals("K302", exception.getErrorCode().getCode());
    }
}