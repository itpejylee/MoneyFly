package com.kakaopay.moneyfly.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀 에러 상세 정의
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode{

    //K0XX  공통
    COMM_INV_REQUEST(405,"K001", "잘못된 요청 정보 입니다."),
    COMM_INV_PARAM(400,"K002", "잘못된 파라메터 정보 입니다."),
    COMM_ERR_OCCUR(500,"K003", "오류가 발생하였습니다. 재시도 바랍니다."),


    //K1XX 뿌리기
    SEND_TKN_ISSUE(400,"K101", "뿌리기중 에러가 발생하였습니다. 재시도 바랍니다."),

    //K2XX 토근받기
    RECV_NOT_TARGET(400,"K201","받을 수 있는 대상이 아닙니다."),
    RECV_DUP_ACTION(400,"K202","한번만 받을수 있습니다."),
    RECV_EXP_TIME(400, "K203", "받을 수 있는 시간이 만료되었습니다(10분)"),
    RECV_NOT_POSSIBLE(400, "K204", "모든 받기가 완료되었습니다."),
    RECV_CMM_ERROR(400, "K205", "받는중 에러가 발생하였습니다. 재시도 바랍니다."),

    //K3XX 토큰조회
    INQR_NOT_AUTH(400,"K301","조회할 수 있는 권한이 없습니다."),
    INQR_EXP_TIME(400, "K302", "조회할 수 있는 기간이 만료되었습니다."),
    INQR_CMM_ERROR(400, "K303", "조회도중 에러가 발생하였습니다. 재시도 바랍니다.");




    private int status;
    private String code;
    private String message;



}
