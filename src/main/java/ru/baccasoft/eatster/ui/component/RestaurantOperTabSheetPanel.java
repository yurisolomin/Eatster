package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.TabSheet;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantOperTabSheetPanel extends TabSheet {

    private static final long serialVersionUID = -2363229714338627841L;
    
    private final RestaurantOperationsPanel operationsPanel = new RestaurantOperationsPanel();
    private final RestaurantReportPanel reportsPanel = new RestaurantReportPanel();

    public RestaurantOperTabSheetPanel() {
        buildLayout();
    }
    private void buildLayout() {
        //
        addTab(operationsPanel, "Операции");
        addTab(reportsPanel, "Оплата");
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void setRestaurantId(long restaurantId) {
        operationsPanel.setRestaurantId(restaurantId);
        reportsPanel.setRestaurantId(restaurantId);
        }
    
}
