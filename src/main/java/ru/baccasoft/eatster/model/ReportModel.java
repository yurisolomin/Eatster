package ru.baccasoft.eatster.model;

import java.util.ArrayList;
import java.util.List;

public class ReportModel extends CommonIdModel {
    public static final String STAT_FORMED = "formed";
    public static final String STAT_FORMED_RUS = "в стадии формирования";
    public static final String STAT_PAID = "paid";
    public static final String STAT_PAID_RUS = "оплачено";
    public static final String STAT_NOTPAID = "notpaid";
    public static final String STAT_NOTPAID_RUS = "не оплачено";

    private long restaurantId;
    private String restaurantName;
    private int scoresTotal;
    private int scoresSpent;
    private int operCount;
    private int checkSum;
    private String status;
    private int reportYear;
    private int reportMonth;
    private int commissionSum;

    public static class ReportStatus {
        String id;
        String name;
        public ReportStatus(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }
    
    public ReportModel() {
        scoresTotal = 0;
        scoresSpent = 0;
        operCount = 0;
        restaurantId = 0;
        restaurantName = "";
        checkSum = 0;
        status = "";
        reportYear = 0;
        reportMonth = 0;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReportYear() {
        return reportYear;
    }

    public void setReportYear(int reportYear) {
        this.reportYear = reportYear;
    }

    public int getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(int reportMonth) {
        this.reportMonth = reportMonth;
    }

    public int getCommissionSum() {
        return commissionSum;
    }

    public void setCommissionSum(int commissionSum) {
        this.commissionSum = commissionSum;
    }
    
    public int calcScoresBalance() {
        return getScoresTotal()-getScoresSpent();
    }

    public int calcIncomeSum() {
        return getCommissionSum() - getScoresTotal();
    }

    public int calcPayOffBalance() {
        return getCommissionSum() - getScoresSpent();
    }

    public String getStatusRus() {
        if (status.equals(STAT_FORMED)) {
            return STAT_FORMED_RUS;
        }
        if (status.equals(STAT_PAID)) {
            return STAT_PAID_RUS;
        }
        if (status.equals(STAT_NOTPAID)) {
            return STAT_NOTPAID_RUS;
        }
        return "";
    }
    
    public static List<ReportStatus> getStatusList() {
        List<ReportStatus> list = new ArrayList();
        list.add(new ReportStatus(STAT_FORMED,STAT_FORMED_RUS));
        list.add(new ReportStatus(STAT_PAID,STAT_PAID_RUS));
        list.add(new ReportStatus(STAT_NOTPAID,STAT_NOTPAID_RUS));
        return list;
    }
}
