package com.sternitc.simplekafka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class User {

    private final String id = UUID.randomUUID().toString();
    private String name;
    private String role;
}
