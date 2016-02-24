package ru.baccasoft.eatster.ui.component;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.OperationTotalModel;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantOperationsPartnerPanel extends VerticalLayout implements Button.ClickListener {

    private static final long serialVersionUID = -3851727710839406195L;

    private class Fields {

        private static final float WIDTH_FIELD = 3.5f;
        TextField operCount = new TextField();
        TextField operSum = new TextField();
        TextField scoresTotal = new TextField();    //начислено
        TextField scoresSpent = new TextField();    //списано
        TextField commissionSum = new TextField();      //прибыль
        TextField calcPayOffBalance = new TextField();    //сальдо выплат

        public Fields() {
            operCount.setWidth(WIDTH_FIELD, Unit.CM);
            operCount.setNullRepresentation("");
            operSum.setWidth(WIDTH_FIELD, Unit.CM);
            operSum.setNullRepresentation("");
            scoresTotal.setWidth(WIDTH_FIELD, Unit.CM);
            scoresTotal.setNullRepresentation("");
            scoresSpent.setWidth(WIDTH_FIELD, Unit.CM);
            scoresSpent.setNullRepresentation("");
            commissionSum.setWidth(WIDTH_FIELD, Unit.CM);
            commissionSum.setNullRepresentation("");
            calcPayOffBalance.setWidth(WIDTH_FIELD, Unit.CM);
            calcPayOffBalance.setNullRepresentation("");
        }

    }
    private final Fields fields = new Fields();
    private BeanFieldGroup<OperationTotalModel> fieldBindings = null;
    private final Table operationsGrid = new Table();
    private final static String BUTTON_SHOW_CAPTION = "Показать";
    private final static String LABEL_PRESS_SHOW_CAPTION = "Нажмите кнопку '"+BUTTON_SHOW_CAPTION+"' для получения данных";
    private final static String LABEL_NO_DATA = "НЕТ ДАННЫХ.";
    private final Button buttonShowByRestaurant = new Button(BUTTON_SHOW_CAPTION, this);
    private final Button buttonExportToExcel = new Button("Выгрузить в XLS", this);
    private final Label labelNoData = new Label();
    private final float BUTTON_WIDTH = 4f;
    private final static int FIX_ROW_COUNT = 10;
    private long restaurantId = 0;

    public RestaurantOperationsPartnerPanel() {
        buildLayout();
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component, String caption, Alignment alignment) {
        Label lab = new Label(caption);
        lab.setWidthUndefined();
        grid.addComponent(lab, col - 1, row);
        grid.setComponentAlignment(lab, alignment);
        //
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, alignment);
    }

    private void buildLayout() {
        setSpacing(true);
        setMargin(new MarginInfo(true,false,false,false));
        //
        GridLayout grid = new GridLayout(6, 2);
        grid.setSpacing(true);
        //
        addToGrid(grid, 1, 0, fields.operCount, "Всего транзакций",Alignment.TOP_LEFT);
        addToGrid(grid, 1, 1, fields.operSum, "Оборот",Alignment.TOP_LEFT);
        addToGrid(grid, 3, 0, fields.scoresTotal, "Начислено баллов",Alignment.TOP_LEFT);
        addToGrid(grid, 3, 1, fields.scoresSpent, "Списано баллов",Alignment.TOP_LEFT);
        addToGrid(grid, 5, 0, fields.commissionSum, "Комиссия EatAction",Alignment.TOP_LEFT);
        addToGrid(grid, 5, 1, fields.calcPayOffBalance, "Сальдо выплат",Alignment.TOP_LEFT);
        addComponent(grid);
        //
        HorizontalLayout buttonsLayout = new HorizontalLayout(buttonShowByRestaurant,buttonExportToExcel);
        buttonsLayout.setSpacing(true);
        addComponent(buttonsLayout);
        //
        operationsGrid.addContainerProperty("Дата и время", String.class, null);
        operationsGrid.addContainerProperty("iD клиента", Long.class, null);
        operationsGrid.addContainerProperty("Официант", String.class, null);
        operationsGrid.addContainerProperty("Тип транзакции", String.class, null);
        operationsGrid.addContainerProperty("Сумма чека", Integer.class, null);
        operationsGrid.addContainerProperty("% Кэшбэка", Integer.class, null);
        operationsGrid.addContainerProperty("Списано баллов", Integer.class, null);
        operationsGrid.addContainerProperty("Начислено баллов", Integer.class, null);
        operationsGrid.addContainerProperty("Комиссия сервиса", Integer.class, null);
        operationsGrid.addContainerProperty("Примечание", String.class, null);
        operationsGrid.setSizeFull();
        operationsGrid.setColumnCollapsingAllowed(true);
        operationsGrid.setColumnReorderingAllowed(true);
        addComponent(operationsGrid);
        addComponent(labelNoData);
        //
        buttonShowByRestaurant.setWidth(BUTTON_WIDTH,Unit.CM);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void bindFieldsBuffered(OperationTotalModel operationTotal, List<OperationModel> list) {
        fieldBindings = BeanFieldGroup.bindFieldsBuffered(operationTotal, fields);
        fieldBindings.setReadOnly(true);
        operationsGrid.removeAllItems();
        int row = 0;
        for(OperationModel oper: list) {
            String transactType;
            if (oper.getDecScore() != 0) {
                transactType = "Списание";
            } else {
                transactType = "Начисление";
            }
            int cashback = oper.getCashbackBaseRate() + oper.getCashbackBonusRate();
            String operDateTime = oper.getOperDate()+" "+oper.getOperTime();
            operationsGrid.addItem(new Object[]{
                    operDateTime,
                    oper.getUserId(),
                    oper.getWaiterName(),
                    transactType,
                    oper.getCheckSum(),
                    cashback,
                    oper.getDecScore(),
                    oper.getAddScore(),
                    oper.getCalcCommissionSum(),
                    oper.getComment()
                    }, ++row);            
        }
        operationsGrid.setPageLength(10);
        if (row > FIX_ROW_COUNT) {
            row = FIX_ROW_COUNT;
        }
        operationsGrid.setPageLength(row);
        labelNoData.setCaption(row > 0 ? "": LABEL_NO_DATA);
    }

    private void clearTable() {
        //пустая форма
        OperationTotalModel operationTotal = new OperationTotalModel();
        List<OperationModel> operationList = new ArrayList();
        bindFieldsBuffered(operationTotal,operationList);
        labelNoData.setCaption(LABEL_PRESS_SHOW_CAPTION);
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == buttonShowByRestaurant) {
            showByRestaurant();
        }
        if (event.getButton() == buttonExportToExcel) {
            ExcelExport excelExport;
            excelExport = new ExcelExport(operationsGrid);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Операции");
            excelExport.export();
        }
    }

    public void showByRestaurant() {
        OperationTotalModel operationTotal = getUI().getOperationTotalService().getByRestaurant(restaurantId);
        List<OperationModel> operationList = getUI().getOperationService().findByRestaurant(restaurantId);
        bindFieldsBuffered(operationTotal,operationList);
    }
    
    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        clearTable();
    }

    public long getRestaurantId() {
        return restaurantId;
    }
    
}
