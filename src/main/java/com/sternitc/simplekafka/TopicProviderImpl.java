package com.sternitc.simplekafka;

public class TopicProviderImpl implements TopicProvider {
    @Override
    public String get(String base) {
        return base;
    }
}
