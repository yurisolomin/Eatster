package ru.baccasoft.eatster.model;

public class UserReportModel extends CommonNamedModel {
    private String birthday;//YYYY-MM-DD
    private String gender; //M,F
    private String email;
    private String phone;
    private String registrationDate;//YYYY-MM-DD
    private String promocode;
    private boolean bonusActivated;
    private String bonusDateEnd;
    private String friendPromocode;
    private int friendsCount;
    private int friendsBonusActivatedCount;
    private String friendsBonusDateEnd;
    private int scoresTotal;
    private int scoresSpent;
    private int scoresBalance;
    private int operCount;
    private int operSum;
    private int bonusPeriodEstimate;

    public UserReportModel() {
        super();
        birthday = "";
        gender = "";
        email = "";
        phone = "";
        registrationDate = "";
        promocode = "";
        bonusActivated = false;
        bonusDateEnd = "";
        friendPromocode = "";
        friendsCount = 0;
        friendsBonusActivatedCount = 0;
        friendsBonusDateEnd = "";
        scoresTotal = 0;
        scoresSpent = 0;
        scoresBalance = 0;
        operCount = 0;
        operSum = 0;
        bonusPeriodEstimate = 0;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public int getScoresBalance() {
        return scoresBalance;
    }

    public void setScoresBalance(int scoresBalance) {
        this.scoresBalance = scoresBalance;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
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

    public int getFriendsBonusActivatedCount() {
        return friendsBonusActivatedCount;
    }

    public void setFriendsBonusActivatedCount(int friendsBonusActivatedCount) {
        this.friendsBonusActivatedCount = friendsBonusActivatedCount;
    }

    public String getFriendsBonusDateEnd() {
        return friendsBonusDateEnd;
    }

    public void setFriendsBonusDateEnd(String friendsBonusDateEnd) {
        this.friendsBonusDateEnd = friendsBonusDateEnd;
    }

    public int getScoresTotal() {
        return scoresTotal;
    }

    public void setScoresTotal(int scoresTotal) {
        this.scoresTotal = scoresTotal;
    }

    public int getScoresSpent() {
        return scoresSpent;
    }

    public void setScoresSpent(int scoresSpent) {
        this.scoresSpent = scoresSpent;
    }

    public int getOperCount() {
        return operCount;
    }

    public void setOperCount(int operCount) {
        this.operCount = operCount;
    }

    public int getOperSum() {
        return operSum;
    }

    public void setOperSum(int operSum) {
        this.operSum = operSum;
    }

    public int getBonusPeriodEstimate() {
        return bonusPeriodEstimate;
    }

    public void setBonusPeriodEstimate(int bonusPeriodEstimate) {
        this.bonusPeriodEstimate = bonusPeriodEstimate;
    }

    public String getFriendPromocode() {
        return friendPromocode;
    }

    public void setFriendPromocode(String friendPromocode) {
        this.friendPromocode = friendPromocode;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
    
}
