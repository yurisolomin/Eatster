package ru.baccasoft.eatster.model;

public class UserModel extends CommonNamedModel {
    private String birthday;//YYYY-MM-DD
    private String gender; //M,F
    private String email;
    private String phone;
    private String registrationDate;//YYYY-MM-DD
    private int referrals;
    private String password;
    private String promocode;
    private boolean bonusActivated;
    private String bonusDateEnd;
    private Long friendUserId;
    private String friendBonusDateEnd;
    private String friendPromocode;

    public UserModel() {
        super();
        birthday = "";
        gender = "";
        email = "";
        phone = "";
        registrationDate = "";
        referrals = 0;
        password = "";
        promocode = null;
        bonusActivated = false;
        bonusDateEnd = "";
        friendUserId = null;
        friendBonusDateEnd = "";
        friendPromocode = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationDate() {
        return DateAsString.getInstance().fixAsString(registrationDate);
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = DateAsString.getInstance().fixAsString(registrationDate);
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return DateAsString.getInstance().fixAsString(birthday);
    }

    public void setBirthday(String birthday) {
        this.birthday = DateAsString.getInstance().fixAsString(birthday);
    }

    public int getReferrals() {
        return referrals;
    }

    public void setReferrals(int referrals) {
        this.referrals = referrals;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public boolean isBonusActivated() {
        return bonusActivated;
    }

    public void setBonusActivated(boolean bonusActivated) {
        this.bonusActivated = bonusActivated;
    }

    public String getBonusDateEnd() {
        return bonusDateEnd;
    }

    public void setBonusDateEnd(String bonusDateEnd) {
        this.bonusDateEnd = bonusDateEnd;
    }

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendBonusDateEnd() {
        return friendBonusDateEnd;
    }

    public void setFriendBonusDateEnd(String friendBonusDateEnd) {
        this.friendBonusDateEnd = friendBonusDateEnd;
    }

    public String getFriendPromocode() {
        return friendPromocode;
    }

    public void setFriendPromocode(String friendPromocode) {
        this.friendPromocode = friendPromocode;
    }
    
}
