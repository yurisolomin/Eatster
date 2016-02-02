package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class UserBonusService {
    private static final Logger LOG = Logger.getLogger(UserBonusService.class);
    
    @Autowired
    AppProp appProp;
    @Autowired
    UserService userService;

    public static int estimate(String dateStartInString, String dateEndInString) {
        if (DateAsString.getInstance().isEmpty(dateStartInString)) {
            return 0;
        }
        if (DateAsString.getInstance().isEmpty(dateEndInString)) {
            return 0;
        }
        Date dateStart = DateAsString.getInstance().toDate(dateStartInString);
        Date dateEnd = DateAsString.getInstance().toDate(dateEndInString);
        long timeStart = dateStart.getTime();
        long timeEnd = dateEnd.getTime();
        long diffTime = timeEnd - timeStart;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        long result = diffDays + 1;
        if ( diffDays > Integer.MAX_VALUE || diffDays < Integer.MIN_VALUE ) {
            LOG.warn("Error on estimate({0},{1})! diffDays={2}. Is not int. Set estimate=0", dateStartInString, dateEndInString, diffDays);
            result = 0;
        }
        return (int)result;
    }
    
    private int getPersonalPeriodEstimate(long userId, String onDate) {
        LOG.debug("getPersonalPeriodEstimate: userId={0},onDate={1}", userId,onDate);
        String bonusDateEnd = userService.getPersonalBonusDateEnd(userId);
        LOG.debug("bonusDateEnd={0}", bonusDateEnd);
        int days = estimate(onDate,bonusDateEnd);
        LOG.debug("Ok. return {0}", days);
        return days;
    }
    
    private int getFriendsPeriodEstimate(long userId, String onDate) {
        LOG.debug("getFriendsPeriodEstimate: userId={0},onDate={1}", userId,onDate);
        String bonusDateEnd = userService.getFriendsBonusDateEnd(userId);
        LOG.debug("bonusDateEnd={0}", bonusDateEnd);
        int days = estimate(onDate,bonusDateEnd);
        LOG.debug("Ok. return {0}", days);
        return days;
    }

    //определение даты окончания бонуса для пользователя, как максимальную из дат - личной и по друзьям
    private String getDateEnd(long userId) {
        LOG.debug("getDateEnd: id={0}", userId);
        String personalBonusDateEnd = userService.getPersonalBonusDateEnd(userId);
        String friendsBonusDateEnd = userService.getFriendsBonusDateEnd(userId);
        int compareDate = DateAsString.getInstance().compare(personalBonusDateEnd,friendsBonusDateEnd);
        LOG.debug("compareDate({0},{1})={2}",personalBonusDateEnd,friendsBonusDateEnd,compareDate);
        String bonusDateEndMax;
        if (compareDate <= 0) {
            bonusDateEndMax = friendsBonusDateEnd;
        } else {
            bonusDateEndMax = personalBonusDateEnd;
        }
        LOG.warn("Ok. Return bonusDateEndMax={0}",bonusDateEndMax);
        return bonusDateEndMax;
    }

    
    public int getPeriodEstimate(long userId, String onDate) {
        LOG.debug("getPeriodEstimate: userId={0},onDate={1}", userId,onDate);
        int personalPeriodEstimate = getPersonalPeriodEstimate(userId, onDate);
        int friendsPeriodEstimate = getFriendsPeriodEstimate(userId, onDate);
        LOG.debug("personalPeriodEstimate={0},friendsPeriodEstimate={1}", personalPeriodEstimate,friendsPeriodEstimate);
        int periodEstimate;
        if (personalPeriodEstimate < friendsPeriodEstimate) {
            periodEstimate = friendsPeriodEstimate;
        } else {
            periodEstimate = personalPeriodEstimate;
        }
        LOG.debug("Ok. return {0}", periodEstimate);
        return periodEstimate;
    }
    
    public int getPeriodEstimateOnCurDate(long userId) {
        String curDate = DateAsString.getInstance().curDateAsString();
        return getPeriodEstimate(userId,curDate);
    }

    @Transactional
    private void checkOnTransact(long userId, String onDateInString) {
        LOG.debug("checkOnTransact: userId={0},onDate={1}", userId,onDateInString);
        UserModel item = userService.getItem(userId);
        if (item == null) {
            LOG.error("Error! User not found");
            return;
        }
        if (item.isBonusActivated() ) {
            LOG.debug("Ok. Bonus already activated");
            return;
        }
        if (item.getFriendPromocode().isEmpty()) {
            LOG.debug("Ok. No friendPromocode");
            return;
        }
        //при пустой или неверной дате будем считать что бонус не активен
        if (DateAsString.getInstance().isEmpty(onDateInString)) {
            LOG.error("Error! onDate is empty");
            return;
        }
        Long friend_user_id = item.getFriendUserId();
        if (friend_user_id == null) {
            LOG.error("Error! UserId is null but friendPromocode exists");
            return;
        }
        if (!item.getBonusDateEnd().isEmpty()) {
            LOG.error("Error! bonusDateEnd not empty");
            return;
        }
        if (!item.getFriendBonusDateEnd().isEmpty()) {
            LOG.error("Error! friendBonusDateEnd not empty");
            return;
        }
        //это бонусный период
        int cashbackPeriodLength = appProp.getCashbackPeriodLength();
        LOG.debug("cashbackPeriodLength={0}",cashbackPeriodLength);
        //это дата на которую проводится операция
        Date onDate = DateAsString.getInstance().toDate(onDateInString);
        //установим дату окончания бонуса лично для пользователя
        Date personalDateEnd = DateAsString.getInstance().addDays(onDate,cashbackPeriodLength-1);
        String personalDateEndInString = DateAsString.getInstance().toString(personalDateEnd);
        item.setBonusActivated(true);
        item.setBonusDateEnd(personalDateEndInString);
        LOG.debug("Set bonusActivated=true");
        LOG.debug("Set personalBonusDateEnd={0}",personalDateEndInString);
        //а теперь продлим бонусный период для моего друга
        //это текущий бонусный период друга
        int friendPeriodEstimate = getPeriodEstimate(item.getFriendUserId(), onDateInString);
        //добавим к нему бонусный период
        Date friendDateEnd = DateAsString.getInstance().addDays(onDate,friendPeriodEstimate+cashbackPeriodLength-1);
        String friendDateEndInString = DateAsString.getInstance().toString(friendDateEnd);
        item.setFriendBonusDateEnd(friendDateEndInString);
        LOG.debug("friendUserId={0},curFriendPeriodEstimate={1}",item.getFriendUserId(),friendPeriodEstimate);
        LOG.debug("Set friendFriendDateEnd={0}",friendDateEndInString);
/*
        String friendBonusDateEnd = getDateEnd(item.getFriendUserId());
        LOG.debug("Current friendBonusDateEnd={0}",friendBonusDateEnd);
        //если ее нет, то она равна моей
        if (friendBonusDateEnd.isEmpty()) {
            friendBonusDateEnd = personalBonusDateEnd;
        } else {
            //если она есть, то добавим к ней бонусный приод
            Date date1 = DateAsString.getInstance().toDate(friendBonusDateEnd);
            Date date2 = DateAsString.getInstance().addDays(date1,cashbackPeriodLength);
            //если она получается меньше персональной, то приравняем ее к персональной
            Date date3 = DateAsString.getInstance().toDate(personalBonusDateEnd);
            if (date3.compareTo(date2) > 0) {
                date2 = date3;
            }
            friendBonusDateEnd = DateAsString.getInstance().toString(date2);
        }
        item.setFriendBonusDateEnd(friendBonusDateEnd);
        LOG.debug("Set friendBonusDateEnd={0}",friendBonusDateEnd);
*/
        //обновим данные по бонусам
        userService.updateItem(item);
        LOG.debug("checkOnTransact:Ok.");
    }
    
    public boolean getActiveOnTransact(long userId, String onDate) {
        LOG.debug("getActiveOnTransact: userId={0},onDate={1}", userId,onDate);
        //обратимся к бонусной логике при выполнении транзакции
        checkOnTransact(userId,onDate);
        //мой личный бонусный период
        int personalPeriodEstimate = getPersonalPeriodEstimate(userId, onDate);
        LOG.debug("personalPeriodEstimate={0}", personalPeriodEstimate);
        if (personalPeriodEstimate > 0) {
            LOG.debug("Ok. Return true");
            return true;
        }
        //период по друзьям
        int friendsPeriodEstimate = getFriendsPeriodEstimate(userId,onDate);
        LOG.debug("friendsPeriodEstimate={0}", friendsPeriodEstimate);
        if (friendsPeriodEstimate > 0) {
            LOG.debug("Ok. Return true");
            return true;
        }
        LOG.debug("Ok. Return false");
        return false;
    }
    
}
