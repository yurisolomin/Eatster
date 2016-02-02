package ru.baccasoft.eatster.ui.window;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantNoActiveWindow extends Window implements Button.ClickListener {
    
    private static final long serialVersionUID = 1L;

    private Button buttonTransferInfo;
    private Button buttonClose;
    private VerticalLayout layoutButton;
    private final Panel panelTransferInfo = new Panel();
    
    public RestaurantNoActiveWindow(String restaurantName) {
        super("");
        buildLayout(restaurantName);
    }
    
    private class ITextField extends TextField {

        private static final long serialVersionUID = 1L;

        public ITextField(String caption, String value) {
            super(caption, value);
            super.setWidth(15, Unit.CM);
        }
        
    }
    
    private void buildLayout(String restaurantName) {
        GridLayout subContent = new GridLayout(1,4);
        setContent(subContent);
        subContent.setMargin(new MarginInfo(true,true,true,true));
        subContent.setSizeFull();
        subContent.setSpacing(true);

        VerticalLayout topContent = new VerticalLayout();
        //topContent.setMargin(new MarginInfo(true,true,true,true));
        topContent.addComponent(new Label("Внимание!"));
        topContent.addComponent(new Label(""));
        topContent.addComponent(new Label("В целях удаленного подтверждения Ваших полномочий, от имени Вашего юридического лица, с которым заключена Публичная оферта и далее будут производиться взаиморасчёты в системе EatAction, необходимо произвести возвратный проверочный платеж в размере: 1 рубль 00 копеек."));
        topContent.addComponent(new Label("В платёжном документе в графе \"Назначение платежа\" необходимо указать: \"Активация "+restaurantName+" в системе EatAction\""));
        topContent.addComponent(new Label("До активации Вашего аккаунта, Вы можете заполнить информацию о Вашем заведении, загрузить Ваш логотип, фотографии заведения и подготовить рекламные предложения и акции Вашего заведения. Публикация информации о Вашем заведении происходит после активации Вашего аккаунта."));
        topContent.addComponent(new Label("По факту активирования Вашего аккаунта, мы направим Вам сообщение на e-mail указанный при регистрации, о публикации Вашего заведения и Ваших рекламных предложений и акций в Системе Eataction."));
        //
        subContent.addComponent(topContent,0,0);
        //
        layoutButton = new VerticalLayout();
        layoutButton.addComponent(new Label(""));
        layoutButton.addComponent(new Label(""));
        buttonTransferInfo = new Button("Информация для перевода",this);
        layoutButton.addComponent(buttonTransferInfo);
        layoutButton.setComponentAlignment(buttonTransferInfo, Alignment.TOP_CENTER);
        subContent.addComponent(layoutButton,0,1);
        //
        panelTransferInfo.setVisible(false);
        panelTransferInfo.setCaption(buttonTransferInfo.getCaption());
        panelTransferInfo.setSizeUndefined();
        subContent.addComponent(panelTransferInfo,0,2);
        subContent.setComponentAlignment(panelTransferInfo, Alignment.TOP_CENTER);
        //
        buttonClose = new Button("Закрыть",this);
        subContent.addComponent(buttonClose,0,3);
        subContent.setComponentAlignment(buttonClose, Alignment.BOTTOM_RIGHT);
        //
        center();
        setModal(true);
        setHeight(90, Unit.PERCENTAGE);
        setWidth(90,Unit.PERCENTAGE);

    }
    
/*    
    private void buildLayout(String restaurantName) {
        VerticalLayout subContent = new VerticalLayout();
        setContent(subContent);
        subContent.setMargin(new MarginInfo(true,true,true,true));
        subContent.setSizeFull();
        subContent.setSpacing(true);

        VerticalLayout topContent = new VerticalLayout();
        topContent.setMargin(new MarginInfo(true,true,true,true));
        topContent.addComponent(new Label("Внимание!"));
        topContent.addComponent(new Label(""));
        topContent.addComponent(new Label("В целях удаленного подтверждения Ваших полномочий, от имени Вашего юридического лица, с которым заключена Публичная оферта и далее будут производиться взаиморасчёты в системе EatAction, необходимо произвести возвратный проверочный платеж в размере: 1 рубль 00 копеек."));
        topContent.addComponent(new Label("В платёжном документе в графе \"Назначение платежа\" необходимо указать: \"Активация "+restaurantName+" в системе EatAction\""));
        topContent.addComponent(new Label("До активации Вашего аккаунта, Вы можете заполнить информацию о Вашем заведении, загрузить Ваш логотип, фотографии заведения и подготовить рекламные предложения и акции Вашего заведения. Публикация информации о Вашем заведении происходит после активации Вашего аккаунта."));
        topContent.addComponent(new Label("По факту активирования Вашего аккаунта, мы направим Вам сообщение на e-mail указанный при регистрации, о публикации Вашего заведения и Ваших рекламных предложений и акций в Системе Eataction."));
//
//        topContent.addComponent(new Label("Для того чтобы пользоваться системой необходимо провести платёж от имени юр. лица на наш счёт в размере 1 рубля."));
//        topContent.addComponent(new Label("На платёжке необходимо указать название заведения в графе \"назначение платежа\"."));
//        topContent.addComponent(new Label("Пример - активация ресторана Villagio."));
//        topContent.addComponent(new Label(""));
//        topContent.addComponent(new Label("Как только мы активируем Ваш аккаунт мы свяжемся с Вами! До активации вы можете заполнить и редактировать данные о ресторане"));
        //
        subContent.addComponent(topContent);
        //
        layoutButton = new VerticalLayout();
        layoutButton.addComponent(new Label(""));
        layoutButton.addComponent(new Label(""));
        buttonTransferInfo = new Button("Информация для перевода",this);
        layoutButton.addComponent(buttonTransferInfo);
        layoutButton.setComponentAlignment(buttonTransferInfo, Alignment.TOP_CENTER);
        subContent.addComponent(layoutButton);
        //
        panelTransferInfo.setVisible(false);
        panelTransferInfo.setCaption(buttonTransferInfo.getCaption());
        panelTransferInfo.setSizeUndefined();
        subContent.addComponent(panelTransferInfo);
        subContent.setComponentAlignment(panelTransferInfo, Alignment.TOP_CENTER);
        //
        buttonClose = new Button("Закрыть",this);
        subContent.addComponent(buttonClose);
        subContent.setComponentAlignment(buttonClose, Alignment.BOTTOM_RIGHT);
        //
        center();
        setModal(true);
        setHeight(80, Unit.PERCENTAGE);
        setWidth(80,Unit.PERCENTAGE);

    }
*/
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == buttonClose) {
            close();
        }
        if (event.getButton() == buttonTransferInfo) {
            AppProp appProp = getUI().getAppProp();
            FormLayout layoutForm = new FormLayout();
            layoutForm.setMargin(new MarginInfo(true,true,false,true));
            layoutForm.addComponent(new ITextField("Получатель перевода", appProp.getTransferReceiverName()));
            layoutForm.addComponent(new ITextField("ИНН получателя", appProp.getTransferReceiverINN()));
            layoutForm.addComponent(new ITextField("КПП получателя", appProp.getTransferReceiverKPP()));
            layoutForm.addComponent(new ITextField("Расчетный счет", appProp.getTransferReceiverAccount()));
            layoutForm.addComponent(new ITextField("Наименование банка", appProp.getTransferBankName()));
            layoutForm.addComponent(new ITextField("БИК", appProp.getTransferBankBIC()));
            layoutForm.addComponent(new ITextField("Кор.счет", appProp.getTransferBankAccount()));
            panelTransferInfo.setContent(layoutForm);
            panelTransferInfo.setVisible(true);
            layoutButton.setVisible(false);
        }
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

}
