package com.rule.client;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String ERBADAGANG_INPUT = "erbadagang-input";
    String TREK_INPUT = "trek-input";

    @Input(ERBADAGANG_INPUT)
    SubscribableChannel erbadagangInput();

    @Input(TREK_INPUT)
    SubscribableChannel trekInput();
}
