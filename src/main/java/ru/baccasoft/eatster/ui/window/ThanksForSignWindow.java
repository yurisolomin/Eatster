package ru.baccasoft.eatster.ui.window;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ThanksForSignWindow extends Window {

    private static final float WINDOW_WIDTH = 12;
    private static final float WINDOW_HIGHT = 7;
    private static final long serialVersionUID = 3522773723321691739L;
    private Button buttonOk;
    
    public ThanksForSignWindow(Button.ClickListener listener) {
        super("");
        buildLayout(listener);
    }
    
    private void buildLayout(Button.ClickListener listener) {
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        setContent(subContent);
        subContent.setSizeFull();

        VerticalLayout topContent = new VerticalLayout();
        topContent.addComponent(new Label(""));
        Label caption = new Label("Благодарим за регистрацию");
        caption.setWidthUndefined();
        topContent.addComponent(caption);
        topContent.setComponentAlignment(caption, Alignment.TOP_CENTER);
        Label text1 = new Label("В ближайшее время на указанный Вами e-mail");
        Label text2 = new Label("будет выслан пароль для входа в систему");
        text1.setWidthUndefined();
        text2.setWidthUndefined();
        topContent.addComponent(new Label(""));
        topContent.addComponent(text1);
        topContent.addComponent(text2);
        topContent.setComponentAlignment(text1, Alignment.TOP_CENTER);
        topContent.setComponentAlignment(text2, Alignment.TOP_CENTER);
        subContent.addComponent(topContent);
        buttonOk = new Button("OK",listener);
        subContent.addComponent(buttonOk);
        subContent.setComponentAlignment(buttonOk, Alignment.BOTTOM_CENTER);
        buttonOk.setWidth(WINDOW_WIDTH-1,Unit.CM);
        
/*        
        //
        VerticalLayout btmContent = new VerticalLayout();
        btmContent.setSizeFull();
        Button buttonOk = new Button("OK");
        btmContent.addComponent(buttonOk);
        btmContent.setComponentAlignment(buttonOk, Alignment.BOTTOM_CENTER);
        buttonOk.setSizeFull();
        subContent.addComponent(btmContent);*/
        //
        center();
        setModal(true);
        setHeight(WINDOW_HIGHT, Unit.CM);
        setWidth(WINDOW_WIDTH,Unit.CM);

    }

    public Button getButtonOk() {
        return buttonOk;
    }
    
}
