package com.kakaopay.moneyfly.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 토큰
 */
@Getter
public class CreateTokenDto {
    private String token;

    public CreateTokenDto(String token) {
        this.token = token;
    }
}
