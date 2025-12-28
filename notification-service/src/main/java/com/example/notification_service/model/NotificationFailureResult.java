package com.example.notification_service.model;

import com.example.notification_service.constants.OutcomeType;

import java.util.Map;

public class NotificationFailureResult extends NotificationResult {
    Throwable failureRoot;

    public NotificationFailureResult(Object payload, OutcomeType outcomeType, Throwable failureRoot) {
        super(payload, outcomeType);
        this.failureRoot = failureRoot;
    }

    public NotificationFailureResult(Object payload, OutcomeType outcomeType, Map<String, Object> metaData, Throwable failureRoot) {
        super(payload, outcomeType, metaData);
        this.failureRoot = failureRoot;
    }

    public Throwable getFailureRoot() {
        return failureRoot;
    }

    public void setFailureRoot(Throwable failureRoot) {
        this.failureRoot = failureRoot;
    }
}
