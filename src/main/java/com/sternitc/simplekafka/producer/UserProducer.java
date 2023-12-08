package com.sternitc.simplekafka.producer;

import com.sternitc.simplekafka.domain.User;

public interface UserProducer {

    void send(User user);
}
