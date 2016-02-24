package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import java.util.List;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantActionsPanel extends VerticalLayout {

    private static final long serialVersionUID = -6241740858114483242L;

    VerticalLayout layoutActions = null;
    private long restaurantId = 0;

    public RestaurantActionsPanel() {
        buildLayout();
    }

    private void buildLayout() {
        layoutActions = new VerticalLayout();
        layoutActions.setSpacing(true);
        addComponent(layoutActions);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void clear() {
        layoutActions.removeAllComponents();
    }
    
    public void refresh() {
        Notification.show("АКЦИИ", Type.HUMANIZED_MESSAGE);
        clear();
        ActionService actionService = getUI().getActionService();
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
    
    public void addNewActionPanel() {
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

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
}
