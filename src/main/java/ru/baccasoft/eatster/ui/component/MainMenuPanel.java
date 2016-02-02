package ru.baccasoft.eatster.ui.component;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;
import java.util.ArrayList;
import java.util.List;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.Logout_Event;

public class MainMenuPanel extends HorizontalLayout implements Button.ClickListener {
    
    private static final long serialVersionUID = 1L;
//    Button btnRestaurant = new Button("Управление ресторанами",this);
//    Button btnModeration = new Button("Модерация",this);
    
    private static final int WIDTH_BUTTON = 7;
    private class MenuButton {
        final Button button;
        final Component component;
        public MenuButton(Button button, Component component) {
            this.button = button;
            this.component = component;
        }
    }
    
    private final List<MenuButton> menuButtons = new ArrayList();
    Button btnLogout = new Button("Выход", this);
    private final GridLayout grid = new GridLayout(1,1);

    public MainMenuPanel() {
        super();
        super.setWidth("100%");
        super.setSpacing(true);
        super.addComponent(grid);
        grid.setSpacing(true);
        grid.setWidth("100%");
        //
        btnLogout.setStyleName(BaseTheme.BUTTON_LINK);
        btnLogout.setSizeUndefined();
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnLogout) {
           getUI().fire(new Logout_Event());
           return;
        }
        for(MenuButton menuButton: menuButtons) {
            boolean visible = (event.getButton() == menuButton.button);
            if (menuButton.component != null) {
                menuButton.component.setVisible(visible);
            }
        }
    }
    
    public Button addButton(String caption, Component component) {
        Button button = new Button(caption,this);
        button.setWidthUndefined();
        button.setStyleName(BaseTheme.BUTTON_LINK);
        grid.addComponent(button,grid.getColumns()-1,0);
        grid.setColumns( grid.getColumns() + 1);
        menuButtons.add( new MenuButton(button,component) );
        return button;
    }
    
    public void addButtonLogout() {
        grid.addComponent(btnLogout,grid.getColumns()-1,0);
        grid.setColumnExpandRatio(grid.getColumns()-1,grid.getColumns());
        grid.setComponentAlignment(btnLogout, Alignment.TOP_RIGHT);
    }
    
    public void setVisibleButtons(boolean visible) {
        for(MenuButton menuButton: menuButtons) {
            menuButton.button.setVisible(visible);
        }
        
    }
    
}
