package ru.baccasoft.eatster.model;

public class UserSMSModel extends CommonIdModel {
    private String phone;
    private String text;

    public UserSMSModel() {
        phone = "";
        text = "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
