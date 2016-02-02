package ru.baccasoft.eatster.ui.window;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.baccasoft.eatster.image.ImageValidator;

public class ImageRestrictionsWindow extends Window implements Button.ClickListener {
    
    private static final long serialVersionUID = 1L;

    private Button buttonOk;
    
    public ImageRestrictionsWindow(ImageValidator imageValidator) {
        super("");
        buildLayout(imageValidator);
    }
    
    private void buildLayout(ImageValidator imageValidator) {
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        setContent(subContent);
        subContent.setSizeFull();

        VerticalLayout topContent = new VerticalLayout();
        Label captionLabel = new Label("Размеры изображения");
        captionLabel.setWidthUndefined();
        topContent.addComponent(captionLabel);
        topContent.setComponentAlignment(captionLabel, Alignment.TOP_CENTER);
        Label minLabel = new Label("Минимальный  размер (ш/в): "+imageValidator.getWidthMin()+"x"+imageValidator.getHeightMin());
        Label maxLabel = new Label("Максимальный размер (ш/в): "+imageValidator.getWidthMax()+"x"+imageValidator.getHeightMax());
        topContent.addComponent(new Label(""));
        topContent.addComponent(minLabel);
        topContent.addComponent(maxLabel);
        topContent.setComponentAlignment(minLabel, Alignment.MIDDLE_LEFT);
        topContent.setComponentAlignment(maxLabel, Alignment.MIDDLE_LEFT);
        topContent.setSpacing(true);
        topContent.setMargin(new MarginInfo(true, true, false, true));
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
        setHeight(6, Unit.CM);
        setWidth(14, Unit.CM);
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
