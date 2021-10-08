package com.amritanshu.whatsappclone.Models;

import java.net.URL;

public class MessageModel {
    String uId, message, messageId;
    URL meetingURL;
    Long timestamp;

    public MessageModel(String uId, String message, Long timestamp, URL meetingURL) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.meetingURL = meetingURL;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessageModel(String uId, URL meetingURL) {
        this.uId = uId;
        this.meetingURL = meetingURL;
    }

    public URL getMeetingURL() {
        return meetingURL;
    }

    public void setMeetingURL(URL meetingURL) {
        this.meetingURL = meetingURL;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageModel()
    {}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

