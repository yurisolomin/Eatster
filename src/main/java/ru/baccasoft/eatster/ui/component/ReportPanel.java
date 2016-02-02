package ru.baccasoft.eatster.ui.component;

import ru.baccasoft.eatster.ui.window.ReportStatusWindow;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Calendar;
import java.util.List;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationReportModel;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.eatster.service.OperationReportService;
import ru.baccasoft.eatster.service.ReportService;
import ru.baccasoft.eatster.ui.AppUI;

public class ReportPanel extends VerticalLayout implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    DateField fieldRepMonth = new DateField();
    DateField fieldCurMonth = new DateField();
    private final Table tableCurMonth = new Table();
    private final Table tableRepMonth = new Table();
    private final Button buttonRepMonthExportToExcel = new Button("Выгрузить в XLS", this);
    private final Button buttonCurMonthExportToExcel = new Button("Выгрузить в XLS", this);
    private final Button buttonRepMonthCalc = new Button("Рассчитать", this);
    private final OperationReportService operationReportService;
    private final ReportService reportService;
    private final Button buttonShowCurMonth = new Button("Показать", this);
    private final Button buttonShowRepMonth = new Button("Показать", this);
    private final static String LABEL_NO_DATA = "НЕТ ДАННЫХ.";
    private final static String CHANGE_STATUS_CAPTION = "Изменить статус";
    private final Label labelCurMonthNoData = new Label();
    private final Label labelRepMonthNoData = new Label();
    private final static int FIX_ROW_COUNT = 10;
    private final static String titleForButtonColumn = "";

    public ReportPanel(OperationReportService operationReportService,ReportService reportService) {
        this.operationReportService = operationReportService;
        this.reportService = reportService;
        buildLayout();
    }

    private void buildLayout() {
        fieldRepMonth.setDateFormat("MM-yyyy");
        fieldRepMonth.setResolution(Resolution.MONTH);
        fieldRepMonth.setValue(DateAsString.getInstance().curDate());
        fieldCurMonth.setDateFormat("MM-yyyy");
        fieldCurMonth.setResolution(Resolution.MONTH);
        fieldCurMonth.setValue(DateAsString.getInstance().curDate());
        fieldCurMonth.setReadOnly(true);
        //
        GridLayout gridReport = new GridLayout(4, 1);
        gridReport.setSpacing(true);
        gridReport.setMargin(true);
        gridReport.setWidth("100%");
        //
        Label label = new Label("Выберите месяц отчета");
        label.setSizeUndefined();
        gridReport.addComponent(label, 0, 0);
        gridReport.addComponent(fieldRepMonth, 1, 0);
        gridReport.addComponent(buttonShowRepMonth, 2, 0);
        HorizontalLayout layout = new HorizontalLayout(buttonRepMonthCalc,buttonRepMonthExportToExcel);
        layout.setSpacing(true);
//        gridReport.addComponent(buttonRepMonthExportToExcel, 3, 0);
        gridReport.addComponent(layout, 3, 0);
        gridReport.setColumnExpandRatio(3,3f);
//        gridReport.setComponentAlignment(buttonRepMonthExportToExcel, Alignment.TOP_RIGHT);
        gridReport.setComponentAlignment(layout, Alignment.TOP_RIGHT);
        //
        tableRepMonth.addContainerProperty("Ресторан", String.class, null);
        tableRepMonth.addContainerProperty("Баллов начислено", Integer.class, null);
        tableRepMonth.addContainerProperty("Баллов списано", Integer.class, null);
        tableRepMonth.addContainerProperty("Итого", Integer.class, null);
        tableRepMonth.addContainerProperty("Статус платежа", String.class, null);
        tableRepMonth.addContainerProperty(titleForButtonColumn, Button.class, null);
        tableRepMonth.setSizeFull();
        tableRepMonth.setColumnCollapsingAllowed(false);
        tableRepMonth.setColumnReorderingAllowed(true);
//        tableRepMonth.setColumnCollapsible(titleForButtonColumn, false);
        //
        tableCurMonth.addContainerProperty("Ресторан", String.class, null);
        tableCurMonth.addContainerProperty("Баллов начислено", Integer.class, null);
        tableCurMonth.addContainerProperty("Баллов списано", Integer.class, null);
        tableCurMonth.addContainerProperty("Итого", Integer.class, null);
        tableCurMonth.setSizeFull();
        tableCurMonth.setColumnCollapsingAllowed(false);
        tableCurMonth.setColumnReorderingAllowed(true);
        //
        VerticalLayout reportPanel = new VerticalLayout(gridReport,tableRepMonth,labelRepMonthNoData);
        reportPanel.setSizeFull();
        //
        GridLayout gridCurMonth = new GridLayout(4, 1);
        gridCurMonth.setSpacing(true);
        gridCurMonth.setMargin(true);
        gridCurMonth.setWidth("100%");
        //
        label = new Label("Текущий месяц");
        label.setSizeUndefined();
        gridCurMonth.addComponent(label, 0, 0);
        gridCurMonth.addComponent(fieldCurMonth, 1, 0);
        gridCurMonth.addComponent(buttonShowCurMonth, 2, 0);
        gridCurMonth.addComponent(buttonCurMonthExportToExcel, 3, 0);
        gridCurMonth.setColumnExpandRatio(3,3f);
        gridCurMonth.setComponentAlignment(buttonCurMonthExportToExcel, Alignment.TOP_RIGHT);
        //
        /*
        HorizontalLayout layout = new HorizontalLayout(buttonShowCurMonth,buttonCurMonthExportToExcel);
        layout.setWidth("100%");
        layout.setComponentAlignment(buttonShowCurMonth, Alignment.TOP_LEFT);
        layout.setComponentAlignment(buttonCurMonthExportToExcel, Alignment.TOP_RIGHT);
        */
        VerticalLayout curmonthPanel = new VerticalLayout(gridCurMonth,tableCurMonth,labelCurMonthNoData);
        curmonthPanel.setSizeFull();
        //
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addTab(curmonthPanel,"Текущий месяц");
        tabSheet.addTab(reportPanel,"Отчеты");
        addComponent(tabSheet);
        
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void showCurrentMonth() {
        List<OperationReportModel> list = operationReportService.getByMonth(getCurrentYear(),getCurrentMonth());
        tableCurMonth.removeAllItems();
        int row = 0;
        for(OperationReportModel oper: list) {
            tableCurMonth.addItem(new Object[]{
                oper.getRestaurantName(),
                oper.getScoresTotal(),
                oper.getScoresSpent(),
                oper.getScoresBalance()
                }, ++row);            
        }
        if (row > FIX_ROW_COUNT) {
            row = FIX_ROW_COUNT;
        }
        tableCurMonth.setPageLength(row);
        labelCurMonthNoData.setCaption(row > 0 ? "": LABEL_NO_DATA);
    }
    
    private int getReportYear() {
        java.util.Date date= fieldRepMonth.getValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);        
    }
    
    private int getReportMonth() {
        java.util.Date date= fieldRepMonth.getValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    private int getCurrentYear() {
        java.util.Date date= fieldCurMonth.getValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);        
    }
    
    private int getCurrentMonth() {
        java.util.Date date= fieldCurMonth.getValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public void showReportMonth() {
        int iMonth = getReportMonth();
        int iYear = getReportYear();
        List<ReportModel> list = reportService.findByMonth(iYear, iMonth);
        tableRepMonth.removeAllItems();
        int row = 0;
        for(ReportModel oper: list) {
            Button buttonChangeStatus = new Button(CHANGE_STATUS_CAPTION,this);
            buttonChangeStatus.setData(oper);
            tableRepMonth.addItem(new Object[]{
                oper.getRestaurantName(),
                oper.getScoresTotal(),
                oper.getScoresSpent(),
                oper.getScoresBalance(),
                oper.getStatusRus(),
                buttonChangeStatus
                }, ++row);            
        }
        if (row > FIX_ROW_COUNT) {
            row = FIX_ROW_COUNT;
        }
        tableRepMonth.setPageLength(row);
        labelRepMonthNoData.setCaption(row > 0 ? "": LABEL_NO_DATA);
    }
    
    private void changeStatus(ReportModel reportModel) {
        ReportStatusWindow reportStatusWindow = new ReportStatusWindow();
        reportStatusWindow.setReportModel(reportModel);
        getUI().addWindow(reportStatusWindow);
    }
            
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getCaption().equals(CHANGE_STATUS_CAPTION)) {
            ReportModel reportModel = (ReportModel)event.getButton().getData();
            changeStatus(reportModel);
        }
        if (event.getButton() == buttonRepMonthExportToExcel) {
            //чтобы не экспортировать колонку с кнопкой
            tableRepMonth.setColumnCollapsingAllowed(true);
            tableRepMonth.setColumnCollapsed(titleForButtonColumn, true);
            ExcelExport excelExport;
            try {
                excelExport = new ExcelExport(tableRepMonth);
                excelExport.excludeCollapsedColumns();
                excelExport.setReportTitle("Отчет");
                excelExport.export();
            } finally {
                tableRepMonth.setColumnCollapsed(titleForButtonColumn, false);
                tableRepMonth.setColumnCollapsingAllowed(false);
            }
        }
        if (event.getButton() == buttonRepMonthCalc) {
            int year = getReportYear();
            int month = getReportMonth();
            if (!reportService.canCalculate(year,month)) {
                Notification.show("Не могу пересчитать месяц! Обнаружены записи со статусом '"+ReportModel.STAT_PAID+"'.", Notification.Type.WARNING_MESSAGE);
                return;
            }
            reportService.calculate(year, month);
/*            //проставимпредыдущий месяц в не оплачено
            --month;
            if (month < 1) {
                month = 12;
                --year;
            }
            reportService.setAllFormedToNotPaid(year, month);*/
            Notification.show("РАСЧЕТ ПРОШЕЛ УСПЕШНО.", Notification.Type.HUMANIZED_MESSAGE);
            showReportMonth();
        }
        if (event.getButton() == buttonShowCurMonth) {
            showCurrentMonth();
        }
        if (event.getButton() == buttonShowRepMonth) {
            showReportMonth();
        }
        if (event.getButton() == buttonCurMonthExportToExcel) {
            ExcelExport excelExport;
            excelExport = new ExcelExport(tableCurMonth);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Текущий месяц");
            excelExport.export();
        }
    }
    
}
