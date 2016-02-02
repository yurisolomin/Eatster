package ru.baccasoft.eatster.ui.window;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WarningWindow extends Window implements Button.ClickListener {
    
    private static final long serialVersionUID = 1L;

    private Button buttonOk;
    
    public WarningWindow(String caption,String message, int heightRatio, int widthRatio) {
        super("");
        buildLayout(caption,message,heightRatio,widthRatio);
    }
    
    private void buildLayout(String caption,String message, int heightRatio,int widthRatio) {
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        setContent(subContent);
        subContent.setSizeFull();

        VerticalLayout topContent = new VerticalLayout();
        Label captionLabel = new Label(caption);
        captionLabel.setWidthUndefined();
        topContent.addComponent(captionLabel);
        topContent.setComponentAlignment(captionLabel, Alignment.TOP_CENTER);
        Label messageLabel = new Label(message);
        topContent.addComponent(new Label(""));
        topContent.addComponent(messageLabel);
        topContent.setComponentAlignment(messageLabel, Alignment.MIDDLE_CENTER);
        topContent.setMargin(new MarginInfo(true, true, true, true));
        topContent.setSizeFull();
        subContent.addComponent(topContent);
        buttonOk = new Button("OK",this);
        subContent.addComponent(buttonOk);
        subContent.setComponentAlignment(buttonOk, Alignment.BOTTOM_CENTER);
        buttonOk.setWidth(50,Unit.PERCENTAGE);
        
        center();
        setModal(true);
        //setClosable(false);
        //setResizable(false);
        setHeight(heightRatio, Unit.PERCENTAGE);
        setWidth(widthRatio, Unit.PERCENTAGE);

    }

    public Button getButtonOk() {
        return buttonOk;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == buttonOk) {
            close();
        }
    }
    
}
