package ru.baccasoft.eatster.ui.component;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.eatster.service.ReportService;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantReportPanel extends VerticalLayout implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    private final static int FIX_ROW_COUNT = 10;
    private final static String BUTTON_SHOW_CAPTION = "Показать";
    private final static String LABEL_PRESS_SHOW_CAPTION = "Нажмите кнопку '"+BUTTON_SHOW_CAPTION+"' для получения данных";
    private final static String LABEL_NO_DATA = "НЕТ ДАННЫХ.";
    private final float BUTTON_WIDTH = 4f;
    private final Table tableReports = new Table();
    private final Button buttonExportToExcel = new Button("Выгрузить в XLS", this);
    private final Button buttonShow = new Button(BUTTON_SHOW_CAPTION, this);
    private final Button buttonContract = new Button("Договор", this);
//    private final Link buttonContract = new Link("Договор",null);
    private final Label labelNoData = new Label();
    private long restaurantId;

    public RestaurantReportPanel() {
        buildLayout();
    }

    private void buildLayout() {
        //
        GridLayout gridReport = new GridLayout(2, 1);
        gridReport.setSpacing(true);
        gridReport.setMargin(new MarginInfo(true,true,false,true));
        gridReport.setWidth("100%");
        //
        HorizontalLayout layout = new HorizontalLayout(buttonContract,buttonExportToExcel);
        layout.setSpacing(true);
        gridReport.addComponent(buttonShow, 0, 0);
        gridReport.addComponent(layout, 1, 0);
        gridReport.setColumnExpandRatio(1,2f);
        gridReport.setComponentAlignment(layout, Alignment.TOP_RIGHT);
        //
        tableReports.addContainerProperty("Месяц", String.class, null);
        tableReports.addContainerProperty("Баллов начислено", Integer.class, null);
        tableReports.addContainerProperty("Баллов списано", Integer.class, null);
        tableReports.addContainerProperty("Итого", Integer.class, null);
        tableReports.addContainerProperty("Статус платежа", String.class, null);
        tableReports.setSizeFull();
        tableReports.setColumnCollapsingAllowed(true);
        tableReports.setColumnReorderingAllowed(true);
        //
        VerticalLayout reportPanel = new VerticalLayout(gridReport,tableReports,labelNoData);
        reportPanel.setSpacing(true);
        reportPanel.setSizeFull();
        addComponent(reportPanel);
        //
        buttonShow.setWidth(BUTTON_WIDTH,Unit.CM);
        buttonContract.setWidth(BUTTON_WIDTH,Unit.CM);
        buttonExportToExcel.setWidth(BUTTON_WIDTH,Unit.CM);
        //
        //buttonContract.setTargetName("_blank"); //new window
        //buttonContract.setStyleName(Reindeer.BUTTON_DEFAULT);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }
           
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == buttonShow) {
            showReports();
        }
        if (event.getButton() == buttonExportToExcel) {
            ExcelExport excelExport;
            excelExport = new ExcelExport(tableReports);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Оплата");
            excelExport.export();
        }
        if (event.getButton() == buttonContract) {
            String contractUrl = getUI().getAppProp().getContractUrl().trim();
            if (!contractUrl.isEmpty()) {
                getUI().getPage().open(contractUrl, "_blank");
            }
        }
    }

    public void showReports() {
        ReportService reportService = getUI().getReportService();
        List<ReportModel> list = reportService.findByRestaurant(restaurantId);
        tableReports.removeAllItems();
        int row = 0;
        for(ReportModel oper: list) {
            String month = String.format("%02d/%04d", oper.getReportMonth(),oper.getReportYear());
            tableReports.addItem(new Object[]{
                month,
                oper.getScoresTotal(),
                oper.getScoresSpent(),
                oper.getScoresBalance(),
                oper.getStatusRus()
                }, ++row);            
        }
        if (row > FIX_ROW_COUNT) {
            row = FIX_ROW_COUNT;
        }
        tableReports.setPageLength(row);
        labelNoData.setCaption(row > 0 ? "": LABEL_NO_DATA);
    }
    
    private void clearTable() {
        tableReports.removeAllItems();
        tableReports.setPageLength(0);
        labelNoData.setCaption(LABEL_PRESS_SHOW_CAPTION);
    }
    
    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        clearTable();
/*        //подчитаем ссылку на договор
        ExternalResource contractResource = null;
        String contractUrl = getUI().getAppProp().getContractUrl().trim();
        if (!contractUrl.isEmpty()) {
            contractResource = new ExternalResource(contractUrl);
        }
        buttonContract.setResource(contractResource);*/
    }
}
