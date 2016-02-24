package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.VerticalLayout;

public abstract class MainMenuItemLayout extends VerticalLayout {

    public abstract void doRefresh();
    
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            doRefresh();
        }
    }
}
