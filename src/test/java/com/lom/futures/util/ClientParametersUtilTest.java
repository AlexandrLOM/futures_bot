package com.lom.futures.util;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ClientParametersUtilTest {

    @Test
    void createEmptyParameters() {
        LinkedHashMap<String, Object> clientParameters = new LinkedHashMap<>();
        var resultClientParameters = ClientParametersUtil.createEmptyParameters();

        assertEquals(clientParameters, resultClientParameters );
    }
}