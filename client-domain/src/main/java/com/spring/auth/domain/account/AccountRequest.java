package com.spring.auth.domain.account;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest {
    private String email;
    private String ci;
    private String searchType;

    @Builder
    public AccountRequest(String email,
                          String ci,
                          String searchType){
        this.email = email;
        this.ci = ci;
        this.searchType = searchType;
    }


}
