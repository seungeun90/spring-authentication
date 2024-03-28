package com.spring.auth.domain.pass;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 본인확인 표준창 응답 데이터
 * https://manager.mobile-ok.com/guide/mok_std_guide/#26-app
 * 6. 본인확인-표준창 검증결과 요청
 * */

@Getter @Setter
@NoArgsConstructor
public class PassResult {
    private String resultCode;
    private String resultMsg;
    private String serviceId;
    private String encryptMOKKeyToken; //검증결과 요청타입이 MOKToken 일때 응답값
    private String encryptMOKResult;   //검증결과 요청타입이 MOKResult 일때 응답값으로 암호화된 본인확인 인증결과 데이터


    @Builder
    public PassResult(
             String resultCode,
             String resultMsg,
             String serviceId,
             String encryptMOKKeyToken,
             String encryptMOKResult
    ){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.serviceId = serviceId;
        this.encryptMOKKeyToken = encryptMOKKeyToken;
        this.encryptMOKResult = encryptMOKResult;
    }
}
