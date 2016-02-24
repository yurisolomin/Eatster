package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.Label;

public class WLabel extends Label {

    private static final long serialVersionUID = -7283112303388940995L;

    public WLabel(String content) {
        super(content);
        super.setSizeFull();
        super.addStyleName("wlabel");
    }
    
}
