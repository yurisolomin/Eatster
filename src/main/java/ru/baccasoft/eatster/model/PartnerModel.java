package ru.baccasoft.eatster.model;

public class PartnerModel extends CommonNamedModel {
    private String password;
    private String contactName;
    private String contactPost;
    private String contactEmail;
    private String contactNote;
    private String contactPhone;
    private boolean admin;

    public PartnerModel() {
        super();
        password = "";
        contactName = "";
        contactPost = "";
        contactEmail = "";
        contactNote = "";
        contactPhone = "";
        admin = false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPost() {
        return contactPost;
    }

    public void setContactPost(String contactPost) {
        this.contactPost = contactPost;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNote() {
        return contactNote;
    }

    public void setContactNote(String contactNote) {
        this.contactNote = contactNote;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
