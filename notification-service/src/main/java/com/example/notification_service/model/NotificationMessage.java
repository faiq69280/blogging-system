package com.example.notification_service.model;
import com.example.notification_service.constants.NotificationChannelType;

import java.util.Map;

public class NotificationMessage {
    private Object message;
    private  NotificationChannelType notificationChannelType;
    private String senderAddress;
    private String recieverAddress;
    private Map<String, Object> messageMetaData;

    public NotificationMessage() {}

    public NotificationMessage(Object message, NotificationChannelType notificationChannelType) {
        this.message = message;
        this.notificationChannelType = notificationChannelType;
    }

    public Object getMessage() { return message; }
    public void setMessage(Object message) { this.message = message; }

    public NotificationChannelType getNotificationChannelType() { return notificationChannelType; }
    public void setNotificationChannelType(NotificationChannelType notificationChannelType) {
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
