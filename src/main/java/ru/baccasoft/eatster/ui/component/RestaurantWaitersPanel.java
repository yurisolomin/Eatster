package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ClickableRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import java.util.List;
import org.vaadin.gridutil.GridUtil;
import org.vaadin.gridutil.renderer.DeleteButtonValueRenderer;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.WaiterDialogCreate_Event;
import ru.baccasoft.eatster.ui.event.WaiterDialogDelete_Event;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantWaitersPanel extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RestaurantWaitersPanel.class);

    Grid waitersGrid;
//    private GridCellFilter filter;
    private final Button createWaiterButton = new Button("Добавить официанта");

    public RestaurantWaitersPanel() {
        buildLayout();
    }

    private static final int WIDTH_ID = 160;
    private static final int WIDTH_NAME = 300;
    private static final int WIDTH_LOGIN = 200;
    private static final int WIDTH_PASSWORD = 200;
    
    private void buildLayout() {
        setSpacing(true);
        addComponent(createWaiterButton);
        waitersGrid = new Grid();
  //      filter = new GridCellFilter(waitersGrid);
        //
        waitersGrid.setContainerDataSource(new BeanItemContainer<>(WaiterModel.class));
        waitersGrid.setSizeFull();
        addComponent(waitersGrid);
        //
        waitersGrid.setColumnOrder("id", "name", "login", "password");
        waitersGrid.getColumn("id").setHeaderCaption("id").setWidth(WIDTH_ID);
        waitersGrid.getColumn("name").setHeaderCaption("Имя официанта").setWidth(WIDTH_NAME);
        waitersGrid.getColumn("login").setHeaderCaption("Логин").setWidth(WIDTH_LOGIN);
        waitersGrid.getColumn("password").setHeaderCaption("Пароль").setWidth(WIDTH_PASSWORD);
        waitersGrid.removeColumn("restaurantId");
        waitersGrid.removeColumn("deleted");
        waitersGrid.setHeightMode(HeightMode.ROW);
        waitersGrid.setHeightByRows(10);
        waitersGrid.setSelectionMode(Grid.SelectionMode.NONE);
        //подвяжем кнопки к колонке ID
/*        waitersGrid.getColumn("id")
                .setRenderer(new EditDeleteButtonValueRenderer(new EditDeleteButtonClickListener() {

                    @Override
                    public void onDelete(final com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent event) {
                        WaiterModel waiterModel = (WaiterModel)event.getItemId();
                        getUI().fire(new WaiterDialogDelete_Event(waiterModel));
                    }
                    @Override
                    public void onEdit(ClickableRenderer.RendererClickEvent event) {
                        WaiterModel waiterModel = (WaiterModel)event.getItemId();
                        getUI().fire(new WaiterDialogEdit_Event(waiterModel));
                    }

                }));*/
        waitersGrid.getColumn("id")
                .setRenderer(new DeleteButtonValueRenderer(new RendererClickListener() {
                    @Override
                    public void click(ClickableRenderer.RendererClickEvent event) {
                        WaiterModel waiterModel = (WaiterModel)event.getItemId();
                        getUI().fire(new WaiterDialogDelete_Event(waiterModel));
                    }
                }));
        //подвяжем смещение к колонке id
        waitersGrid.setCellStyleGenerator(new CellStyleGenerator() {
            @Override
            public String getStyle(Grid.CellReference cellReference) {
                if (cellReference.getPropertyId().equals("id")) {
                    return GridUtil.ALIGN_CELL_RIGHT;
                } else {
                    return null;
                }
            }

        });

        createWaiterButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().fire(new WaiterDialogCreate_Event());
            }
        });
        
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void bindFieldsBuffered(List<WaiterModel> list) {
        BeanItemContainer<WaiterModel> waiterContainer = new BeanItemContainer<>(WaiterModel.class, list);
        waitersGrid.setContainerDataSource(waiterContainer);
    }
}
