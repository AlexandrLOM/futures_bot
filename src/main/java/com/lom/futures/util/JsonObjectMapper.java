package com.lom.futures.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lom.futures.dto.FuturesBalance;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class JsonObjectMapper {

    ObjectMapper objectMapper;

    public List<FuturesBalance> convertFuturesBalance(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, new TypeReference<List<FuturesBalance>>() {
        });
    }
}
