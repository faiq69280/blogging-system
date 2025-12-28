package com.example.article_api.domain;

import java.util.Map;

public class UserModel {
    String userId;
    String userName;
    Map<String, String> contactSubscription;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, String> getContactSubscription() {
        return contactSubscription;
    }

    public void setContactSubscription(Map<String, String> contactSubscription) {
        this.contactSubscription = contactSubscription;
    }
}
