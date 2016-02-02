package ru.baccasoft.eatster.ui.window;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.ActionPanel;
import ru.baccasoft.eatster.ui.event.ReportModelUpdate_Event;
import ru.baccasoft.utils.logging.Logger;

public class ReportStatusWindow extends Window {

    private static final Logger LOG = Logger.getLogger(ActionPanel.class);
    private static final long serialVersionUID = 1L;

    private class Fields {

        private static final int WIDTH_FIELD = 11;
        ComboBox status = new ComboBox();

        public Fields() {
            status.setWidth(WIDTH_FIELD, Unit.CM);
            comboboxForReportStatus(status);
            status.setNullSelectionAllowed(false);
        }

        private void comboboxForReportStatus(ComboBox comboBox) {
            comboBox.removeAllItems();
            for (ReportModel.ReportStatus item : ReportModel.getStatusList()) {
                comboBox.addItem(item.getId());
                comboBox.setItemCaption(item.getId(), item.getName());
            }
        }

    }
    private final Fields fields = new Fields();
    private ReportModel reportModel = null;
    private BeanFieldGroup<ReportModel> fieldBindings = null;

    public ReportStatusWindow() {
        super();
        buildLayout();
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component, String caption) {
        Label lab = new Label(caption);
        grid.addComponent(lab, col - 1, row);
        grid.setComponentAlignment(lab, Alignment.TOP_LEFT);
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    private void buildLayout() {
        setCaption("Изменить статус платежа");
        //
        center();
        setSizeUndefined();
        //
        GridLayout gridFields = new GridLayout(2, 1);
        gridFields.setSpacing(true);
        gridFields.setMargin(true);
        gridFields.setSizeUndefined();
        addToGrid(gridFields, 1, 0, fields.status, "на");
        //
        GridLayout gridButtons = new GridLayout(2, 1);
        gridButtons.setSizeFull();
        VerticalLayout content = new VerticalLayout(gridFields, gridButtons);
        setContent(content);
        //
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        //
        Button confirmButton = new Button("Подтвердить");
        confirmButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (commit()) {
                    getUI().fire(new ReportModelUpdate_Event(reportModel));
                    close(); // Close the sub-window
                }
            }
        });
        cancelButton.setSizeFull();
        confirmButton.setSizeFull();
        gridButtons.addComponent(cancelButton, 0, 0);
        gridButtons.addComponent(confirmButton, 1, 0);
        // Disable the close button
        setClosable(false);
    }

    public void setReportModel(ReportModel reportModel) {
        this.reportModel = reportModel;
        fieldBindings = BeanFieldGroup.bindFieldsBuffered(reportModel, fields);
        
    }
    public boolean commit() {
        try {
            fieldBindings.commit();
            fields.status.setComponentError(null);
            if (reportModel.getStatus() == null) {
                String msg = "Укажите статус.";
                Notification.show(msg, Notification.Type.WARNING_MESSAGE);
                fields.status.setComponentError(new UserError(msg) );
                return false;
            }
            return true;
        } catch (FieldGroup.CommitException e) {
            LOG.debug("Error on commit: " + e.getMessage());
            Notification.show("Ошибка заполнения данных", Notification.Type.WARNING_MESSAGE);
            return false;
        }
    }

}
