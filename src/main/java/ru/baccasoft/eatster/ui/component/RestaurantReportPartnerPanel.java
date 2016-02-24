package ru.baccasoft.eatster.ui.component;

import java.util.List;
import ru.baccasoft.eatster.model.ReportModel;

public class RestaurantReportPartnerPanel extends RestaurantReportAbstractPanel {

    private static final long serialVersionUID = 8429802031562883814L;
    

    public RestaurantReportPartnerPanel() {
        super();
        buildLayout();
    }
    
    private void buildLayout() {
        tableReports.addContainerProperty("Месяц", String.class, null);
        tableReports.addContainerProperty("Оборот", Integer.class, null);
        tableReports.addContainerProperty("Баллов начислено", Integer.class, null);
        tableReports.addContainerProperty("Баллов списано", Integer.class, null);
        tableReports.addContainerProperty("Комиссия EatAction", Integer.class, null);
        tableReports.addContainerProperty("Сальдо выплат", Integer.class, null);
        tableReports.addContainerProperty("Статус платежа", String.class, null);
    }
    
    @Override
    public int fillTable(List<ReportModel> list) {
        int row = 0;
        for(ReportModel oper: list) {
            String month = String.format("%02d/%04d", oper.getReportMonth(),oper.getReportYear());
            tableReports.addItem(new Object[]{
                month,
                oper.getCheckSum(),
                oper.getScoresTotal(),
                oper.getScoresSpent(),
                oper.getCommissionSum(),
                oper.calcPayOffBalance(),
                oper.getStatusRus()
                }, ++row);            
        }
        return row;
    }
    
}
