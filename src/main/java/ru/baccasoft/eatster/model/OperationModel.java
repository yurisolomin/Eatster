package ru.baccasoft.eatster.model;

public class OperationModel extends CommonIdModel {
    public static final String STATUS_NEW = "new";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_DELETED = "deleted";
    private long restaurantId;
    private long userId;
    private long waiterId;
    private String operDate;//YYYY-MM-DD
    private String operTime;//HH:MM
    private int checkSum;
    private String comment;
    private String status; 
    private String restaurantName;  //из связанной таблицы
    private String userName;        //из связанной таблицы
    private String userPhone;       //из связанной таблицы
    private String waiterName;      //из связанной таблицы
    private int cashbackBaseRate;
    private int cashbackBonusRate;
    private int decScore;
    private int addScore;
    private int commissionRate;

    public OperationModel() {
        super();
        restaurantId = 0;
        userId = 0;
        waiterId = 0;
        operDate="";
        operTime="";
        checkSum = 0;
        comment = "";
        restaurantName = "";
        userName = "";
        userPhone = "";
        waiterName = "";
        status = "";
        cashbackBaseRate = 0;
        cashbackBonusRate = 0;
        decScore = 0;
        addScore = 0;
        commissionRate = 0;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(long waiterId) {
        this.waiterId = waiterId;
    }

    public String getOperDate() {
        return DateAsString.getInstance().fixAsString(operDate);
    }

    public void setOperDate(String operDate) {
        this.operDate = DateAsString.getInstance().fixAsString(operDate);
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }
    
    public String getShortDateTime() {
        String shortDate;
        if (operDate.length() != 10 ) {
            shortDate = "00/00/00";
        } else {
            shortDate = operDate.substring(8,10)
                    +"/"+operDate.substring(5,7)
                    +"/"+operDate.substring(2,4);
        }
        return shortDate+" "+operTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getCashbackBaseRate() {
        return cashbackBaseRate;
    }

    public void setCashbackBaseRate(int cashbackBaseRate) {
        this.cashbackBaseRate = cashbackBaseRate;
    }

    public int getCashbackBonusRate() {
        return cashbackBonusRate;
    }

    public void setCashbackBonusRate(int cashbackBonusRate) {
        this.cashbackBonusRate = cashbackBonusRate;
    }

    public int getDecScore() {
        return decScore;
    }

    public void setDecScore(int decScore) {
        this.decScore = decScore;
    }

    public int getAddScore() {
        return addScore;
    }

    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public int getCalcCommissionSum() {
        return getCheckSum() * getCommissionRate() / 100;
    }

    public int getCalcIncomeSum() {
        return getCalcCommissionSum() - getAddScore();
    }

    public int getCalcPayOffBalance() {
        return getCalcCommissionSum() - getDecScore();
    }

}
