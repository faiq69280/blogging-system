package com.example.article_api.async.events;

import java.util.Map;

public class MessagePayload {
    private Object message;
    private  String notificationChannelType;
    private String senderAddress;
    private String recieverAddress;
    private Map<String, Object> messageMetaData;

    public MessagePayload() {}

    public MessagePayload(Object message, String notificationChannelType, String recieverAddress) {
        this.message = message;
        this.notificationChannelType = notificationChannelType;
        this.recieverAddress = recieverAddress;
    }

    public Object getMessage() { return message; }
    public void setMessage(Object message) { this.message = message; }

    public String getNotificationChannelType() { return notificationChannelType; }
    public void setNotificationChannelType(String notificationChannelType) {
        this.notificationChannelType = notificationChannelType;
    }

    public String getSenderAddress() { return senderAddress; }
    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }

    public Map<String, Object> getMessageMetaData() {
        return messageMetaData;
    }

    public void setMessageMetaData(Map<String, Object> messageMetaData) {
        this.messageMetaData = messageMetaData;
    }

    public String getRecieverAddress() {
        return recieverAddress;
    }

    public void setRecieverAddress(String recieverAddress) {
        this.recieverAddress = recieverAddress;
    }
}