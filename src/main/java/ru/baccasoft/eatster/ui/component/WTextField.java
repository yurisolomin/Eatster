package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class WTextField extends TextField {
    
    private static final long serialVersionUID = 1L;

    public WTextField() {
        super();
        super.setSizeFull();
        super.addStyleName("wtextfield");
    }

    
    public WTextField(String caption) {
        super(caption);
        super.setSizeFull();
//        super.addStyleName(ValoTheme.TEXTFIELD_HUGE);
//        super.setHeight("1.5em");
//        super.setWidth("100%");
        super.addStyleName("wtextfield");
//        setSizeUndefined();
        //super.addStyleName("myresponsive");
//        super.setResponsive(true);
        //super.setHeight("100px");
        
    }
    
}
