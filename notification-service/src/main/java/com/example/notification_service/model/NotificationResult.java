package com.example.notification_service.model;
import com.example.notification_service.constants.OutcomeType;

import java.util.Map;

public class NotificationResult {
    private Object payload;
    private OutcomeType outcomeType;
    private Map<String, Object> metaData;

    public NotificationResult(Object payload, OutcomeType outcomeType) {
        this.payload = payload;
        this.outcomeType = outcomeType;
    }

    public NotificationResult(Object payload, OutcomeType outcomeType, Map<String, Object> metaData) {
        this.payload = payload;
        this.outcomeType = outcomeType;
        this.metaData = metaData;
    }

    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }

    public OutcomeType getOutcomeType() { return outcomeType; }
    public void setOutcomeType(OutcomeType outcomeType) { this.outcomeType = outcomeType; }

    public Map<String, Object> getMetaData() { return metaData; }
    public void setMetaData(Map<String, Object> metaData) { this.metaData = metaData; }
}
