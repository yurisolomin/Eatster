package ru.baccasoft.eatster.ui.window;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SupportInfoWindow extends Window implements Button.ClickListener {

    private static final float WINDOW_WIDTH = 16;
    private static final float WINDOW_HIGHT = 8;
    private static final long serialVersionUID = 4021234713806620352L;
    private Button buttonOk;
    
    public SupportInfoWindow(String supportPhone,String supportEmail) {
        super("");
        buildLayout(supportPhone,supportEmail);
    }
    
    private void buildLayout(String supportPhone,String supportEmail) {
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        setContent(subContent);
        subContent.setSizeFull();

        VerticalLayout topContent = new VerticalLayout();
        topContent.addComponent(new Label(""));
        Label caption = new Label("Восстановление пароля");
        caption.setWidthUndefined();
        topContent.addComponent(new Label(""));
        topContent.addComponent(caption);
        topContent.setComponentAlignment(caption, Alignment.TOP_CENTER);
//        Label text1 = new Label("Для восстановления пароля отправьте запрос на почту "+supportEmail);
//        Label text1 = new Label("Для восстановление пароля вам необходимо обратиться в службу поддержки, телефон: "+supportPhone+", e-mail: "+supportEmail);
        Label text1 = new Label("Для восстановление пароля вам необходимо обратиться в службу поддержки");
        Label text2 = new Label("Телефон: "+supportPhone);
        Label text3 = new Label("E-mail: "+supportEmail);
        text1.setSizeUndefined();
//        text2.setWidthUndefined();
        topContent.addComponent(new Label(""));
        topContent.addComponent(text1);
        topContent.addComponent(text2);
        topContent.addComponent(text3);
        topContent.setComponentAlignment(text1, Alignment.TOP_LEFT);
        //topContent.setComponentAlignment(text2, Alignment.TOP_LEFT);
        //topContent.setComponentAlignment(text3, Alignment.TOP_LEFT);
        subContent.addComponent(topContent);
        buttonOk = new Button("OK",this);
        subContent.addComponent(buttonOk);
        subContent.setComponentAlignment(buttonOk, Alignment.BOTTOM_CENTER);
        buttonOk.setWidth(WINDOW_WIDTH-1,Unit.CM);
        
        center();
        setModal(true);
        setHeight(WINDOW_HIGHT, Unit.CM);
        setWidth(WINDOW_WIDTH,Unit.CM);

    }

    public Button getButtonOk() {
        return buttonOk;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        close();
    }
    
}
