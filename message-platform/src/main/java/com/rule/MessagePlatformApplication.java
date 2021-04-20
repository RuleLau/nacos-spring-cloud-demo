package com.rule;

import com.rule.source.MessagePlatformSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@SpringBootApplication
@EnableBinding({Source.class, MessagePlatformSource.class})
public class MessagePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagePlatformApplication.class, args);
	}

}
