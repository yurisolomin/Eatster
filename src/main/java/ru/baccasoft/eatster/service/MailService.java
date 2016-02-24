package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class MailService {
    private static final Logger LOG = Logger.getLogger(MailService.class);
    
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage preConfiguredMessage;
    @Autowired
    AppProp appProp;
    
    public boolean send(String to, String subject, String text) {
        LOG.debug("send:to={0}, subject={1}, text={2}", to, subject, text);
        SimpleMailMessage message = new SimpleMailMessage(preConfiguredMessage);
//        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        LOG.debug("message: from={0}, to={1}, subject={2}, text={3}", 
                message.getFrom(), 
                to, 
                message.getSubject(), 
                message.getText()
                );
        try {
            mailSender.send(message);
            LOG.debug("Ok.");
            return true;
        } catch(Exception e) {
            LOG.error("Fail. Error:"+e.getMessage());
            return false;
        }
    }
    
    private String managerSignature() {
        return
         "С уважением, "+appProp.getManagerName()+"\n"
        +"менеджер по работе с партнёрами EatAction\n"
        +"телефон: "+appProp.getManagerEmail()+"\n"
        +"почта: "+appProp.getManagerPhone()+"\n";
        
    }

    private String financeTeamSignature() {
        return
         "С уважением,\n"
        +"Отдел финансового контроля EatAction\n"
        +"почта: "+appProp.getFinanceTeamEmail()+"\n"
        +"телефон: "+appProp.getFinanceTeamPhone()+"\n";
        
    }

    private String transferInfo() {
        return ""
        +appProp.getTransferReceiverName()+"\n"
        +"ИНН/КПП " + appProp.getTransferReceiverINN()+"/"+appProp.getTransferReceiverKPP()+"\n"
        +"ОГРН " + appProp.getTransferReceiverOGRN()+"\n"
        +"Р/С " + appProp.getTransferReceiverAccount()+"\n"
        +"К/С " + appProp.getTransferBankAccount()+"\n"
        +appProp.getTransferBankName()+"\n"
        +"БИК " + appProp.getTransferBankBIC()+"\n"
        ;
    }
    
    public boolean sendPasswordOnPartnerRegistration(String partnerEmail, String password) {
        return send(partnerEmail, "Пароль для входа EatAction",
         "Спасибо за регистрацию в Системе EatAction!\n"
        +"\n"
        +"Ваш пароль для входа – "+password+"\n"
        +"\n"
        +"Вы можете изменить его в разделе Настройки – Системные настройки.\n"
        +"\n"
        +"Для публикации Вашего заведения, пожалуйста, заполните полностью профиль Вашего заведения: адрес, типы кухни, средний чек и т.п. информацию.  Добавьте фотографии Вашего заведения и/или самых вкусных и красивых блюд которые можно заказать в Вашем заведении.\n"
        +"\n"
        +"Также для большего привлечения клиентов, Вы можете сейчас или позднее, создать Ваши рекламные объявления и акции.\n"
        +"\n"
        +"Если у Вас есть какие-то вопросы - свяжитесь с нами.\n"
        +"\n"
        +managerSignature()         
        );
    }
    
    public boolean sendDetailsOnRestaurantRegistration(String partnerEmail) {
        return send(partnerEmail, "Публикация в Системе EatAction",
         "Здравствуйте уважаемый Партнёр!\n"
        +"\n"
        +"Чтобы активировать Ваш аккаунт в системе EatAction, оплатите, пожалуйста, 1 рубль от имени юридического лица или частного предпринимателя, принявшего условия публичной оферты (сумма активационной оплаты в 1 рубль, будет возвращена Вам).\n"
        +"\n"
        +"Данная процедура является юридическим фактом акцепта публичной оферты и необходима, чтобы проверить Ваши данные, подтвердить Ваши полномочия и предотвратить возможные попытки мошенников, причинить вред Вашей деловой репутации или выдать себя за Вас. \n"
        +"\n"
        +"После активации аккаунта, профиль Вашего ресторана будет опубликован в системе.\n"
        +"\n"
        +"Для большего привлечения клиентов именно в Ваше заведение, рекомендуем Вам создать (если Вы этого ещё не сделали) - рекламные предложения и акций от Вашего заведения! \n"
        +"\n"
        +"Список идей возможных акций, скидок и предложений, которые наверняка заинтересуют потенциальных посетителей Вашего заведения, Вы можете увидеть по адресу: "+appProp.getIdeasUrl()+"\n"
        +"\n"
        +"Возникли вопросы по работе с системой – воспользуйтесь помощью: "+appProp.getHelpUrl()+"\n"
        +"\n"
        +"Наши реквизиты: \n"
        +transferInfo()         
        +"\n"
        +managerSignature()         
        );
    }
    
    public boolean sendRestaurantActivated(String partnerEmail) {
        return send(partnerEmail, "Ваш ресторан опубликован!",
         "Мы получили Ваш платеж в сумме 1 рубль и активировали Ваш аккаунт.\n"
        +"Поздравляем, теперь профиль Вашего заведения опубликован в Системе и мобильном приложении Eatster!\n"
        +"\n"
        +"Для привлечения большего количества посетителей и увеличения дохода, создавайте больше интересных и заманчивых предложений и акций в Системе EatAction! \n"
        +"Потенциальные посетители обязательно их увидят и придут к Вам! \n"
        +"\n"
        +"Идеи разнообразных акций, скидок и предложений, Вы всегда можете взять из презентации «Идеи для повышения посещаемости от EatAction» "+appProp.getIdeasUrl()+".\n"
        +"\n"
        +"Возникли вопросы по работе с системой – воспользуйтесь помощью: "+appProp.getHelpUrl()+"\n"
        +"\n"
        +managerSignature()         
        );
    }

    public boolean sendActionPublished(String partnerEmail, String actionName) {
        return send(partnerEmail, "Акция опубликована - "+actionName,
         "Поздравляем, Ваша акция успешно прошла модерацию в системе EatAction и опубликована!\n"
        +"Готовьтесь встречать новых клиентов.\n"
        +"\n"
        +"Создавайте больше акций, чтобы привлекать больше клиентов.\n"
        +"Идеи разнообразных акций, скидок и предложений, Вы всегда можете взять из презентации «Идеи для повышения посещаемости от EatAction» "+appProp.getIdeasUrl()+".\n"
        +"\n"
        +"Возникли вопросы по работе с системой – воспользуйтесь помощью: "+appProp.getHelpUrl()+"\n"
        +"\n"
        +managerSignature()
        );
    }            
    public boolean sendReportData(String partnerEmail, ReportModel reportModel) {
        String months[] = {"","январь","февраль","март","апрель","май","июнь","июль","август","сентябрь","октябрь","ноябрь","декабрь"};
        int month = reportModel.getReportMonth();
        if (month < 1 || month > 12) {
            month = 0;
        }
        //счет будет выписан следующим годом, если месяц отчета = 12
        //int docYear = reportModel.getReportYear();
        //if (month == 12) {
        //    ++docYear;
        //}
        String roublesName = "рублей";
        int mod10 = reportModel.getCheckSum() % 10;
        switch (mod10) {
            case 1: 
                roublesName = "рубль"; 
                break;
            case 2: 
            case 3: 
            case 4: 
                roublesName = "рубля"; 
                break;
        }
        return send(partnerEmail, "Счет за "+months[month]+" месяц",
         "Закончился месяц, самое время произвести расчет с Системой EatAction, чтобы привлекать ещё больше клиентов.\n"
        +"\n"
        +"Ваш оборот, привлеченных с помощью системы клиентов, за месяц: "+reportModel.getCheckSum()+" "+roublesName+"\n"
        +"Количество операций: "+reportModel.getOperCount()+"\n"
        +"Начислено клиентам баллов: "+reportModel.getScoresTotal()+"\n"
        +"Списано клиентам баллов: "+reportModel.getScoresSpent()+"\n"
        +"\n"
        +"Наши реквизиты:\n"
        +transferInfo()         
        +"\n"
        +financeTeamSignature()         
        );
    }
}
