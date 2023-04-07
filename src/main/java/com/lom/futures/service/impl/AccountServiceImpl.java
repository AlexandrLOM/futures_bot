package com.lom.futures.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.ClientParametersUtil;
import com.lom.futures.util.JsonObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class AccountServiceImpl implements AccountService {

    FuturesClientService client;
    JsonObjectMapper jsonObjectMapper;

    @Override
    public List<FuturesBalance> futuresBalance() {
        var params = ClientParametersUtil.createEmptyParameters();
        var result = client.account().futuresAccountBalance(params);
        List<FuturesBalance> futuresBalances = new ArrayList<>();
        try {
            futuresBalances = jsonObjectMapper.convertFuturesBalance(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); //TODO: to implement
        }
        return futuresBalances.stream()
                .filter(Objects::nonNull)
                .filter(futuresBalance -> futuresBalance.getUpdateTime() != 0L)
                .toList();
    }


}
