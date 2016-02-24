package ru.baccasoft.eatster.model;

public class OperationTotalModel extends CommonModel {

    private Integer scoresTotal;
    private Integer scoresSpent;
    private Integer operCount;
    private Integer operSum;
    private Integer commissionSum;

    public OperationTotalModel() {
        scoresTotal = null;
        scoresSpent = null;
        operCount = null;
        operSum = null;
        commissionSum = null;
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
    
    public Integer getCommissionSum() {
        return commissionSum;
    }

    public void setCommissionSum(Integer commissionSum) {
        this.commissionSum = commissionSum;
    }

    public Integer getCalcScoresBalance() {
        if (getScoresTotal() == null || getScoresSpent() == null) {
            return null;
        }
        return getScoresTotal()-getScoresSpent();
    }

    public Integer getCalcIncomeSum() {
        if (getCommissionSum() == null || getScoresTotal() == null) {
            return null;
        }
        return getCommissionSum() - getScoresTotal();
    }

    public Integer getCalcPayOffBalance() {
        if (getCommissionSum() == null || getScoresSpent() == null) {
            return null;
        }
        return getCommissionSum() - getScoresSpent();
    }
}
