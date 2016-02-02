package ru.baccasoft.eatster.model;

public class OperationReportModel extends CommonModel {

    private long restaurantId;
    private String restaurantName;
    private int scoresTotal;
    private int scoresSpent;
    private int scoresBalance;
    private int operCount;
    private int checkSum;

    public OperationReportModel() {
        scoresTotal = 0;
        scoresSpent = 0;
        scoresBalance = 0;
        operCount = 0;
        restaurantId = 0;
        restaurantName = "";
        checkSum = 0;
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

    public int getScoresBalance() {
        return scoresBalance;
    }

    public void setScoresBalance(int scoresBalance) {
        this.scoresBalance = scoresBalance;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }
    
}
