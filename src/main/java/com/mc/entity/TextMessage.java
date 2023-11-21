package com.mc.entity;

import lombok.Data;

import java.util.List;

@Data
public class TextMessage extends Message {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 被提及者userId列表
     */
    private List<String> mentionedList;

    /**
     * 被提及者电话号码列表
     */
    private List<String> mentionedMobileList;
  
    /**
     * 提及全体
     */
    private Boolean mentionAll = false;

    public TextMessage(String content, List<String> mentionedList, List<String> mentionedMobileList, Boolean mentionAll) {
        this.content = content;
        this.mentionedList = mentionedList;
        this.mentionedMobileList = mentionedMobileList;
        this.mentionAll = mentionAll;
    }

    public TextMessage(String content) {
        this(content, null, null, false);
    }
}