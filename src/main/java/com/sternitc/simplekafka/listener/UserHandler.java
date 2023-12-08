package com.sternitc.simplekafka.listener;

public interface UserHandler {

    void handle(String key, String correlationId, String user);
}
