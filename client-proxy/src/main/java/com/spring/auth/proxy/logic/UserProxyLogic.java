package com.spring.auth.proxy.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.spring.auth.proxy.service.UserProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserProxyLogic implements UserProxyService {
    private  final MediatorCaller mediatorCaller;

    @Override
    public Account getAccount(Account account) {
        Map<String, String> header = new HashMap<>();
        ResponseData<Account> data = mediatorCaller.post(
                MediatorType.REST,
                MediatorService.ACCOUNT,
                "/account",
                account,
                header,
                new TypeReference<ResponseData<Account>>() {
                }
        ).block();
        return data.getData();
    }


    @Override
    public Long save(Account account) {
        Map<String, String> header = new HashMap<>();
        ResponseData<Long> data = mediatorCaller.post(
                MediatorType.REST,
                MediatorService.ACCOUNT,
                "/",
                account,
                header,
                new TypeReference<ResponseData<Long>>() {
                }
        ).block();
        return data.getData();
    }


}
