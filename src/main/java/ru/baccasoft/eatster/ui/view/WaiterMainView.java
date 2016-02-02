package ru.baccasoft.eatster.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.WButton;
import ru.baccasoft.eatster.ui.component.WLabel;
import ru.baccasoft.eatster.ui.event.Logout_Event;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = WaiterMainView.NAME)
public class WaiterMainView extends VerticalLayout implements View, Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(WaiterMainView.class);

    private static final long serialVersionUID = 1L;
    public static final String NAME = "wmain";
//    private static final float WIDTH_BUTTON = 4f;

    private final Button addScoreButton = new WButton("Начисление", this);;
    private final Button decScoreButton = new WButton("Списание", this);
    private final Button cancelButton = new WButton("Выход", this);

    @Autowired
    WaiterService waiterService;

    public WaiterMainView() {
    }
/*
    @PostConstruct
    void init() {
        setSizeFull();
        
        VerticalLayout scoreLayout = new VerticalLayout(new WLabel("")
                        ,addScoreButton,new WLabel(""),new WLabel(""),new WLabel("")
                        ,decScoreButton);
        scoreLayout.setSpacing(true);
        scoreLayout.setMargin(new MarginInfo(true, true, true, true));
        addComponent(scoreLayout);

        VerticalLayout bottomLayout = new VerticalLayout(cancelButton);
        bottomLayout.setMargin(new MarginInfo(true, true, true, true));
        addComponent(bottomLayout);
        setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);
    }
*/
/*    
    @PostConstruct
    void init() {
        setSizeFull();
        
        VerticalLayout fields = new VerticalLayout(new WLabel("")
                        ,addScoreButton,new WLabel(""),new WLabel(""),new WLabel("")
                        ,decScoreButton);
        fields.setWidth("100%");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));

        
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setWidth("100%");
        viewLayout.setComponentAlignment(fields, Alignment.TOP_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        viewLayout.setHeightUndefined();
        addComponent(viewLayout);
        
        VerticalLayout bottomLayout = new VerticalLayout(cancelButton);
        cancelButton.setWidth("100%");
        cancelButton.setHeightUndefined();
        bottomLayout.setMargin(new MarginInfo(true, true, true, true));
        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("100%");
        bottomLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_CENTER);
        addComponent(bottomLayout);
    }
*/
/*    
    @PostConstruct
    void init() {
        setSizeFull();
        GridLayout grid = new GridLayout(1,5);
        grid.setSizeFull();
        grid.setMargin(new MarginInfo(true, true, true, true));
        
        grid.addComponent(addScoreButton, 0, 0);
        grid.addComponent(new WLabel(""), 0, 1);
        grid.addComponent(decScoreButton, 0, 2);
        grid.addComponent(new WLabel(""), 0, 3);
        grid.addComponent(new WLabel(""), 0, 3);
        grid.addComponent(cancelButton, 0, 4);
        addComponent(grid);
        //addScoreButton.setHeightUndefined();
        //decScoreButton.setHeightUndefined();
        //cancelButton.setHeightUndefined();
    }
*/    
    @PostConstruct
    void init() {
        setWidth("100%");
        setSpacing(true);
        addComponent(new WLabel(""));
        setMargin(new MarginInfo(true, true, true, true));
        addComponent(addScoreButton);
        addComponent(new WLabel(""));
        addComponent(decScoreButton);
        addComponent(new WLabel(""));
        addComponent(new WLabel(""));
        addComponent(new WLabel(""));
        addComponent(cancelButton);
        addScoreButton.setHeightUndefined();
        decScoreButton.setHeightUndefined();
        cancelButton.setHeightUndefined();
        addScoreButton.setWidth("100%");
        decScoreButton.setWidth("100%");
        cancelButton.setWidth("100%");
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        LOG.debug("enter");
        if (!getUI().isWaiterApp()) {
            getUI().fire(new SwitchView_Event(WaiterLoginView.NAME));
            LOG.debug("Fail. Not waiter app. Redirect to {0}", WaiterLoginView.NAME);
            return;
        }
        LOG.debug("Ok");
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == addScoreButton) {
            getUI().fire(new SwitchView_Event(WaiterScoreAddView.NAME));
        }
        if (event.getButton() == decScoreButton) {
            getUI().fire(new SwitchView_Event(WaiterScoreDecView.NAME));
        }
        if (event.getButton() == cancelButton) {
            getUI().fire(new Logout_Event());
        }
    }
}
