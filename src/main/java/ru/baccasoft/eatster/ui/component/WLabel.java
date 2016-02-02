package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class WLabel extends Label {

    private static final long serialVersionUID = 1L;

    public WLabel(String content) {
        super(content);
//        super.setHeight("100%");
//        super.setWidth("100%");
//        addStyleName(ValoTheme.LABEL_LARGE);
        setSizeFull();
//        setSizeUndefined();
        super.addStyleName("wlabel");
//        super.setResponsive(true);
    }
    
}
