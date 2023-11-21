package com.mc.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mc.config.WebhookConfig;
import com.mc.entity.Message;
import com.mc.entity.TextMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;


import java.io.IOException;
import java.util.List;

/**
 * 微信机器人消息发送器
 * @author yuyuanweb
 */
@Slf4j
@Data
public class RtxRobotMessageSender {

    private final String webhook;
  
    public WebhookConfig webhookConfig;

    public RtxRobotMessageSender(String webhook) {
        this.webhook = webhook;
    }

    /**
     * 支持自定义消息发送
     */
    public void sendMessage(Message message) throws Exception {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            send(textMessage);
        } else {
            throw new RuntimeException("Unsupported message type");
        }
    }

    /**
     * 发送文本（简化调用）
     */ 
    public void sendText(String content) throws Exception {
        sendText(content, null, null, false);
    }

    public void sendText(String content, List<String> mentionedList, List<String> mentionedMobileList,Boolean mentionAll) throws Exception {
        TextMessage textMessage = new TextMessage(content, mentionedList, mentionedMobileList, mentionAll);
        send(textMessage);
    }
    
    /**
     * 发送消息的公共依赖底层代码
     */
    private void send(Message message) throws Exception {
        String webhook = this.webhook;
        String messageJsonObject = JSONUtil.toJsonStr(message);
       // 未传入配置，降级为从配置文件中寻找
        if (StrUtil.isBlank(this.webhook)) {
            try {
                webhook = webhookConfig.getWebhook();
            } catch (Exception e) {
                System.out.println("没有找到配置项中的webhook,请检查：1.是否在application.yml中填写webhook 2.是否在spring环境下运行");
                throw new RuntimeException(e);
            }
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                messageJsonObject);
        Request request = new Request.Builder()
                .url(webhook)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.info("消息发送成功");
            } else {
                log.error("消息发送失败，响应码：{}", response.code());
                throw new Exception("消息发送失败，响应码：" + response.code());
            }
        } catch (IOException e) {
            log.error("发送消息时发生错误:" + e);
            throw new Exception("发送消息时发生错误", e);
        }
    }
}