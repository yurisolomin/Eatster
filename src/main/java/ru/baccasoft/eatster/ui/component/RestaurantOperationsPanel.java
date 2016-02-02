package ru.baccasoft.eatster.ui.component;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.OperationTotalModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.ShowAllOperations_Event;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantOperationsPanel extends VerticalLayout implements Button.ClickListener {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RestaurantOperationsPanel.class);

    private class Fields {

        private static final int WIDTH_FIELD = 4;
        TextField operCount = new TextField();
        TextField operSum = new TextField();
        TextField scoresTotal = new TextField();    //начислено
        TextField scoresSpent = new TextField();    //списано
        TextField scoresBalance = new TextField();  //баланс

        public Fields() {
            operCount.setWidth(WIDTH_FIELD, Unit.CM);
            operCount.setNullRepresentation("");
            operSum.setWidth(WIDTH_FIELD, Unit.CM);
            operSum.setNullRepresentation("");
            scoresTotal.setWidth(WIDTH_FIELD, Unit.CM);
            scoresTotal.setNullRepresentation("");
            scoresSpent.setWidth(WIDTH_FIELD, Unit.CM);
            scoresSpent.setNullRepresentation("");
            scoresBalance.setWidth(WIDTH_FIELD, Unit.CM);
            scoresBalance.setNullRepresentation("");
        }

    }
    private final Fields fields = new Fields();
    private BeanFieldGroup<OperationTotalModel> fieldBindings = null;
    private final Table operationsGrid = new Table();
    private final static String BUTTON_SHOW_CAPTION = "Показать";
    private final static String LABEL_PRESS_SHOW_CAPTION = "Нажмите кнопку '"+BUTTON_SHOW_CAPTION+"' для получения данных";
    private final static String LABEL_NO_DATA = "НЕТ ДАННЫХ.";
    private final Button buttonShowByRestaurant = new Button(BUTTON_SHOW_CAPTION, this);
    private final Button buttonShowAll = new Button("Показать все", this);
    private final Button buttonExportToExcel = new Button("Выгрузить в XLS", this);
    private final Label labelNoData = new Label();
    private final float BUTTON_WIDTH = 4f;
    private final static int FIX_ROW_COUNT = 10;
    private long restaurantId;

    public RestaurantOperationsPanel() {
        buildLayout();
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component, String caption) {
        Label lab = new Label(caption);
        grid.addComponent(lab, col - 1, row);
        grid.setComponentAlignment(lab, Alignment.TOP_LEFT);
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    private void buildLayout() {
        setSpacing(true);
        HorizontalLayout buttonsLayout = new HorizontalLayout(buttonShowByRestaurant,buttonShowAll);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setMargin(new MarginInfo(true,true,false,true));
        addComponent(buttonsLayout);
        GridLayout grid = new GridLayout(6, 2);
        grid.setSpacing(true);
        grid.setMargin(new MarginInfo(false,true,false,true));
  
        grid.setSizeFull();
        addToGrid(grid, 1, 0, fields.operCount, "Всего операций");
        addToGrid(grid, 1, 1, fields.operSum, "Оборот");
        addToGrid(grid, 3, 0, fields.scoresTotal, "Начислено баллов");
        addToGrid(grid, 3, 1, fields.scoresSpent, "Списано баллов");
        addToGrid(grid, 5, 0, fields.scoresBalance, "Баланс");
        grid.addComponent(buttonExportToExcel, 5, 1);
        //buttonExportToExcel.setEnabled(false);
        addComponent(grid);
        
        operationsGrid.addContainerProperty("Пользователь", String.class, null);
        operationsGrid.addContainerProperty("Официант", String.class, null);
        operationsGrid.addContainerProperty("Дата и время", String.class, null);
        operationsGrid.addContainerProperty("Чек", Integer.class, null);
        operationsGrid.addContainerProperty("Баллы", Integer.class, null);
        operationsGrid.addContainerProperty("Примечание", String.class, null);
        operationsGrid.setSizeFull();
        operationsGrid.setColumnCollapsingAllowed(true);
        operationsGrid.setColumnReorderingAllowed(true);
        addComponent(operationsGrid);
        addComponent(labelNoData);
        //
        buttonShowByRestaurant.setWidth(BUTTON_WIDTH,Unit.CM);
        buttonShowAll.setWidth(BUTTON_WIDTH,Unit.CM);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void bindFieldsBuffered(OperationTotalModel operationTotal, List<OperationModel> list) {
        boolean isAdmin = getUI().getPartnerModel().isAdmin();
        buttonShowAll.setVisible(isAdmin);
        fieldBindings = BeanFieldGroup.bindFieldsBuffered(operationTotal, fields);
        fieldBindings.setReadOnly(true);
        operationsGrid.removeAllItems();
        int row = 0;
        for(OperationModel oper: list) {
            operationsGrid.addItem(new Object[]{
                oper.getUserName(),
                oper.getWaiterName(),
                oper.getShortDateTime(),
                oper.getCheckSum(),
                oper.getScore(),
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
        if (event.getButton() == buttonShowAll) {
            showAll();
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
    
    public void showAll() {
        OperationTotalModel operationTotal = getUI().getOperationTotalService().getAll();
        List<OperationModel> operationList = getUI().getOperationService().findAll();
        bindFieldsBuffered(operationTotal,operationList);
    }
    
    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        clearTable();
        }
}
