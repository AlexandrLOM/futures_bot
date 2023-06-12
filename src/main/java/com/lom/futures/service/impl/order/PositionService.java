package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.Params;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.service.impl.BasicAccountService;
import com.lom.futures.util.ClientParametersUtil;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService extends BasicAccountService {
    public PositionService(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public List<Position> positionInformation(Symbol symbol, Long timestamp)  throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.symbol.name(), symbol.name());
        params.put(Params.timestamp.name(), timestamp);
        var result = client.account().positionInformation(params);
        return jsonObjectMapper.convertPositionInformation(result);

    }
}
