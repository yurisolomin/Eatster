package ru.baccasoft.eatster.ui;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.annotations.PreserveOnRefresh;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.web.context.ContextLoaderListener;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import javax.servlet.annotation.WebInitParam;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.service.MailService;
import ru.baccasoft.eatster.service.OperationReportService;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.OperationTotalService;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.PushService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.ReportService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.service.UserReportService;
import ru.baccasoft.eatster.service.UserSMSService;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.window.RestaurantNoActiveWindow;
import ru.baccasoft.eatster.ui.event.ActionDelete_Event;
import ru.baccasoft.eatster.ui.event.ActionDelete_Listener;
import ru.baccasoft.eatster.ui.event.ActionPhotoUploaded_Event;
import ru.baccasoft.eatster.ui.event.ActionPhotoUploaded_Listener;
import ru.baccasoft.eatster.ui.event.ActionSave_Event;
import ru.baccasoft.eatster.ui.event.ActionSave_Listener;
import ru.baccasoft.eatster.ui.event.LoginSuccess_Event;
import ru.baccasoft.eatster.ui.event.LoginSuccess_Listener;
import ru.baccasoft.eatster.ui.event.Logout_Event;
import ru.baccasoft.eatster.ui.event.Logout_Listener;
import ru.baccasoft.eatster.ui.event.ModerationAction_Event;
import ru.baccasoft.eatster.ui.event.ModerationAction_Listener;
import ru.baccasoft.eatster.ui.event.ModerationPhoto_Event;
import ru.baccasoft.eatster.ui.event.ModerationPhoto_Listener;
import ru.baccasoft.eatster.ui.event.AdminOperationInsert_Event;
import ru.baccasoft.eatster.ui.event.RefreshView_Event;
import ru.baccasoft.eatster.ui.event.RefreshView_Listener;
import ru.baccasoft.eatster.ui.event.ReportModelUpdate_Event;
import ru.baccasoft.eatster.ui.event.ReportModelUpdate_Listener;
import ru.baccasoft.eatster.ui.event.RestaurantChange_Event;
import ru.baccasoft.eatster.ui.event.RestaurantChange_Listener;
import ru.baccasoft.eatster.ui.event.RestaurantLogoDelete_Event;
import ru.baccasoft.eatster.ui.event.RestaurantLogoDelete_Listener;
import ru.baccasoft.eatster.ui.event.RestaurantLogoUploaded_Event;
import ru.baccasoft.eatster.ui.event.RestaurantLogoUploaded_Listener;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoDelete_Event;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoDelete_Listener;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoSave_Event;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoSave_Listener;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoUploaded_Event;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoUploaded_Listener;
import ru.baccasoft.eatster.ui.event.ShowAction_Event;
import ru.baccasoft.eatster.ui.event.ShowAction_Listener;
import ru.baccasoft.eatster.ui.event.ShowAllOperations_Event;
import ru.baccasoft.eatster.ui.event.ShowAllOperations_Listener;
import ru.baccasoft.eatster.ui.event.ShowInformation_Event;
import ru.baccasoft.eatster.ui.event.ShowInformation_Listener;
import ru.baccasoft.eatster.ui.event.ShowOperation_Event;
import ru.baccasoft.eatster.ui.event.ShowOperation_Listener;
import ru.baccasoft.eatster.ui.event.ShowPartner_Event;
import ru.baccasoft.eatster.ui.event.ShowPartner_Listener;
import ru.baccasoft.eatster.ui.event.ShowPhoto_Event;
import ru.baccasoft.eatster.ui.event.ShowPhoto_Listener;
import ru.baccasoft.eatster.ui.event.ShowWaiter_Event;
import ru.baccasoft.eatster.ui.event.ShowWaiter_Listener;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;
import ru.baccasoft.eatster.ui.event.SwitchView_Listener;
import ru.baccasoft.eatster.ui.event.WaiterAction_Event;
import ru.baccasoft.eatster.ui.event.WaiterAction_Listener;
import ru.baccasoft.eatster.ui.event.WaiterDialogCreate_Event;
import ru.baccasoft.eatster.ui.event.WaiterDialogDelete_Event;
import ru.baccasoft.eatster.ui.event.WaiterDialogEdit_Event;
import ru.baccasoft.eatster.ui.view.AdminLoginView;
import ru.baccasoft.eatster.ui.view.MainView;
import ru.baccasoft.utils.logging.Logger;
import ru.baccasoft.eatster.ui.event.WaiterDialogEdit_Listener;
import ru.baccasoft.eatster.ui.event.WaiterDialogCreate_Listener;
import ru.baccasoft.eatster.ui.event.WaiterDialogDelete_Listener;
import ru.baccasoft.eatster.ui.event.WaiterLoginSuccess_Event;
import ru.baccasoft.eatster.ui.event.WaiterLoginSuccess_Listener;
import ru.baccasoft.eatster.ui.scope.PartnerScope;
import ru.baccasoft.eatster.ui.view.NoRestaurantsView;
import ru.baccasoft.eatster.ui.view.PartnerLoginView;
import ru.baccasoft.eatster.ui.view.RegistrationCompleteView;
import ru.baccasoft.eatster.ui.view.WaiterLoginView;
import ru.baccasoft.eatster.ui.view.WaiterMainView;
import ru.baccasoft.eatster.ui.event.AdminOperationInsert_Listener;

@Theme("mytheme")
@SpringUI
@SuppressWarnings("serial")
@PreserveOnRefresh
public class AppUI extends UI
        implements
        LoginSuccess_Listener,
        WaiterLoginSuccess_Listener,
        SwitchView_Listener,
        Logout_Listener {

    private static final Logger LOG = Logger.getLogger(AppUI.class);

    private PartnerModel partnerModel = null;
    private WaiterModel waiterModel = null;

    private final GridLayout appGridLayout = new GridLayout(1, 2);
    private final Panel contentPanel = new Panel();
    private Navigator navigator;
    private final transient Blackboard blackboard = new Blackboard();
    private static final String FILE_SERVLET_URL = "servlets/file?";

    enum AppType {
        APP_ADMIN, APP_PARTNER, APP_WAITER
    };
    private AppType appType;

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private AppProp appProp;

    @Autowired
    private MainView mainView;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    PartnerScope partnerScope;
    @Autowired
    MailService mailService;
    @Autowired
    OperationTotalService operationTotalService;
    @Autowired
    OperationService operationService;
    @Autowired
    ReportService reportService;
    @Autowired
    UserSMSService userSMSService;
    @Autowired
    WaiterService waiterService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    ActionService actionService;
    @Autowired
    PhotoService photoService;
    @Autowired
    OperationReportService operationReportService;
    @Autowired
    UserReportService userReportService;
    @Autowired
    PushService pushService;
    @Autowired
    PushTokenService pushTokenService;

    @WebServlet(
            value = {"/partner/*", "/waiter/*", "/admin/*", "/VAADIN/*"},
            asyncSupported = true,
            initParams = {
                @WebInitParam(name = "ui", value = "ru.baccasoft.eatster.ui.AppUI"),
                @WebInitParam(name = "widgetset", value = "ru.baccasoft.eatster.AppWidgetSet")
            }
    )
    public static class Servlet extends SpringVaadinServlet {
    }

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    }

    /*
    @Configuration
    @EnableVaadin
    public static class MyConfiguration {
    }
     */
    @Override
    protected void init(VaadinRequest request) {
        LOG.debug("AppUI.init");
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        GridLayout rootGridLayout = new GridLayout(1, 1);
        rootGridLayout.setWidth(100, Unit.PERCENTAGE);
        rootGridLayout.setHeight(99, Unit.PERCENTAGE);
        setContent(rootGridLayout);

        appGridLayout.setWidth(100, Unit.PERCENTAGE);
        appGridLayout.setHeight(100, Unit.PERCENTAGE);

        rootGridLayout.addComponent(appGridLayout, 0, 0);
        rootGridLayout.setComponentAlignment(appGridLayout, Alignment.TOP_CENTER);

        contentPanel.setSizeFull();

        //appGridLayout.addComponent(userPanel, 0, 0);
        //appGridLayout.setComponentAlignment(userPanel, Alignment.TOP_CENTER);
        appGridLayout.addComponent(contentPanel, 0, 1);
        appGridLayout.setComponentAlignment(contentPanel, Alignment.TOP_CENTER);

        appGridLayout.setRowExpandRatio(1, 1);

        //set page title
        getPage().setTitle("Eatster");

        navigator = new Navigator(this, contentPanel);
        navigator.addProvider(viewProvider);
        registerEvents();
        addListeners();
        initGuestUser();
    }

    public Blackboard getBlackboard() {
        return blackboard;
    }

    private void initGuestUser() {
        partnerModel = null;
        waiterModel = null;
        appType = null;
        String location = getPage().getLocation().getPath();
        String loginViewName = "";
        if (location.endsWith("/partner")) {
            appType = AppType.APP_PARTNER;
            loginViewName = PartnerLoginView.NAME;
        }
        if (location.endsWith("/waiter")) {
            appType = AppType.APP_WAITER;
            loginViewName = WaiterLoginView.NAME;
        }
        if (location.endsWith("/admin")) {
            appType = AppType.APP_ADMIN;
            loginViewName = AdminLoginView.NAME;
        }
        View loginView = viewProvider.getView(loginViewName);
        navigator.addView("", loginView);
        navigator.setErrorView(loginView);
        navigator.navigateTo(loginViewName);
    }

    private void registerEvents() {
        blackboard.register(SwitchView_Listener.class, SwitchView_Event.class);
        blackboard.register(LoginSuccess_Listener.class, LoginSuccess_Event.class);
        blackboard.register(Logout_Listener.class, Logout_Event.class);
        blackboard.register(ActionDelete_Listener.class, ActionDelete_Event.class);
        blackboard.register(ActionSave_Listener.class, ActionSave_Event.class);
        blackboard.register(RestaurantChange_Listener.class, RestaurantChange_Event.class);
        blackboard.register(ShowAction_Listener.class, ShowAction_Event.class);
        blackboard.register(ShowInformation_Listener.class, ShowInformation_Event.class);
        blackboard.register(ShowPartner_Listener.class, ShowPartner_Event.class);
        blackboard.register(RefreshView_Listener.class, RefreshView_Event.class);
        blackboard.register(ShowOperation_Listener.class, ShowOperation_Event.class);
        blackboard.register(ShowWaiter_Listener.class, ShowWaiter_Event.class);
        blackboard.register(ShowPhoto_Listener.class, ShowPhoto_Event.class);
        blackboard.register(WaiterDialogCreate_Listener.class, WaiterDialogCreate_Event.class);
        blackboard.register(WaiterDialogDelete_Listener.class, WaiterDialogDelete_Event.class);
        blackboard.register(WaiterDialogEdit_Listener.class, WaiterDialogEdit_Event.class);
        blackboard.register(ShowAllOperations_Listener.class, ShowAllOperations_Event.class);
        blackboard.register(WaiterAction_Listener.class, WaiterAction_Event.class);
        blackboard.register(ActionPhotoUploaded_Listener.class, ActionPhotoUploaded_Event.class);
        blackboard.register(RestaurantPhotoUploaded_Listener.class, RestaurantPhotoUploaded_Event.class);
        blackboard.register(RestaurantPhotoDelete_Listener.class, RestaurantPhotoDelete_Event.class);
        blackboard.register(RestaurantPhotoSave_Listener.class, RestaurantPhotoSave_Event.class);
        blackboard.register(RestaurantLogoUploaded_Listener.class, RestaurantLogoUploaded_Event.class);
        blackboard.register(RestaurantLogoDelete_Listener.class, RestaurantLogoDelete_Event.class);
        blackboard.register(WaiterLoginSuccess_Listener.class, WaiterLoginSuccess_Event.class);
        blackboard.register(ModerationPhoto_Listener.class, ModerationPhoto_Event.class);
        blackboard.register(ModerationAction_Listener.class, ModerationAction_Event.class);
        blackboard.register(ReportModelUpdate_Listener.class, ReportModelUpdate_Event.class);
        blackboard.register(AdminOperationInsert_Listener.class, AdminOperationInsert_Event.class);

    }

    private void addListeners() {
        blackboard.addListener(this);
        blackboard.addListener(mainView);
    }

    public void fire(com.github.wolfie.blackboard.Event event) {
        blackboard.fire(event);
    }

    @Override
    public void onLoginSuccess(LoginSuccess_Event event) {
        LOG.debug("onLoginSuccess: partnerModel={0}", event.getPartnerModel());
        partnerModel = event.getPartnerModel();
        //список доступных ресторанов
        List<RestaurantModel> listRestaurant;
        if (partnerModel.isAdmin()) {
            listRestaurant = restaurantService.findAll();
        } else {
            listRestaurant = restaurantService.findByPartner(partnerModel.getId());
        }
        //подтянем справочники для выпадающих списков
        partnerScope.init(listRestaurant);
        //если нет доступных ресторанов
        if (listRestaurant.isEmpty()) {
            if (partnerModel.isAdmin()) {
                //отправим админа на страницу нет данных, т.к. рестораны еще не заводили
                navigator.navigateTo(NoRestaurantsView.NAME);
            } else {
                //а партнера отправим на страницу завершения регистрации ресторана
                navigator.navigateTo(RegistrationCompleteView.NAME);
            }
            LOG.debug("Ok.");
            return;
        }
        //если доступ к чему нибудь есть, то первый в списке - будет ресторан по умолчанию
        RestaurantModel topRestaurant = listRestaurant.get(0);
        mainView.getRestaurantPanel().setRestaurantModel(topRestaurant);
        navigator.navigateTo(MainView.NAME);
        //для партнера проверим статус ресторана и выведем информационное окно, если ресторан не авторизован
        if (!partnerModel.isAdmin()) {
            if (topRestaurant.getStatus().equals(RestaurantModel.STAT_UNAUTH)) {
                addWindow(new RestaurantNoActiveWindow(topRestaurant.getName()));
            }
        }
/*        
        UserSMSModel u = new UserSMSModel();
        u.setPhone("+79053632154");
        u.setText("1234");
        userSMSService.insertItem(u);
        List<UserSMSModel> l = userSMSService.findByPhone("+79053632154",2);
        LOG.debug("size={0}",l.size());
        LOG.debug("item={0}",l.get(0));
*/        
        LOG.debug("Ok.");
    }

    @Override
    public void onWaiterLoginSuccess(WaiterLoginSuccess_Event event) {
        LOG.debug("onWaiterLoginSuccess: waiterModel={0}", event.getWaiterModel());
        waiterModel = event.getWaiterModel();
        navigator.navigateTo(WaiterMainView.NAME);
        LOG.debug("Ok.");
    }

    @Override
    public void onSwitchView(SwitchView_Event event) {
        LOG.debug("onSwitchView:name={0}", event.getViewName());
        navigator.navigateTo(event.getViewName());
        LOG.debug("Ok. Navigate name={0}", event.getViewName());
    }

    @Override
    public void onLogout(Logout_Event event) {
        partnerModel = null;
        waiterModel = null;
        appType = null;
        getSession().close();
        getUI().getPage().setLocation("");
    }

    public PartnerModel getPartnerModel() {
        return partnerModel;
    }

    public WaiterModel getWaiterModel() {
        return waiterModel;
    }

    public String getFileDownloadUrl(String urlParams) {
        return FILE_SERVLET_URL + urlParams;
    }

    public boolean isWaiterApp() {
        return (appType == AppType.APP_WAITER);
    }

    public boolean isAdminApp() {
        return (appType == AppType.APP_ADMIN);
    }

    public boolean isPartnerApp() {
        return (appType == AppType.APP_PARTNER);
    }

    public PartnerScope getPartnerScope() {
        return partnerScope;
    }

    public MainView getMainView() {
        return mainView;
    }

    public AppProp getAppProp() {
        return appProp;
    }

    public MailService getMailService() {
        return mailService;
    }

    public OperationTotalService getOperationTotalService() {
        return operationTotalService;
    }

    public OperationService getOperationService() {
        return operationService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    
/*  FIXME захожу в админку, выход, логин партнерки, опять админка - getUI()=null ?!
    пока не знаю, что с этим делать*/
    @Override
    public void refresh(VaadinRequest request) {
        super.refresh(request);
        LOG.debug("refresh:");
        if (!isAdminApp() && !isPartnerApp()) {
            LOG.debug("Skip refresh. No admin/partner app");
            return;
        }
        if (mainView.getUI() == null) {
            LOG.debug("Skip refresh. mainView.getUI() = null");
            return;
        }
        LOG.debug("Reload admin/partner data");
        //обновим данные партнера/админа
        long partnerId = partnerModel.getId();
        partnerModel = partnerService.getItem(partnerId);
        LOG.debug("admin/partner={0}",partnerModel);
        //список доступных ресторанов
        List<RestaurantModel> listRestaurant;
        if (partnerModel.isAdmin()) {
            listRestaurant = restaurantService.findAll();
        } else {
            listRestaurant = restaurantService.findByPartner(partnerModel.getId());
        }
        //подтянем справочники для выпадающих списков
        partnerScope.init(listRestaurant);
        mainView.doExternalRefresh();
        LOG.debug("Ok.");
    }


    public WaiterService getWaiterService() {
        return waiterService;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public PhotoService getPhotoService() {
        return photoService;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public PartnerService getPartnerService() {
        return partnerService;
    }

    public OperationReportService getOperationReportService() {
        return operationReportService;
    }

    public UserReportService getUserReportService() {
        return userReportService;
    }

    public PushService getPushService() {
        return pushService;
    }

    public PushTokenService getPushTokenService() {
        return pushTokenService;
    }
}
