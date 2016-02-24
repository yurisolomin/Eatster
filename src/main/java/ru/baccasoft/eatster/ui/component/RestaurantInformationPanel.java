package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.scope.PartnerScope;

public class RestaurantInformationPanel extends TabSheet {
    private static final long serialVersionUID = -3971748969794296788L;

    private static final float WIDTH_LABEL = 4.3f;

    private class Fields {
        private static final int WIDTH_CARDS = 2;
        private static final int WIDTH_TINY = 3;
        private static final int WIDTH_SMALL = 5;
        private static final int WIDTH_DEFAULT = 9;
        private static final int WIDTH_NAME = 12;
        private static final int WIDTH_LARGE = 13;
        private static final int WIDTH_ENTERTAINMENTS = 15;

        TextField name = new TextField(null,"Не заполнено название заведения");
        TextArea description = new TextArea();//
        GridLayout publicLayout = new GridLayout();
        ComboBox cuisineId1 = new EatsterIdComboBox(null,"Укажите кухню");//"Кухня"
        ComboBox cuisineId2 = new EatsterIdComboBox(null);
        ComboBox cuisineId3 = new EatsterIdComboBox(null);
        TextField address = new TextField(null,"Не заполнено поле 'Адрес'");
//        TextField coordLatAsString = new TextField();
//        TextField coordLonAsString = new TextField();
        EatsterCoordField myCoordLat = new EatsterCoordField("Latt");
        EatsterCoordField myCoordLon = new EatsterCoordField("Long");
        ComboBox subwayId = new EatsterIdComboBox(null,"Укажите метро");
        ComboBox averageCheckId = new EatsterIdComboBox(null,"Укажите средний чек");
        ComboBox parkingId = new EatsterIdComboBox(null,"Укажите парковку");
        EatsterPhoneField phone = new EatsterPhoneField();
        TextField website = new TextField();
        ComboBox wifi = new EatsterAvailableComboBox(null,"Не заполнено поле 'wi-fi'");
        ComboBox kidsMenu = new EatsterAvailableComboBox(null,"Не заполнено поле 'Детское меню'");
        TextField entertainments = new TextField();
        CheckBox cardsVisa = new CheckBox("Visa");
        CheckBox cardsMasterCard = new CheckBox("Master Card");
        CheckBox cardsMaestro = new CheckBox("Maestro");
        CheckBox cardsUnionPay = new CheckBox("UnionPay");
        CheckBox cardsVisaElectron = new CheckBox("Visa electron");
        CheckBox cardsAmericanExpress = new CheckBox("American express");
        CheckBox cardsDinersClub = new CheckBox("Diners club");
        CheckBox cardsPro100 = new CheckBox("Pro100");
        CheckBox cardsJCB = new CheckBox("JCB");
        CheckBox cardsMIR = new CheckBox("МИР");
        ComboBox workdayStartTime = new EatsterTimeComboBox(null,"Не заполнено время начала работы в будни");
        ComboBox workdayEndTime = new EatsterTimeComboBox(null,"Не заполнено время окончания работы в будни");
        ComboBox holidayStartTime = new EatsterTimeComboBox(null,"Не заполнено время начала работы в выходные");
        ComboBox holidayEndTime = new EatsterTimeComboBox(null,"Не заполнено время окончания работы в выходные");
        CheckBox workdayMonday = new CheckBox("ПН");
        CheckBox workdayTuesday = new CheckBox("ВТ");
        CheckBox workdayWednesday = new CheckBox("СР");
        CheckBox workdayThursday = new CheckBox("ЧТ");
        CheckBox workdayFriday = new CheckBox("ПТ");
        CheckBox workdaySaturday = new CheckBox("СБ");
        CheckBox workdaySunday = new CheckBox("ВС");
        //
        TextField legalName = new TextField("Юр. название");
        TextField legalAddress = new TextField("Юр. адрес");
        TextField director = new TextField("ФИО директора");
        TextField bank = new TextField("Наименование Банка");
        TextField OGRN = new TextField("ОГРН");
        TextField INN = new TextField("ИНН");
        TextField corrAccount = new TextField("Кор.счет");
        TextField custAccount = new TextField("Расчетный счет");
        TextField KPP = new TextField("КПП");
        TextField BIC = new TextField("БИК");

        public Fields() {
            name.setWidth(WIDTH_NAME, Unit.CM);
            description.setWordwrap(true);
            description.setRows(3);
            description.setWidth(WIDTH_NAME, Unit.CM);
            address.setWidth(WIDTH_NAME, Unit.CM);
            subwayId.setWidth(WIDTH_DEFAULT, Unit.CM);
            website.setWidth(WIDTH_DEFAULT, Unit.CM);
            phone.setWidth(WIDTH_DEFAULT, Unit.CM);
            averageCheckId.setWidth(WIDTH_DEFAULT, Unit.CM);
            parkingId.setWidth(WIDTH_DEFAULT, Unit.CM);
            wifi.setWidth(WIDTH_DEFAULT, Unit.CM);
            kidsMenu.setWidth(WIDTH_DEFAULT, Unit.CM);
            entertainments.setWidth(WIDTH_ENTERTAINMENTS, Unit.CM);
            cardsVisa.setWidth(WIDTH_CARDS, Unit.CM);
            cardsMasterCard.setWidthUndefined();
            cardsMaestro.setWidthUndefined();
            cardsUnionPay.setWidthUndefined();
            cardsVisaElectron.setWidthUndefined();
            cardsAmericanExpress.setWidthUndefined();
            cardsDinersClub.setWidthUndefined();
            cardsPro100.setWidthUndefined();
            cardsJCB.setWidthUndefined();
            cardsMIR.setWidthUndefined();
            
            workdayStartTime.setWidth(WIDTH_TINY, Unit.CM);
            workdayEndTime.setWidth(WIDTH_TINY, Unit.CM);
            holidayStartTime.setWidth(WIDTH_TINY, Unit.CM);
            holidayEndTime.setWidth(WIDTH_TINY, Unit.CM);
            //
            legalName.setWidth(WIDTH_LARGE, Unit.CM);
            legalAddress.setWidth(WIDTH_LARGE, Unit.CM);
            director.setWidth(WIDTH_LARGE, Unit.CM);
            OGRN.setWidth(WIDTH_LARGE, Unit.CM);
            INN.setWidth(WIDTH_LARGE, Unit.CM);
            KPP.setWidth(WIDTH_LARGE, Unit.CM);
            bank.setWidth(WIDTH_LARGE, Unit.CM);
            BIC.setWidth(WIDTH_LARGE, Unit.CM);
            custAccount.setWidth(WIDTH_LARGE, Unit.CM);
            corrAccount.setWidth(WIDTH_LARGE, Unit.CM);
            //
            name.setRequired(true);
            name.setRequiredError("Не заполнено название заведения");
            name.setValidationVisible(false);
            address.setRequired(true);
            address.setRequiredError("Не заполнено поле 'Адрес'");
            address.setValidationVisible(false);
            phone.setRequired(true);
            phone.setRequiredError("Не заполнено поле 'Телефон'");
            phone.setValidationVisible(false);
        }

    }
    private final Fields fields = new Fields();
    private BeanFieldGroup<RestaurantModel> fieldBindings = null;
    private RestaurantModel restaurantModel = null;
    private final RestaurantLogoPanel logoPhotoPanel = new RestaurantLogoPanel("Логотип");
    private Tab tabLegalData;

    public RestaurantInformationPanel() {
        buildLayout();
    }

    private void addToGrid(GridLayout grid, int row, Component component, String caption) {
        Label lab = new Label(caption);
        lab.setWidth(WIDTH_LABEL,Unit.CM);
        grid.addComponent(lab, 0, row);
        grid.addComponent(component, 1, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    private void buildLayout() {
        GridLayout gridTop = new GridLayout(2, 2);
        gridTop.setSpacing(true);
        gridTop.setSizeUndefined();
        addToGrid(gridTop, 0, fields.name, "Название ресторана");
        addToGrid(gridTop, 1, fields.description, "Описание ресторана");
        HorizontalLayout hLayout = new HorizontalLayout(gridTop,new Label(""),logoPhotoPanel);
        hLayout.setSpacing(true);

        GridLayout grid = new GridLayout(2, 15);
        grid.setSpacing(true);
        grid.setSizeUndefined();

        int row = -1;
        HorizontalLayout layout;
        //
        layout = new HorizontalLayout(fields.cuisineId1, fields.cuisineId2, fields.cuisineId3);
        layout.setSpacing(true);
        addToGrid(grid, ++row, layout, "Кухня");
        //
        layout = new HorizontalLayout(fields.address, new Label("Координаты"), new Label("latt"), fields.myCoordLat, new Label("long"), fields.myCoordLon);
        layout.setSpacing(true);
        addToGrid(grid, ++row, layout, "Адрес");
        //
        addToGrid(grid, ++row, new HorizontalLayout(fields.subwayId), "Метро");
        //
        layout = new HorizontalLayout(fields.phone, new Label("web сайт"), fields.website);
        layout.setSpacing(true);
        addToGrid(grid, ++row, layout, "Телефон");
        //
        //оборачиваю в HorizontalLayout т.к. инчае звездочка не показывается. видать косяк какой-то vaadin
        addToGrid(grid, ++row, new HorizontalLayout(fields.averageCheckId), "Средний чек");
        addToGrid(grid, ++row, new HorizontalLayout(fields.wifi), "wi-fi");
        addToGrid(grid, ++row, new HorizontalLayout(fields.parkingId), "Парковка");
        addToGrid(grid, ++row, new HorizontalLayout(fields.kidsMenu), "Детское меню");
        addToGrid(grid, ++row, fields.entertainments, "Развлечения");
        //
        layout = new HorizontalLayout(fields.cardsVisa, fields.cardsMasterCard, fields.cardsMaestro, fields.cardsUnionPay, fields.cardsVisaElectron, fields.cardsAmericanExpress,fields.cardsDinersClub,fields.cardsPro100,fields.cardsJCB,fields.cardsMIR);
        layout.setSpacing(true);
        addToGrid(grid, ++row, layout, "Пласт.карты");
        addToGrid(grid, ++row, new Label("Время работы"), "");
        //
        layout = new HorizontalLayout(new Label("с"), fields.workdayStartTime, new Label("по"), fields.workdayEndTime,
                new Label("  "), fields.workdayMonday, fields.workdayTuesday, fields.workdayWednesday, fields.workdayThursday, fields.workdayFriday);
        layout.setSpacing(true);
        addToGrid(grid, ++row, layout, "Будни");
        //
        layout = new HorizontalLayout(new Label("с"), fields.holidayStartTime, new Label("по"), fields.holidayEndTime,
                new Label("  "), fields.workdaySaturday, fields.workdaySunday);
        layout.setSpacing(true);
        addToGrid(grid, ++row, layout, "Выходные");
        
        VerticalLayout tabLayout = new VerticalLayout(new Label(""),hLayout,grid);
        tabLayout.setSpacing(true);
        //tabLayout.setMargin(new MarginInfo(false, false, false, true));
        addTab(tabLayout, "Публичные данные");
        //
        FormLayout form;
        form = new FormLayout(fields.legalName, fields.legalAddress, fields.director, fields.bank, fields.OGRN, fields.INN, fields.corrAccount, fields.custAccount, fields.KPP, fields.BIC);
        form.setSpacing(true);
        form.setSizeUndefined();
        //
        tabLegalData = addTab(form, "Юридические данные");
        //
        fields.name.focus();
    }

    public void refreshComboBox() {
        PartnerScope partnerScope = getUI().getPartnerScope();
        partnerScope.comboboxForCuisine(fields.cuisineId1);
        partnerScope.comboboxForCuisine(fields.cuisineId2);
        partnerScope.comboboxForCuisine(fields.cuisineId3);
        partnerScope.comboboxForSubway(fields.subwayId);
        partnerScope.comboboxForParking(fields.parkingId);
        partnerScope.comboboxForAverageCheck(fields.averageCheckId);
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void setRestaurantModel(RestaurantModel restaurantModel, boolean readonly) {
        this.restaurantModel = restaurantModel;
        fieldBindings = BeanFieldGroup.bindFieldsBuffered(restaurantModel, fields);
        fields.myCoordLat.setReadOnly(false);
        fields.myCoordLon.setReadOnly(false);
        fields.myCoordLat.setValue(restaurantModel.getCoordLat());
        fields.myCoordLon.setValue(restaurantModel.getCoordLon());
        //логотип
        long restaurantId = restaurantModel.getId();
        PhotoService photoService = getUI().getPhotoService();
        PhotoModel logoPhotoModel = photoService.findRestaurantLogo(restaurantId);
        if (logoPhotoModel == null) {
            logoPhotoModel = new PhotoModel();
            logoPhotoModel.setObjectId(restaurantId);
            logoPhotoModel.setObjectType(PhotoModel.TYPE_REST_LOGO);
            logoPhotoModel.setStatus(PhotoModel.STAT_MODERATION);
        }
        //всем поставим только чтение
        logoPhotoPanel.setPhotoModel(logoPhotoModel,readonly);
        fieldBindings.setReadOnly(readonly);
        //но выброчно разрешим
        fields.myCoordLat.setReadOnly(false);
        fields.myCoordLon.setReadOnly(false);
        fields.subwayId.setReadOnly(false);
        fields.phone.setReadOnly(false);
        fields.website.setReadOnly(false);
        fields.wifi.setReadOnly(false);
        fields.kidsMenu.setReadOnly(false);
        fields.parkingId.setReadOnly(false);
        fields.cardsVisa.setReadOnly(false);
        fields.cardsMasterCard.setReadOnly(false);
        fields.cardsMaestro.setReadOnly(false);
        fields.cardsUnionPay.setReadOnly(false);
        fields.cardsVisaElectron.setReadOnly(false);
        fields.cardsAmericanExpress.setReadOnly(false);
        fields.cardsDinersClub.setReadOnly(false);
        fields.cardsPro100.setReadOnly(false);
        fields.cardsJCB.setReadOnly(false);
        fields.cardsMIR.setReadOnly(false);
        fields.workdayStartTime.setReadOnly(false);
        fields.workdayEndTime.setReadOnly(false);
        fields.holidayStartTime.setReadOnly(false);
        fields.holidayEndTime.setReadOnly(false);
        fields.workdayMonday.setReadOnly(false);
        fields.workdayTuesday.setReadOnly(false);
        fields.workdayWednesday.setReadOnly(false);
        fields.workdayThursday.setReadOnly(false);
        fields.workdayFriday.setReadOnly(false);
        fields.workdaySaturday.setReadOnly(false);
        fields.workdaySunday.setReadOnly(false);
//        fields.myCoordLat.setReadOnly(readonly);
//        fields.myCoordLon.setReadOnly(readonly);
    }

    public void validate() {
            fields.name.validate();
            fields.cuisineId1.validate();
            fields.address.validate();
            fields.subwayId.validate();
            fields.phone.validate();
            fields.averageCheckId.validate();
            fields.wifi.validate();
            fields.parkingId.validate();
            fields.kidsMenu.validate();
            fields.workdayStartTime.validate();
            fields.workdayEndTime.validate();
            fields.holidayStartTime.validate();
            fields.holidayEndTime.validate();
    }
    
    public void commit() throws FieldGroup.CommitException {
        fieldBindings.commit();
        restaurantModel.setCoordLat(fields.myCoordLat.getValue());
        restaurantModel.setCoordLon(fields.myCoordLon.getValue());
    }
    
    public void setLogoVisible(boolean visible) {
        logoPhotoPanel.setVisible(visible);
    }
    public void setLegalDataVisible(boolean visible) {
        tabLegalData.setVisible(visible);
    }

    //пока не знаю как по другому обнулить
    public void clearSimpleMenus() {
        fields.kidsMenu.setValue(null);
        fields.wifi.setValue(null);
    }
}
