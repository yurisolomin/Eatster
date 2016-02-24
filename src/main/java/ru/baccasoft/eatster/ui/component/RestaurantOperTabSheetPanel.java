package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantOperTabSheetPanel extends TabSheet {

    private static final long serialVersionUID = -2363229714338627841L;
    
    private final RestaurantOperationsAdminPanel operationsAdminPanel = new RestaurantOperationsAdminPanel();
    private final RestaurantOperationsPartnerPanel operationsPartnerPanel = new RestaurantOperationsPartnerPanel();
    private final RestaurantReportAdminPanel reportsAdminPanel = new RestaurantReportAdminPanel();
    private final RestaurantReportPartnerPanel reportsPartnerPanel = new RestaurantReportPartnerPanel();
    private long restaurantId = 0;

    public RestaurantOperTabSheetPanel() {
        buildLayout();
    }
    private void buildLayout() {
        //
//        tabAdmin = addTab(operationsAdminPanel, "Операции");
//        tabPartner = addTab(operationsPartnerPanel, "Операции");
        addTab(new VerticalLayout(operationsAdminPanel,operationsPartnerPanel), "Операции");
        addTab(new VerticalLayout(reportsAdminPanel,reportsPartnerPanel), "Оплата");
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        }

    public void refresh() {
        if (getUI().isAdminApp()) {
            operationsPartnerPanel.setVisible(false);
            reportsPartnerPanel.setVisible(false);
            //
            operationsAdminPanel.setVisible(true);
            operationsAdminPanel.setRestaurantId(restaurantId);
            reportsAdminPanel.setVisible(true);
            reportsAdminPanel.setRestaurantId(restaurantId);
        } else {
            operationsAdminPanel.setVisible(false);
            reportsAdminPanel.setVisible(false);
            //
            operationsPartnerPanel.setVisible(true);
            operationsPartnerPanel.setRestaurantId(restaurantId);
            reportsPartnerPanel.setVisible(true);
            reportsPartnerPanel.setRestaurantId(restaurantId);
        }
    }
    
    public void clear() {
    }
}
