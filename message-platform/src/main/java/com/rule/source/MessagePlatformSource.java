package com.rule.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessagePlatformSource {

    @Output("erbadagang-output")
    MessageChannel erbadagangOutput();

    @Output("trek-output")
    MessageChannel trekOutput();
}
