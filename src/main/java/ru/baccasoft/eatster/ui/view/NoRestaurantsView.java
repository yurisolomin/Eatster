package ru.baccasoft.eatster.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import ru.baccasoft.eatster.ui.component.RestaurantHeaderPanel;
import ru.baccasoft.eatster.ui.component.MainMenuPanel;

@UIScope
@SpringView(name = NoRestaurantsView.NAME)
public class NoRestaurantsView extends VerticalLayout implements View {
    private static final long serialVersionUID = -1727999837123206194L;
    public static final String NAME = "norestaurants";
    private RestaurantHeaderPanel headerPanel;
    private MainMenuPanel mainMenu;

    public NoRestaurantsView() {
    }
    
    @PostConstruct
    void init() {
        setSpacing(true);
        mainMenu = new MainMenuPanel();
        mainMenu.addSysButtons();
        addComponent(mainMenu);
        headerPanel = new RestaurantHeaderPanel();
        addComponent(headerPanel);
        Label label = new Label("В системе нет зарегистрированных ресторанов.");
        label.addStyleName(ValoTheme.LABEL_LARGE);
        addComponent(label);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        mainMenu.setVisibleButtons(false);
    }
    
}