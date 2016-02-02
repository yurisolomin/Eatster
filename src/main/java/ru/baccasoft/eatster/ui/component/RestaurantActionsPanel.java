package ru.baccasoft.eatster.ui.component;

import com.vaadin.shared.ui.MarginInfo;
import ru.baccasoft.eatster.ui.view.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import java.util.List;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantActionsPanel extends VerticalLayout {

    private static final Logger LOG = Logger.getLogger(AdminLoginView.class);

    private static final long serialVersionUID = 1L;
    VerticalLayout layoutActions = null;
    ActionService actionService;

    private long restaurantId = 0;

    public RestaurantActionsPanel(ActionService actionService) {
        this.actionService = actionService;
        buildLayout();
    }

    private void buildLayout() {
//        setSpacing(true);
        layoutActions = new VerticalLayout();
        layoutActions.setSpacing(true);
        addComponent(layoutActions);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void refresh() {
        // TODO Auto-generated method stub
        Notification.show("АКЦИИ", Type.HUMANIZED_MESSAGE);
//        this.restaurantId = getUI().getMainView().getSelectedRestaurantId();
        this.restaurantId = getUI().getMainView().getRestaurantPanel().getSelectedRestaurantId();
        layoutActions.removeAllComponents();
        List<ActionModel> listActions = actionService.findByRestaurantAll(restaurantId);
        for (ActionModel actionModel : listActions) {
            ActionPanel actionPanel = new ActionPanel();
            layoutActions.addComponent(actionPanel);
            actionPanel.setActionModel(actionModel);
        }
    }

    public ActionPanel findNewActionPanel() {
        for(Component component:layoutActions) {
            ActionPanel actionPanel = (ActionPanel)component;
            if (actionPanel.getActionModel().getId() == 0) {
                return actionPanel;
            }
        }
        return null;
    }
    
    public void createActionPanel() {
        ActionPanel actionPanel = new ActionPanel();
        layoutActions.addComponentAsFirst(actionPanel);
        ActionModel actionModel = new ActionModel();
        actionModel.setRestaurantId(restaurantId);
        actionModel.setStatus(ActionModel.STAT_INACTIVE);
        actionPanel.setActionModel(actionModel);
    }

    public void removeActionPanel(ActionPanel actionPanel) {
        layoutActions.removeComponent(actionPanel);
    }
}
