package com.spring.auth.rest;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailDto {
    private String email;
    private String code;
}
