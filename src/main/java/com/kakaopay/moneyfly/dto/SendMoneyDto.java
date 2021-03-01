package com.kakaopay.moneyfly.dto;



import lombok.*;

import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 뿌린 정보
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SendMoneyDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendTime;     //보낸 시각

    private long sendAmt;               //뿌린 금액

    private long cmpAmt;                //받기완료된 금액

    @Setter
    private List<RecvMoneyDto> compleList = new ArrayList<>();      //받기 완료된 정보(받은 금액, 받은 사용자 아이디)


}
