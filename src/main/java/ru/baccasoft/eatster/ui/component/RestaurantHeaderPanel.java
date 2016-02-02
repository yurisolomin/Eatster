package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.Logout_Event;
import ru.baccasoft.eatster.ui.event.RestaurantChange_Event;
import ru.baccasoft.eatster.ui.event.ShowAction_Event;
import ru.baccasoft.eatster.ui.event.ShowInformation_Event;
import ru.baccasoft.eatster.ui.event.ShowOperation_Event;
import ru.baccasoft.eatster.ui.event.ShowPartner_Event;
import ru.baccasoft.eatster.ui.event.ShowPhoto_Event;
import ru.baccasoft.eatster.ui.event.ShowWaiter_Event;
import ru.baccasoft.eatster.ui.scope.PartnerScope;

public class RestaurantHeaderPanel extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    
    private static final int WIDTH_FIELD = 11;
    private static final float WIDTH_BUTTON = 4.5f;
    private class Fields {
        private final ComboBox id = new EatsterIdComboBox(null,"");
        private final ComboBox status = new ComboBox();

        public Fields() {
            id.setWidth(WIDTH_FIELD, Unit.CM);
            status.setWidth(WIDTH_FIELD, Unit.CM);
        }
    }

    private final Label labRest = new Label("Выберите заведение");
    private final Label labStatus = new Label("Статус заведения");

    private final Button btnAction = new Button("Акции");
    private final Button btnPhoto = new Button("Фотографии");
    private final Button btnWaiter = new Button("Официанты");
    private final Button btnInformation = new Button("Информация");
    private final Button btnOperation = new Button("Операции / Оплата");
    private final Button btnPartner = new Button("Настройки");
    private Property.ValueChangeListener idValueChangeListener;
    //private final Button btnLogout = new Button("Log out");
    
    private final Fields fields = new Fields();
    private BeanFieldGroup<RestaurantModel> fieldBindings;
    
    public RestaurantHeaderPanel() {
        buildLayout();
    }
    
    public final void buildLayout() {
        setSpacing(true);
        //
//        Layout logoutLayout = buildLogoutLayout();
//        addComponent(logoutLayout);
        //
        Layout restaurantLayout = buildRestaurantLayout();
        Layout buttonLayout = buildButtonsLayout();
        HorizontalLayout layout = new HorizontalLayout(restaurantLayout, buttonLayout);
        layout.setSpacing(true);
        addComponent(layout);
    }

    public final Layout buildRestaurantLayout() {
        GridLayout grid = new GridLayout(2, 3);
        grid.setSpacing(true);
        grid.setSizeFull();
        addComponent(grid);
        //
        grid.addComponent(labRest, 0, 0);
        grid.setComponentAlignment(labRest, Alignment.TOP_LEFT);
        grid.addComponent(fields.id, 1, 0);
        grid.setComponentAlignment(fields.id, Alignment.TOP_LEFT);
        grid.addComponent(labStatus, 0, 1);
        grid.setComponentAlignment(labStatus, Alignment.TOP_LEFT);
        grid.addComponent(fields.status, 1, 1);
        grid.setComponentAlignment(fields.status, Alignment.TOP_LEFT);
        //labRest.setWidth("170");
        //labStatus.setWidth("170px");

        // реакция на смену ресторана
        idValueChangeListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String sRestaurantId = fields.id.getValue().toString();
                long restaurantId = Long.parseLong(sRestaurantId);
                getUI().fire(new RestaurantChange_Event(restaurantId));
            }
        };
        fields.id.addValueChangeListener(idValueChangeListener); 
        fields.id.setImmediate(true);
        //
        return grid;
    }

    private void addButtonToGrid(GridLayout grid, Button button, int col, int row) {
        grid.addComponent(button, col, row);
        grid.setComponentAlignment(button, Alignment.TOP_LEFT);
        button.setWidth(WIDTH_BUTTON, Unit.CM);
        button.setEnabled(false);
    }
    public final Layout buildButtonsLayout() {
        GridLayout grid = new GridLayout(3, 2);
        grid.setSpacing(true);
        grid.setSizeFull();
        addComponent(grid);
        //
        addButtonToGrid(grid,btnInformation,0,0);
        addButtonToGrid(grid,btnPhoto,1,0);
        addButtonToGrid(grid,btnWaiter,2,0);
        addButtonToGrid(grid,btnAction,0,1);
        addButtonToGrid(grid,btnOperation,1,1);
        addButtonToGrid(grid,btnPartner,2,1);
        fields.status.setEnabled(false);

        btnAction.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new ShowAction_Event());
//                getUI().fire(new SwitchView_Event(ActionView.NAME));
            }
        });

        btnInformation.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new ShowInformation_Event());
            }
        });
        
        btnPartner.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new ShowPartner_Event());
            }
        });
        
        btnOperation.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new ShowOperation_Event());
            }
        });
        
        btnWaiter.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new ShowWaiter_Event());
            }
        });
        
        btnPhoto.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new ShowPhoto_Event());
            }
        });
        
        return grid;
    }
/*
    public final Layout buildLogoutLayout() {
        VerticalLayout layout = new VerticalLayout(btnLogout);
        layout.setComponentAlignment(btnLogout, Alignment.TOP_RIGHT);

        btnLogout.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new Logout_Event());
            }
        });
        return layout;
    }
*/    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }
    
    public void refreshComboBox() {
        fields.id.removeValueChangeListener(idValueChangeListener);
        try {
            PartnerScope partnerScope = getUI().getPartnerScope();
            partnerScope.comboboxForRestaurant(fields.id);
            partnerScope.comboboxForRestaurantStatus(fields.status);
        } finally {
            fields.id.addValueChangeListener(idValueChangeListener);
        }
    }

    public void setStatusEnabled(boolean enabled) {
        fields.status.setEnabled(enabled);
    }

    public void setButtonsEnabled(boolean isInformation, boolean isAction, boolean isPartner, boolean isOperation, boolean isWaiter,boolean isPhoto) {
        btnAction.setEnabled(isAction);
        btnInformation.setEnabled(isInformation);
        btnPartner.setEnabled(isPartner);
        btnOperation.setEnabled(isOperation);
        btnWaiter.setEnabled(isWaiter);
        btnPhoto.setEnabled(isPhoto);
    }
    
    public void bindFieldsBuffered(RestaurantModel restaurantModel) {
        fieldBindings = BeanFieldGroup.bindFieldsBuffered(restaurantModel, fields);
        if (getUI().isAdminApp()) {
            fields.id.setEnabled(true);
        } else {
            fields.id.setEnabled(false);
        }
    }

    public void commit() throws FieldGroup.CommitException {
        fieldBindings.commit();
    }

    public String getStatusRus() {
        Object item = fields.status.getValue();
        return fields.status.getItemCaption(item);
    }
}
