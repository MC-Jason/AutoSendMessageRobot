package com.mc.config;

import com.mc.controller.RtxRobotMessageSender;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wechatwork-bot")
@ComponentScan
@Data
public class WebhookConfig {

    private String webhook;

    @Bean
    public RtxRobotMessageSender rtxRobotMessageSender() {
        return new RtxRobotMessageSender(webhook);
    }
}