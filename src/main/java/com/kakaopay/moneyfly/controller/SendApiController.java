package com.kakaopay.moneyfly.controller;



import com.kakaopay.moneyfly.dto.CmpRecvMoneyDto;
import com.kakaopay.moneyfly.dto.CreateTokenDto;
import com.kakaopay.moneyfly.dto.ResultResponse;
import com.kakaopay.moneyfly.dto.SendMoneyDto;
import com.kakaopay.moneyfly.service.RecvService;
import com.kakaopay.moneyfly.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;

/**
 * 뿌리고 받기
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/sendMoney")
@RestController
@Validated
public class SendApiController {

    private final SendService sendService;
    private final RecvService recvService;

    /**
     * 뿌리기
     * @param userId 사용자 ID(header)
     * @param roomId 방 id (header)
     * @param sendAmt  뿌린 금액
     * @param sendCnt  받을 인원 수
     * @return  토큰
     */
    @PostMapping
    public ResultResponse sendMoney(@RequestHeader(value = "X-USER-ID") @Positive Long userId,
                                                    @RequestHeader(value = "X-ROOM-ID") @NotBlank @Length(min=5,max=5) String roomId,
                                                    @RequestParam(value = "sendAmt")  @Positive long sendAmt,
                                                    @RequestParam(value = "sendCnt")  @Positive  int sendCnt) {


        String token = sendService.genToken(userId, roomId, sendAmt, sendCnt);
        return new ResultResponse(new CreateTokenDto(token));
    }

    /**
     * 받기
     * @param userId 사용자 ID (header)
     * @param roomId 방 id(header)
     * @param token 토큰
     * @return  받은 금액
     */
    @PutMapping
    public ResultResponse recvMoney(@RequestHeader(value = "X-USER-ID") @Positive Long userId,
                                    @RequestHeader(value = "X-ROOM-ID") @NotBlank @Length(min=5,max=5) String roomId,
                                    @RequestParam(value = "token")  @Length(min=3,max=3) String token) {
        long eachAmt = recvService.getMoney(userId,roomId,token);
        return new ResultResponse(new CmpRecvMoneyDto(eachAmt));

    }

    /**
     * 뿌린 상세 조회
     * @param userId 사용자 ID (header)
     * @param token 토큰
     * @return 뿌린 상세
     */
    @GetMapping
    public ResultResponse inqSendInfo(@RequestHeader(value = "X-USER-ID") @Positive Long userId,
                                                    @RequestParam(value = "token") @NotBlank @Length(min=3,max=3) String token){

        SendMoneyDto sendInfo = sendService.findDetailByToken(userId, token);
        return new ResultResponse(sendInfo);
    }


}
