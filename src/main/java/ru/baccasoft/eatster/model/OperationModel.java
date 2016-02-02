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
    private int score;//>0 add, < 0 dec
    private String comment;
    private String status; 
    private String restaurantName;  //из связанной таблицы
    private String userName;        //из связанной таблицы
    private String waiterName;      //из связанной таблицы

    public OperationModel() {
        super();
        restaurantId = 0;
        userId = 0;
        waiterId = 0;
        operDate="";
        operTime="";
        checkSum = 0;
        score = 0;
        comment = "";
        restaurantName = "";
        userName = "";
        waiterName = "";
        status = "";
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
    
}
