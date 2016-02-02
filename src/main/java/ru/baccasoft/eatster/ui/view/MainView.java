package ru.baccasoft.eatster.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.image.ImageResource;
import ru.baccasoft.eatster.image.ImageService;
import ru.baccasoft.eatster.image.ImageValidator;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.service.OperationReportService;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.OperationTotalService;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.ReportService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.ActionPanel;
import ru.baccasoft.eatster.ui.component.MainMenuPanel;
import ru.baccasoft.eatster.ui.component.ModerationPanel;
import ru.baccasoft.eatster.ui.component.ModerationPhotoPanel;
import ru.baccasoft.eatster.ui.component.ReportPanel;
import ru.baccasoft.eatster.ui.component.RestaurantLogoPanel;
import ru.baccasoft.eatster.ui.component.RestaurantPanel;
import ru.baccasoft.eatster.ui.component.RestaurantPhotoPanel;
import ru.baccasoft.eatster.ui.window.WaiterWindow;
import ru.baccasoft.eatster.ui.event.ActionDelete_Event;
import ru.baccasoft.eatster.ui.event.ActionDelete_Listener;
import ru.baccasoft.eatster.ui.event.ActionPhotoUploaded_Event;
import ru.baccasoft.eatster.ui.event.ActionPhotoUploaded_Listener;
import ru.baccasoft.eatster.ui.event.ActionSave_Event;
import ru.baccasoft.eatster.ui.event.ActionSave_Listener;
import ru.baccasoft.eatster.ui.event.Logout_Event;
import ru.baccasoft.eatster.ui.event.ModerationAction_Event;
import ru.baccasoft.eatster.ui.event.ModerationAction_Listener;
import ru.baccasoft.eatster.ui.event.ModerationPhoto_Event;
import ru.baccasoft.eatster.ui.event.ModerationPhoto_Listener;
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
import ru.baccasoft.eatster.ui.event.ShowInformation_Event;
import ru.baccasoft.eatster.ui.event.ShowInformation_Listener;
import ru.baccasoft.eatster.ui.event.ShowPartner_Event;
import ru.baccasoft.eatster.ui.event.ShowPartner_Listener;
import ru.baccasoft.eatster.ui.event.ShowPhoto_Event;
import ru.baccasoft.eatster.ui.event.ShowPhoto_Listener;
import ru.baccasoft.eatster.ui.event.ShowWaiter_Event;
import ru.baccasoft.eatster.ui.event.ShowWaiter_Listener;
import ru.baccasoft.eatster.ui.event.WaiterAction_Event;
import ru.baccasoft.eatster.ui.event.WaiterAction_Listener;
import ru.baccasoft.eatster.ui.event.WaiterDialogCreate_Event;
import ru.baccasoft.eatster.ui.event.WaiterDialogDelete_Event;
import ru.baccasoft.eatster.ui.event.WaiterDialogEdit_Event;
import ru.baccasoft.utils.logging.Logger;
import ru.baccasoft.eatster.ui.event.WaiterDialogEdit_Listener;
import ru.baccasoft.eatster.ui.event.WaiterDialogCreate_Listener;
import ru.baccasoft.eatster.ui.event.WaiterDialogDelete_Listener;
import ru.baccasoft.eatster.image.ImageBuffer;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.service.MailService;
import ru.baccasoft.eatster.service.PushService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.UserReportService;
import ru.baccasoft.eatster.ui.component.PushPanel;
import ru.baccasoft.eatster.ui.component.UsersPanel;
import ru.baccasoft.eatster.ui.event.AdminOperationInsert_Event;
import ru.baccasoft.eatster.ui.window.WarningWindow;
import ru.baccasoft.eatster.ui.event.AdminOperationInsert_Listener;
import ru.baccasoft.eatster.ui.event.ShowOperation_Event;
import ru.baccasoft.eatster.ui.event.ShowOperation_Listener;

@UIScope
@SpringView(name = MainView.NAME)
public class MainView extends VerticalLayout
        implements
        View,
        RestaurantChange_Listener,
        RefreshView_Listener,
        ShowAction_Listener,
        ShowInformation_Listener,
        ShowPartner_Listener,
        ShowOperation_Listener,
        ShowWaiter_Listener,
        ShowPhoto_Listener,
        ActionSave_Listener,
        ActionPhotoUploaded_Listener,
        ActionDelete_Listener,
        WaiterDialogCreate_Listener,
        WaiterDialogDelete_Listener,
        WaiterDialogEdit_Listener,
        WaiterAction_Listener,
        RestaurantPhotoUploaded_Listener,
        RestaurantPhotoSave_Listener,
        RestaurantPhotoDelete_Listener,
        RestaurantLogoUploaded_Listener,
        RestaurantLogoDelete_Listener,
        ModerationPhoto_Listener,
        ModerationAction_Listener,
        ReportModelUpdate_Listener,
        AdminOperationInsert_Listener {

    private static final Logger LOG = Logger.getLogger(MainView.class);

    private static final long serialVersionUID = 1L;
    public static final String NAME = "main";
    private RestaurantPanel restaurantPanel;
    private ModerationPanel moderationPanel;
    private ReportPanel reportPanel;
    private UsersPanel usersPanel;
    private PushPanel pushPanel;
    private MainMenuPanel mainMenu;

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    ActionService actionService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    OperationService operationService;
    @Autowired
    OperationTotalService operationTotalService;
    @Autowired
    WaiterService waiterService;
    @Autowired
    PhotoService photoService;
    @Autowired
    OperationReportService operationReportService;
    @Autowired
    ReportService reportService;
    @Autowired
    ImageService imageService;
    @Autowired
    AppProp appProp;
    @Autowired
    UserReportService userReportService;
    @Autowired
    PushTokenService pushTokenService;
    @Autowired
    PushService pushService;
    @Autowired
    MailService mailService;

    public MainView() {
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @PostConstruct
    void init() {
        //
        setSpacing(true);
        mainMenu = new MainMenuPanel();
        addComponent(mainMenu);
        //
        restaurantPanel = new RestaurantPanel(photoService, actionService, restaurantService, partnerService, operationTotalService, operationService, waiterService);
        restaurantPanel.setVisible(true);
        addComponent(restaurantPanel);
        //
        moderationPanel = new ModerationPanel(photoService, restaurantService, actionService);
        moderationPanel.setVisible(false);
        addComponent(moderationPanel);
        //
        reportPanel = new ReportPanel(operationReportService, reportService);
        reportPanel.setVisible(false);
        addComponent(reportPanel);
        //
        usersPanel = new UsersPanel(userReportService);
        usersPanel.setVisible(false);
        addComponent(usersPanel);
        //
        pushPanel = new PushPanel(pushService, pushTokenService);
        pushPanel.setVisible(false);
        addComponent(pushPanel);
        //
        mainMenu.addButton("Управление ресторанами", restaurantPanel);
        mainMenu.addButton("Модерация", moderationPanel);
        mainMenu.addButton("Отчеты", reportPanel);
        mainMenu.addButton("Push", pushPanel);
        mainMenu.addButton("Пользователи", usersPanel);
        mainMenu.addButtonLogout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        LOG.debug("enter");
        if (!getUI().isAdminApp() && !getUI().isPartnerApp()) {
            getUI().fire(new Logout_Event());
            LOG.debug("Fail. Not admin or partner app. Do logout.");
            return;
        }
        mainMenu.setVisibleButtons(getUI().isAdminApp());
        if (!getUI().isAdminApp()) {
            restaurantPanel.getInformationPanel().setReadOnly(true);
        }
        restaurantPanel.refreshComboBox();
        onShowInformation(new ShowInformation_Event());
        LOG.debug("Ok");
    }

    @Override
    public void onShowAction(ShowAction_Event event) {
        Notification.show("АКЦИИ", Notification.Type.HUMANIZED_MESSAGE);
        restaurantPanel.setActivePanel(restaurantPanel.getListActionsPanel());
    }

    @Override
    public void onShowInformation(ShowInformation_Event event) {
        Notification.show("ИНФОРМАЦИЯ", Notification.Type.HUMANIZED_MESSAGE);
        restaurantPanel.setActivePanel(restaurantPanel.getInformationPanel());
    }

    @Override
    public void onShowOperation(ShowOperation_Event event) {
        Notification.show("ОПЕРАЦИИ / ОПЛАТА", Notification.Type.HUMANIZED_MESSAGE);
        restaurantPanel.setActivePanel(restaurantPanel.getOperationPanel());
    }

    @Override
    public void onShowPartner(ShowPartner_Event event) {
        Notification.show("ПАРТНЕР", Notification.Type.HUMANIZED_MESSAGE);
        restaurantPanel.setActivePanel(restaurantPanel.getPartnerPanel());
    }

    @Override
    public void onShowWaiter(ShowWaiter_Event event) {
        Notification.show("ОФИЦИАНТЫ", Notification.Type.HUMANIZED_MESSAGE);
        restaurantPanel.setActivePanel(restaurantPanel.getWaiterPanel());
    }

    @Override
    public void onShowPhoto(ShowPhoto_Event event) {
        Notification.show("ФОТОГРАФИИ", Notification.Type.HUMANIZED_MESSAGE);
        restaurantPanel.setActivePanel(restaurantPanel.getListPhotosPanel());
    }

    @Override
    public void onRefreshView(RefreshView_Event event) {
        restaurantPanel.refreshRestaurantPanel();
    }

    @Override
    public void onActionSave(ActionSave_Event event) {
        ActionPanel actionPanel = event.getActionPanel();
        ActionModel actionModel = actionPanel.getActionModel();
        LOG.debug("onActionSave action={0}", actionModel);
        long actionId = actionModel.getId();
        //
        if (actionId == 0) {
            actionId = actionService.insertItem(actionModel);
        } else {
            int updated = actionService.updateItem(actionModel);
            if (updated == 0) {
                Notification.show("Ошибка сохранения: информация об акции потеряна.", Notification.Type.ERROR_MESSAGE);
                return;
            }
        }
        actionModel = actionService.getItem(actionId);
        actionPanel.setActionModel(actionModel);
        LOG.debug("Ok.");
        Notification.show("СОХРАНЕНИЕ", Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void onActionDelete(ActionDelete_Event event) {
        ActionPanel actionPanel = event.getActionPanel();
        ActionModel actionModel = actionPanel.getActionModel();
        LOG.debug("onActionDelete action={0}", actionModel);
        if (actionModel.getId() != 0) {
            actionService.deleteItem(actionModel.getId());
        }
        restaurantPanel.getListActionsPanel().removeActionPanel(actionPanel);
        LOG.debug("Ok.");
        Notification.show("УДАЛЕНИЕ", Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void onWaiterDialogCreate(WaiterDialogCreate_Event event) {
        WaiterModel waiterModel = new WaiterModel();
        waiterModel.setRestaurantId(restaurantPanel.getRestaurantModel().getId());
        WaiterWindow waiterWindow = new WaiterWindow(waiterModel, WaiterWindow.ActionType.Create);
        getUI().addWindow(waiterWindow);
    }

    @Override
    public void onWaiterDialogDelete(WaiterDialogDelete_Event event) {
        try {
            String clubEatsterWaiterLogin = appProp.getClubEatsterWaiterLogin().toUpperCase();
            String deleteWaiterLogin = event.getWaiterModel().getLogin().toUpperCase();
            if (clubEatsterWaiterLogin.equals(deleteWaiterLogin)) {
                Notification.show("НЕ МОГУ УДАЛИТЬ СИСТЕМНОГО ОФИЦИАНТА", Notification.Type.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            LOG.error("Error on check waiter system name: "+ex.getMessage());
        }
        WaiterWindow waiterWindow = new WaiterWindow(event.getWaiterModel(), WaiterWindow.ActionType.Delete);
        getUI().addWindow(waiterWindow);
    }

    @Override
    public void onWaiterDialogEdit(WaiterDialogEdit_Event event) {
        WaiterWindow waiterWindow = new WaiterWindow(event.getWaiterModel(), WaiterWindow.ActionType.Edit);
        getUI().addWindow(waiterWindow);
    }

    @Override
    public void onWaiterAction(WaiterAction_Event event) {
        WaiterModel waiterModel = event.getWaiterModel();
        if (event.getActionType() == WaiterWindow.ActionType.Delete) {
            waiterService.deleteItem(waiterModel.getId());
        }
        if (event.getActionType() == WaiterWindow.ActionType.Create) {
            waiterService.insertItem(waiterModel);
        }
        if (event.getActionType() == WaiterWindow.ActionType.Edit) {
            waiterService.updateItem(waiterModel);
        }
        restaurantPanel.refreshRestaurantPanel();
    }
/*
    //FIXME DELETE ACTION
    @Override
    public void onShowAllOperations(ShowAllOperations_Event event) {

        restaurantPanel.setShowAllOperations(!restaurantPanel.isShowAllOperations());
        if (restaurantPanel.isShowAllOperations()) {
            Notification.show("ОПЕРАЦИИ ВСЕХ РЕСТОРАНОВ", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show("ОПЕРАЦИИ ВЫБРАННОГО РЕСТОРАНА", Notification.Type.HUMANIZED_MESSAGE);
        }
        restaurantPanel.refreshRestaurantPanel();
    }
*/
    /*
    public ImageResource uploadImage(String auxBusinessType, String fileName, InputStream fileInputStream) {
        try {
            return imageService.uploadImage(auxBusinessType, fileName, fileInputStream);
        } catch (Exception ex) {
            WarningWindow warningWindow = new WarningWindow("Неизвестная ошибка при загрузке файла.","Возможно файл нулевого размера или другие ошибки связанные с файловой системой.",30,50);
            getUI().addWindow(warningWindow);
            return null;
        }
    }
     */
    public ImageBuffer validateImage(String fileName, InputStream fileInputStream, ImageValidator imageValidator) {
        LOG.debug("validateImage: fileName={0},imageValidator={1}", fileName, imageValidator.getClass());
        try {
            ImageBuffer imageBuffer = new ImageBuffer(fileName, fileInputStream);
            imageValidator.validateFileName(fileName);
            imageValidator.validateImageSize(imageBuffer.getInputStream());
            imageBuffer.resizeImage(imageValidator);
            LOG.debug("validateImage: Ok.");
            return imageBuffer;
        } catch (ImageValidator.ImageValidatorException ex) {
            LOG.warn("Error on validate image: {0}", ex.getMessage());
            WarningWindow warningWindow = new WarningWindow("Ошибка при загрузке фото:", ex.getMessage(), 30, 50);
            getUI().addWindow(warningWindow);
            LOG.debug("validateImage: Fail.");
            return null;
        } catch (IOException ex) {
            LOG.warn("Error on resize image: {0}", ex.getMessage());
            WarningWindow warningWindow = new WarningWindow("Ошибка при загрузке фото:", "Ошибка при преобразовании фото к нужному соотношению сторон.", 30, 50);
            getUI().addWindow(warningWindow);
            LOG.debug("validateImage: Fail.");
            return null;
        }
    }

    @Override
    public void onActionPhotoUploaded(ActionPhotoUploaded_Event event) {
        ActionPanel actionPanel = event.getActionPanel();
        ActionModel actionModel = actionPanel.getActionModel();
        String fileName = event.getFileName().toLowerCase();
        //
        ImageBuffer imageBuffer = validateImage(fileName,
                event.getInputStream(),
                getUI().getPartnerScope().getImageValidatorActionPhoto()
        );
        if (imageBuffer == null) {
            return;
        }
        ImageResource imageResource = imageService.uploadImage(PhotoModel.TYPE_ACTION_PHOTO, fileName, imageBuffer.getInputStream());
        actionModel.setPhotoUrlParams(imageResource.getUrlParams());
        actionPanel.refreshPhoto();
    }

    @Override
    public void onRestaurantPhotoUploaded(RestaurantPhotoUploaded_Event event) {
        //это панель куда грузили
        RestaurantPhotoPanel restaurantPhotoPanel = event.getRestaurantPhotoPanel();
        //сбросим значения полей ввода в модель
        restaurantPhotoPanel.commit();
        //это данные о фото в этой панели
        PhotoModel photoModel = restaurantPhotoPanel.getPhotoModel();
        //
        ImageBuffer imageBuffer = validateImage(event.getFileName(),
                event.getInputStream(),
                restaurantPhotoPanel.getImageValidator()
        );
        if (imageBuffer == null) {
            return;
        }
        //обновим данные и получим новые
        photoModel = photoService.uploadPhoto(photoModel, imageBuffer.getInputStream(), imageBuffer.getFileName());
        restaurantPhotoPanel.setPhotoModel(photoModel);
    }

    @Override
    public void onRestaurantPhotoSave(RestaurantPhotoSave_Event event) {
        //это панель куда грузили
        RestaurantPhotoPanel restaurantPhotoPanel = event.getRestaurantPhotoPanel();
        //сбросим значения полей ввода в модель
        restaurantPhotoPanel.commit();
        //это данные о фото в этой панели
        PhotoModel photoModel = restaurantPhotoPanel.getPhotoModel();
        if (photoModel.getId() == 0) {
            LOG.debug("onRestaurantPhotoSave: attempt to update new photoModel={0}", photoModel);
            Notification.show("Не могу сохранить пустое фото", Notification.Type.HUMANIZED_MESSAGE);
            return;
        }
        photoService.updateItem(photoModel);
    }

    @Override
    public void onRestaurantPhotoDelete(RestaurantPhotoDelete_Event event) {
        //это панель куда грузили
        RestaurantPhotoPanel restaurantPhotoPanel = event.getRestaurantPhotoPanel();
        //это данные о фото в этой панели
        PhotoModel photoModel = restaurantPhotoPanel.getPhotoModel();
        if (photoModel.getId() == 0) {
            return;
        }
        photoService.deletePhoto(photoModel);
        restaurantPanel.getListPhotosPanel().refresh();
    }

    @Override
    public void onRestaurantLogoUploaded(RestaurantLogoUploaded_Event event) {
        //это панель куда грузили
        RestaurantLogoPanel restaurantLogoPanel = event.getRestaurantLogoPanel();
        //это данные о фото в этой панели
        PhotoModel photoModel = restaurantLogoPanel.getPhotoModel();
        //логотипы не нуждаются в модерации
        photoModel.setStatus(PhotoModel.STAT_PUBLISHED);
        //
        ImageBuffer imageBuffer = validateImage(event.getFileName(),
                event.getInputStream(),
                getUI().getPartnerScope().getImageValidatorRestLogo()
        );
        if (imageBuffer == null) {
            return;
        }
        //обновим данные и получим новые
        photoModel = photoService.uploadPhoto(photoModel, imageBuffer.getInputStream(), imageBuffer.getFileName());
        restaurantLogoPanel.setPhotoModel(photoModel);
    }

    @Override
    public void onRestaurantLogoDelete(RestaurantLogoDelete_Event event) {
        //это панель куда грузили
        RestaurantLogoPanel restaurantLogoPanel = event.getRestaurantLogoPanel();
        //это данные о фото в этой панели
        PhotoModel photoModel = restaurantLogoPanel.getPhotoModel();
        if (photoModel.getId() == 0) {
            return;
        }
        photoService.deletePhoto(photoModel);
        photoModel.setId(0);
        photoModel.setPhotoUrlParams("");
        restaurantLogoPanel.setPhotoModel(photoModel);
    }

    @Override
    public void onRestaurantChange(RestaurantChange_Event event) {
        LOG.debug("onRestaurantChange: getRestaurantId={0}", event.getRestaurantId());
        RestaurantModel restaurantModel = restaurantService.getItem(event.getRestaurantId());
        restaurantPanel.setRestaurantModel(restaurantModel);
        restaurantPanel.refreshRestaurantPanel();
    }

    public RestaurantPanel getRestaurantPanel() {
        return restaurantPanel;
    }

    public WaiterService getWaiterService() {
        return waiterService;
    }

    public PartnerService getPartnerService() {
        return partnerService;
    }

    @Override
    public void onModerationPhoto(ModerationPhoto_Event event) {
        PhotoModel photoModel = event.getPhotoModel();
        if (event.getModerationType() == ModerationPhotoPanel.ModerationType.BAN) {
            photoModel.setStatus(PhotoModel.STAT_REJECTED);
        }
        if (event.getModerationType() == ModerationPhotoPanel.ModerationType.CONFIRM) {
            photoModel.setStatus(PhotoModel.STAT_PUBLISHED);
        }
        photoService.updateItem(photoModel);
        restaurantPanel.getListPhotosPanel().refresh();
        moderationPanel.refresh();
    }

    @Override
    public void onModerationAction(ModerationAction_Event event) {
        ActionModel actionModel = event.getActionModel();
        if (event.getModerationType() == ModerationPhotoPanel.ModerationType.BAN) {
            actionModel.setStatus(ActionModel.STAT_REJECTED);
        }
        if (event.getModerationType() == ModerationPhotoPanel.ModerationType.CONFIRM) {
            actionModel.setStatus(ActionModel.STAT_PUBLISHED);
            RestaurantModel restaurantModel = restaurantService.getItem(actionModel.getRestaurantId());
            PartnerModel partnerModel = partnerService.getItem(restaurantModel.getPartnerId());
            String partnerEMail = partnerModel.getName();
            if (!mailService.send(partnerEMail, "Системное уведомление от Eatster", "Акция Вашего заведения прошла модерацию. Название акции - " + actionModel.getName())) {
                LOG.warn("Error on send notification about action is published! actionModel={0}"+actionModel);
                Notification.show("ВниманиЙ Не могу отправить уведомление об утверждении акции партнеру на адрес "+partnerEMail, Notification.Type.ERROR_MESSAGE);
            }
        }
        actionService.updateItem(actionModel);
        restaurantPanel.getListActionsPanel().refresh();
        moderationPanel.refresh();
    }

    @Override
    public void onReportModelUpdate(ReportModelUpdate_Event event) {
        ReportModel reportModel = event.getReportModel();
        int updated = reportService.updateStatus(reportModel);
        if (updated == 0) {
            Notification.show("НЕ МОГУ ОБНОВИТЬ СТАТУС. ПОПРОБУЙТЕ ПОЗДНЕЕ", Notification.Type.WARNING_MESSAGE);
        }
        reportPanel.showReportMonth();
    }

    @Override
    public void onAdminOperationInsert(AdminOperationInsert_Event event) {
        //при добавлении операции администратором проставим идентификаторы клуба Eatster
        long clubEatsterRestaurantId;
        long clubEatsterWaiterId;
        try {
            String clubEatsterRestaurantName = appProp.getClubEatsterRestaurantName();
            String clubEatsterWaiterLogin = appProp.getClubEatsterWaiterLogin();
            LOG.debug("clubEatsterRestaurantName={0},clubEatsterWaiterLogin={1}", clubEatsterRestaurantName, clubEatsterWaiterLogin);
            //
            RestaurantModel restaurantModel = restaurantService.getItem(clubEatsterRestaurantName);
            if (restaurantModel == null) {
                throw new RuntimeException("Error! clubEatsterRestaurant not found by name=" + clubEatsterRestaurantName);
            }
            clubEatsterRestaurantId = restaurantModel.getId();
            //
            WaiterModel waiterModel = waiterService.getItem(clubEatsterWaiterLogin);
            if (waiterModel == null) {
                throw new RuntimeException("Error! clubEatsterWaiter not found by name=" + clubEatsterWaiterLogin);
            }
            clubEatsterWaiterId = waiterModel.getId();
            LOG.debug("clubEatsterRestaurantId={0},clubEatsterWaiterId={1}", clubEatsterRestaurantId, clubEatsterWaiterId);
        } catch (Exception ex) {
            LOG.error("Error on get iDs for clubEatster: ", ex.getMessage());
            Notification.show("Не удалось получить данные об идентификаторах клуба Eatster.", Notification.Type.ERROR_MESSAGE);
            return;
        }

        try {
            event.getOperationModel().setRestaurantId(clubEatsterRestaurantId);
            event.getOperationModel().setWaiterId(clubEatsterWaiterId);
            operationService.insertItem(event.getOperationModel());
            usersPanel.refresh();
        } catch (Exception ex) {
            LOG.debug("Error on insert operation:", ex.getMessage());
            Notification.show("ОШИБКА ПРИ ДОБАВЛЕНИИ ОПЕРАЦИИ! ПОПРОБУЙТЕ ПОЗДНЕЕ", Notification.Type.WARNING_MESSAGE);
        }
    }

}
