package ru.baccasoft.eatster.model;

public class OperationReportModel extends CommonModel {

    private long restaurantId;
    private String restaurantName;
    private int scoresTotal;
    private int scoresSpent;
    private int operCount;
    private int checkSum;
    private int commissionSum;

    public OperationReportModel() {
        scoresTotal = 0;
        scoresSpent = 0;
        operCount = 0;
        restaurantId = 0;
        restaurantName = "";
        checkSum = 0;
        commissionSum = 0;
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

    public int getCommissionSum() {
        return commissionSum;
    }

    public void setCommissionSum(int commissionSum) {
        this.commissionSum = commissionSum;
    }

    public int getCalcScoresBalance() {
        return getScoresTotal()-getScoresSpent();
    }

    public int getCalcIncomeSum() {
        return getCommissionSum() - getScoresTotal();
    }

    public int getCalcPayOffBalance() {
        return getCommissionSum() - getScoresSpent();
    }

}
