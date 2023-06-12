package com.lom.futures.service.impl;

import com.lom.futures.service.FuturesClientService;
import com.lom.futures.service.helpe.AccountServiceHelper;
import com.lom.futures.util.JsonObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
@Service
public class BasicAccountService extends AccountServiceHelper {

    FuturesClientService client;
    JsonObjectMapper jsonObjectMapper;

}
