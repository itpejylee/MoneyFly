package com.kakaopay.moneyfly.util;


import org.apache.commons.lang3.RandomStringUtils;

/**
 * 기본제공 유틸
 */
public class CommUtil {
    //뿌릴 금액 산정
    public static long calAmt(long sendAmt, int sendCnt){
        final long eachAmt = (sendAmt%sendCnt == 0) ? (sendAmt/sendCnt) :  ((sendAmt/sendCnt)+1);
        return eachAmt;
    }

    //토큰값 생성
    public static String generateToken(){
        final String genToken = RandomStringUtils.randomAlphabetic(3);
        return  genToken;

    }





}
