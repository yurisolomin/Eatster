package ru.baccasoft.eatster.ui.component;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import ru.baccasoft.eatster.model.UserReportModel;
import ru.baccasoft.eatster.service.UserReportService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.window.HelpUsersFilterWindow;
import ru.baccasoft.eatster.ui.window.UserChangeScoresWindow;

public class UsersPanel extends VerticalLayout implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    private class Fields {
        private static final int WIDTH_FIELD = 6;
        TextField filter = new TextField();
        public Fields() {
            filter.setWidth(WIDTH_FIELD, Unit.CM);
            filter.setInputPrompt("iD или Пользователь");
        }

    }
    private final Fields fields = new Fields();
    private final Table usersGrid = new Table();
    private final Button buttonFilter = new Button("Отфильтровать", this);
    private final Button buttonHelp = new Button(null, this);//"Знак вопроса"
    private final Button buttonExportToExcel = new Button("Выгрузить в XLS", this);
    private final UserReportService userReportService;
    private final Label labelNoData = new Label("");
    private final int GRID_ROWS_SIZE = 10;
    private final String BUTTON_SCORES_COLUMN_TITLE = "";
    private final String BUTTON_SCORES_CAPTION = "Изменить баллы";

    public UsersPanel(UserReportService userReportService) {
        this.userReportService = userReportService;
        buildLayout();
    }

    private void buildLayout() {
        setSpacing(true);
        buttonHelp.setIcon(FontAwesome.QUESTION_CIRCLE);
        GridLayout grid = new GridLayout(4, 1);
        grid.setSpacing(true);
//        grid.setMargin(true);
        grid.setSizeFull();
        grid.addComponent(fields.filter,0,0);
        grid.addComponent(buttonFilter, 1, 0);
        grid.addComponent(buttonHelp, 2, 0);
        grid.addComponent(buttonExportToExcel, 3, 0);
        grid.setComponentAlignment(buttonExportToExcel, Alignment.TOP_RIGHT);
        grid.setColumnExpandRatio(2,4);
        addComponent(grid);

        usersGrid.addContainerProperty("iD", Long.class, null);
        usersGrid.addContainerProperty("Пользователь", String.class, null);
        usersGrid.addContainerProperty("Дата рождения", String.class, null);
        usersGrid.addContainerProperty("Пол", String.class, null);
        usersGrid.addContainerProperty("E-mail", String.class, null);
        usersGrid.addContainerProperty("Номер телефона", String.class, null);
        usersGrid.addContainerProperty("Дата регистрации", String.class, null);
        usersGrid.addContainerProperty("Рефералов", Integer.class, null);
        usersGrid.addContainerProperty("Доступно баллов", Integer.class, null);
        usersGrid.addContainerProperty(BUTTON_SCORES_COLUMN_TITLE, Button.class, null);
        usersGrid.addContainerProperty("Промо-код друга", String.class, null);
        usersGrid.addContainerProperty("Личный промо-код", String.class, null);
        usersGrid.addContainerProperty("Кол-во активаций бонуса", Integer.class, null);
        usersGrid.addContainerProperty("Остаток бонусных дней", Integer.class, null);
        usersGrid.addContainerProperty("Начислено баллов", Integer.class, null);
        usersGrid.addContainerProperty("Потрачено баллов", Integer.class, null);
        usersGrid.addContainerProperty("Количество операций", Integer.class, null);
        usersGrid.setSizeFull();
        usersGrid.setColumnCollapsingAllowed(true);
        usersGrid.setColumnReorderingAllowed(true);
        usersGrid.setPageLength(0);
        addComponent(usersGrid);
        addComponent(labelNoData);
        labelNoData.setSizeUndefined();
        labelNoData.setCaption("Нажмите кнопку "+buttonFilter.getCaption());
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    private void showList(List<UserReportModel> list) {
        usersGrid.removeAllItems();
        int row = 0;
        for(UserReportModel user: list) {
            Button buttonScores = new Button(BUTTON_SCORES_CAPTION,this);
            buttonScores.addStyleName("link");
            buttonScores.setData(user);
            usersGrid.addItem(new Object[]{
                user.getId(),
                user.getName(),
                user.getBirthday(),
                user.getGender(),
                user.getEmail(),
                user.getPhone(),
                user.getRegistrationDate(),
                user.getFriendsCount(),
                user.getScoresBalance(),
                buttonScores,
                user.getFriendPromocode(),
                user.getPromocode(),
                user.getFriendsBonusActivatedCount(),
                user.getBonusPeriodEstimate(),
                user.getScoresTotal(),
                user.getScoresSpent(),
                user.getOperCount()
                }, ++row);            
        }
        if (row > 0) {
            if (row > GRID_ROWS_SIZE) {
                usersGrid.setPageLength(GRID_ROWS_SIZE);
            } else {
                usersGrid.setPageLength(row);
            }
            labelNoData.setVisible(false);
        } else {
            usersGrid.setPageLength(0);
            labelNoData.setVisible(true);
        }
    }

    public void refresh() {
        String value = fields.filter.getValue();
        if (value == null) {
            value = "";
        }
        List<UserReportModel> list;
        try {
            Long longValue = Long.parseLong(value);
            list = userReportService.findByUserId(longValue);
            labelNoData.setCaption("Нет данных с iD="+longValue);
        } catch(Exception ex) {
            if (value.trim().equals("")) {
                list = userReportService.findAll();
                labelNoData.setCaption("В таблице пользователей нет данных");
            } else {
                list = userReportService.findByUserMask(value);
                labelNoData.setCaption("Нет данных по маске пользователя '"+value+"'");
            }
        }
        showList(list);
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == buttonFilter) {
            refresh();
        }
        if (event.getButton() == buttonExportToExcel) {
            //чтобы не экспортировать колонку с кнопкой
            usersGrid.setColumnCollapsed(BUTTON_SCORES_COLUMN_TITLE, true);
            ExcelExport excelExport;
            excelExport = new ExcelExport(usersGrid);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Пользователи");
            excelExport.export();
            usersGrid.setColumnCollapsed(BUTTON_SCORES_COLUMN_TITLE, false);
        }
        if (event.getButton() == buttonHelp) {
            HelpUsersFilterWindow window = new HelpUsersFilterWindow();
            getUI().addWindow(window);
        }
        String caption = event.getButton().getCaption();
        if (BUTTON_SCORES_CAPTION.equals(caption)) {
            UserReportModel user = (UserReportModel)event.getButton().getData();
            UserChangeScoresWindow window = new UserChangeScoresWindow(user.getId());
            getUI().addWindow(window);
            //changeStatus(reportModel);
        }
    }

}
