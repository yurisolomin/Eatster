
package ru.baccasoft.eatster.appconfig;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.baccasoft.utils.logging.Logger;

@Configuration
@PropertySource("classpath:app.properties")
@EnableVaadin
@EnableAsync
public class AppProp {

    @Autowired
    Environment env;

    private static final Logger LOG = Logger.getLogger(AppProp.class);

    public AppProp() {
        LOG.debug("AppProp.create!");
    }

    public String getProperty(String name) {
        LOG.trace("getProperty: name={0}", name);
        String result = env.getProperty(name);
        LOG.trace("Ok. return {0}", result);
        return result.trim();
    }

    public double getPropertyFloat(String name) {
        LOG.trace("getPropertyFloat: name={0}", name);
        String sResult = env.getProperty(name);
        LOG.trace("String result={0}", sResult);
        double fResult = Double.parseDouble(sResult);
        LOG.trace("getPropertyFloat: Ok. return {0}", fResult);
        return fResult;
    }
    
    public int getPropertyInt(String name) {
        LOG.trace("getPropertyInt: name={0}", name);
        String sResult = env.getProperty(name);
        LOG.trace("String result={0}", sResult);
        int iResult = Integer.parseInt(sResult);
        LOG.trace("getPropertyInt: Ok. return {0}", iResult);
        return iResult;
    }

    public long getPropertyLong(String name) {
        LOG.trace("getPropertyLong: name={0}", name);
        String sResult = env.getProperty(name);
        LOG.trace("String result={0}", sResult);
        long iResult = Long.parseLong(sResult);
        LOG.trace("getPropertyLong: Ok. return {0}", iResult);
        return iResult;
    }

    public String getPropertyDef(String name, String defValue) {
        LOG.trace("getPropertyDef: name={0}, defValue={1}", name,defValue);
        String result = env.getProperty(name);
        if (result == null) {
            LOG.trace("result=null. use defValue={0}",defValue);
            result = defValue;
        }
        LOG.trace("getPropertyDef: Ok. return {0}", result);
        return result;
    }

    public int getPropertyIntDef(String name,int defValue) {
        LOG.trace("getPropertyIntDef: name={0},defValue={1}", name,defValue);
        String sResult = getPropertyDef(name,Integer.toString(defValue));
        int iResult = defValue;
        try {
            iResult = Integer.parseInt(sResult);
        } catch(Exception e) {};
        LOG.trace("getPropertyIntDef: Ok. return {0}", iResult);
        return iResult;
    }
    
    public boolean getPropertyBoolean(String name) {
        LOG.trace("getPropertyBoolean: name={0}",name);
        String sResult = getProperty(name);
        boolean bResult = Boolean.parseBoolean(sResult);
        LOG.trace("getPropertyBoolean: Ok. return {0}", bResult);
        return bResult;
    }
    
    public String getTransferReceiverName() {
        return getPropertyDef("transfer.receiver.name","");
    }
    public String getTransferReceiverINN() {
        return getPropertyDef("transfer.receiver.INN","");
    }
    public String getTransferReceiverKPP() {
        return getPropertyDef("transfer.receiver.KPP","");
    }
    public String getTransferReceiverAccount() {
        return getPropertyDef("transfer.receiver.account","");
    }
    public String getTransferBankName() {
        return getPropertyDef("transfer.bank.name","");
    }
    public String getTransferBankBIC() {
        return getPropertyDef("transfer.bank.BIC","");
    }
    public String getTransferBankAccount() {
        return getPropertyDef("transfer.bank.account","");
    }
    
    public double getCashbackBaseRate() {
        return getPropertyFloat("cashback.base.rate");
    }
    public double getCashbackBonusRate() {
        return getPropertyFloat("cashback.bonus.rate");
    }
    public int getCashbackPeriodLength() {
        return getPropertyInt("cashback.period.length");
    }
        
    public boolean isShowLogins() {
        return getPropertyBoolean("show.logins");
    }
    
    public String getContractUrl() {
        return getProperty("contract.url");
    }
    
    public String getSupportEmail() {
        return getProperty("support.email");
    }
    

    public String getClubEatsterRestaurantName() {
        return getProperty("club.eatster.restaurant.name");
    }
        
    public String getClubEatsterWaiterLogin() {
        return getProperty("club.eatster.waiter.login");
    }
    
    public int getWaiterRestrictionIntervalOnAddScore() {
        return getPropertyInt("waiter.restriction.interval.on.add.score");
    }
    
    public int getLoginAttemptsInMinute() {
        return getPropertyInt("login.attempts.in.minute");
    }

    public String getPushIosKeypath() {
        return getProperty("push.ios.keypath");
    }
    public String getPushIosPassword() {
        return getProperty("push.ios.password");
    }
    public boolean isPushIosProduction() {
        return getPropertyBoolean("push.ios.production");
    }
    public String getPushAndroidApikey() {
        return getProperty("push.android.apikey");
    }
    public String getPushAndroidCollapsekey() {
        return getProperty("push.android.collapsekey");
    }

    public String getNotificationEmail() {
        return getProperty("notification.email");
    }

}
