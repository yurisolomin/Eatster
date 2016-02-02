package ru.baccasoft.eatster.ui.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import ru.baccasoft.utils.logging.Logger;

public class LoginHelper {
    private static final Logger LOG = Logger.getLogger(LoginHelper.class);

    private final ArrayList<Date> attemptsTime = new ArrayList();
    private static final int INTERVAL_IN_MINUTES = 1;
    private final int maxAttemptsOnInterval;
    public static final String WARN_MESSAGE = "Попытки авторизации проводятся слишком часто. Попробуйте немного позднее";

    public LoginHelper(int maxAttemptsOnInterval) {
        this.maxAttemptsOnInterval = maxAttemptsOnInterval;
    }
    
    public boolean isLoginEnabled() {
        LOG.trace("isLoginEnabled: lengthIntervalInMinutes={0},maxAttemptsOnInterval={1},attemptsBefore={2}",INTERVAL_IN_MINUTES,maxAttemptsOnInterval,attemptsTime.size());
        //это время текущей попытки
        Date nowTime = new Date();
        attemptsTime.add(nowTime);
        //индекс для определения начала интервала для анализа
        int indexStartAttempt = attemptsTime.size() - maxAttemptsOnInterval - 1;
        if (indexStartAttempt < 0) {
            LOG.trace("Ok. Few attempts. Return true");
            return true;
        }
        Date startTime = attemptsTime.get(indexStartAttempt);
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        c.add(Calendar.MINUTE, INTERVAL_IN_MINUTES);
        Date enabledTime = c.getTime();
        //для лога сделаем время подробнее
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        LOG.trace("indexStartAttempt={0},startTime={1},enabledTime={2},now={3}",indexStartAttempt,sdf.format(startTime),sdf.format(enabledTime),sdf.format(nowTime));
        if (nowTime.compareTo(enabledTime) >= 0) {
            LOG.trace("Ok. Return true");
            return true;
        }
        LOG.trace("Fail. Return false");
        return false;
    }
    
    public void clearAttempts() {
        attemptsTime.clear();
    }
}
