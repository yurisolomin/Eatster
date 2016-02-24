package ru.baccasoft.eatster.ui.component;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.eatster.service.ReportService;
import ru.baccasoft.eatster.ui.AppUI;

public abstract class RestaurantReportAbstractPanel extends VerticalLayout implements Button.ClickListener {
    private static final long serialVersionUID = 5492524489443952160L;

    private final static int FIX_ROW_COUNT = 10;
    private final static String BUTTON_SHOW_CAPTION = "Показать";
    private final static String LABEL_PRESS_SHOW_CAPTION = "Нажмите кнопку '"+BUTTON_SHOW_CAPTION+"' для получения данных";
    private final static String LABEL_NO_DATA = "НЕТ ДАННЫХ.";
    private final float BUTTON_WIDTH = 4f;
    protected final Table tableReports = new Table();
    private final Button buttonExportToExcel = new Button("Выгрузить в XLS", this);
    private final Button buttonShow = new Button(BUTTON_SHOW_CAPTION, this);
    private final Button buttonContract = new Button("Договор", this);
    private final Label labelNoData = new Label();
    private long restaurantId;

    public RestaurantReportAbstractPanel() {
        buildLayout();
    }

    abstract int fillTable(List<ReportModel> list);

    private void buildLayout() {
        setMargin(new MarginInfo(true,false,false,false));
        //
        GridLayout gridReport = new GridLayout(2, 1);
        gridReport.setSpacing(true);
        gridReport.setWidth("100%");
        //
        HorizontalLayout layout = new HorizontalLayout(buttonContract,buttonExportToExcel);
        layout.setSpacing(true);
        gridReport.addComponent(buttonShow, 0, 0);
        gridReport.addComponent(layout, 1, 0);
        gridReport.setColumnExpandRatio(1,2f);
        gridReport.setComponentAlignment(layout, Alignment.TOP_RIGHT);
        //
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
        clearTable();
        ReportService reportService = getUI().getReportService();
        List<ReportModel> list = reportService.findByRestaurant(restaurantId);
        tableReports.removeAllItems();
        int rows = fillTable(list);
        if (rows > FIX_ROW_COUNT) {
            rows = FIX_ROW_COUNT;
        }
        tableReports.setPageLength(rows);
        labelNoData.setCaption(rows > 0 ? "": LABEL_NO_DATA);
    }
    
    private void clearTable() {
        tableReports.removeAllItems();
        tableReports.setPageLength(0);
        labelNoData.setCaption(LABEL_PRESS_SHOW_CAPTION);
    }
    
    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        clearTable();
    }
}
