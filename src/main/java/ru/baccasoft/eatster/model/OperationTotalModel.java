package ru.baccasoft.eatster.model;

public class OperationTotalModel extends CommonModel {

    Integer scoresTotal;
    Integer scoresSpent;
    Integer scoresBalance;
    Integer operCount;
    Integer operSum;

    public OperationTotalModel() {
        scoresTotal = null;
        scoresSpent = null;
        scoresBalance = null;
        operCount = null;
        operSum = null;
    }

    public Integer getScoresTotal() {
        return scoresTotal;
    }

    public void setScoresTotal(Integer scoresTotal) {
        this.scoresTotal = scoresTotal;
    }

    public Integer getScoresSpent() {
        return scoresSpent;
    }

    public void setScoresSpent(Integer scoresSpent) {
        this.scoresSpent = scoresSpent;
    }

    public Integer getOperCount() {
        return operCount;
    }

    public void setOperCount(Integer operCount) {
        this.operCount = operCount;
    }

    public Integer getOperSum() {
        return operSum;
    }

    public void setOperSum(Integer operSum) {
        this.operSum = operSum;
    }

    public Integer getScoresBalance() {
        return scoresBalance;
    }

    public void setScoresBalance(Integer scoresBalance) {
        this.scoresBalance = scoresBalance;
    }
}
