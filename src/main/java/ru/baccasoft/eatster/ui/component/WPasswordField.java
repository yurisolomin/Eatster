
package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.PasswordField;

public class WPasswordField extends PasswordField {

    private static final long serialVersionUID = -3707287525332271977L;

    public WPasswordField() {
        super();
        super.setSizeFull();
        super.addStyleName("wpasswordfield");
    }

    public WPasswordField(String caption) {
        super(caption);
        super.setSizeFull();
        super.addStyleName("wpasswordfield");
    }
    
}
