package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.Button;

public class WButton extends Button {

    private static final long serialVersionUID = 1L;

    public WButton(String caption, ClickListener listener) {
        super(caption, listener);
        super.setSizeFull();
        super.addStyleName("wbutton");
    }

}
