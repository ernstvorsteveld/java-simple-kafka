package com.sternitc.simplekafka.listener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserHandlerKafka implements UserHandler {
    @Override
    public void handle(String key, String correlationId, String user) {
        log.info(key, correlationId, user);
    }
}
