package ru.baccasoft.eatster.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class PushTokenModel {

    private String phone;
    private String deviceId;
    private String pushToken;
    private String platform;

    public PushTokenModel(String phone, String deviceId, String pushToken, String platform) {
        this.phone = phone;
        this.deviceId = deviceId;
        this.pushToken = pushToken;
        this.platform = platform;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
