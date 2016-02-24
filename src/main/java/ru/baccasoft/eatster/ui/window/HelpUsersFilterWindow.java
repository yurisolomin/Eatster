package ru.baccasoft.eatster.ui.window;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class HelpUsersFilterWindow extends Window implements Button.ClickListener {

    private static final long serialVersionUID = 788093768207033060L;

    private Button buttonOk;
    
    public HelpUsersFilterWindow() {
        super("");
        buildLayout();
    }
    
    private void addToGrid(GridLayout grid, int row, String filterValue, String caption) {
        Label lab1 = new Label(filterValue);
        lab1.setSizeUndefined();
        Label lab2 = new Label(caption);
        lab2.setSizeUndefined();
        grid.addComponent(lab1, 0, row);
        grid.setComponentAlignment(lab1, Alignment.TOP_LEFT);
        grid.addComponent(lab2, 1, row);
        grid.setComponentAlignment(lab2, Alignment.TOP_LEFT);
    }
    
    private void buildLayout() {
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        setContent(subContent);
        subContent.setSizeUndefined();

        Label captionLabel = new Label("Примеры использования фильтра");
        captionLabel.setWidthUndefined();
        subContent.addComponent(captionLabel);
        subContent.setComponentAlignment(captionLabel, Alignment.TOP_CENTER);
        GridLayout grid = new GridLayout(2,4);
        grid.setColumnExpandRatio(1,2);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setSizeUndefined();
        addToGrid(grid, 0, "123", "По iD пользователя равному 123");
        addToGrid(grid, 1, "Иван", "По имени пользователя равному Иван");
        addToGrid(grid, 2, "Иван%", "По имени пользователя похожему на Иван (Иван, Иванов, Иванович)");
        addToGrid(grid, 3, "%Иван%", "По имени пользователя содержащему Иван (Петров Иван, Иван, Иванович)");
        subContent.addComponent(grid);
        //
        buttonOk = new Button("OK",this);
        subContent.addComponent(buttonOk);
        subContent.setComponentAlignment(buttonOk, Alignment.TOP_CENTER);
        buttonOk.setWidth(50,Unit.PERCENTAGE);
        
        center();
        setModal(true);
        setSizeUndefined();
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
