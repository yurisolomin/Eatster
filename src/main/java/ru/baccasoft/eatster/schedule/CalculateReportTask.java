package ru.baccasoft.eatster.schedule;

import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.service.ReportService;
import ru.baccasoft.utils.logging.Logger;

//look applicationContext.xml to change settings
public class CalculateReportTask {

    private static final Logger LOG = Logger.getLogger(ReportService.class);

    @Autowired
    ReportService reportService;

    public void runTask() {
        LOG.info("runTask:");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        LOG.info("curYear={0},curMonth={1}", year, month);
        --month;
        if (month < 1) {
            month = 12;
            --year;
        }
        LOG.info("calcYear={0},calcMonth={1}", year, month);
        boolean alreadyCalculated = reportService.alreadyCalculated(year, month);
        LOG.info("alreadyCalculated={0}", alreadyCalculated);
        if (alreadyCalculated) {
            LOG.info("runTask: Ok. Skip calculate");
            return;
        }
        try {
            reportService.calculate(year, month);
            reportService.sendEmails(year, month);
            /*            --month;
            if (month < 1) {
                month = 12;
                --year;
            }
            LOG.debug("notpaidYear={0},notpaidMonth={1}",year,month);
            reportService.setAllFormedToNotPaid(year, month);*/
            LOG.info("runTask: Ok.");
        } catch (Exception e) {
            LOG.info("runTask: Fail. Exception on calculate: " + e.getMessage());
        }
    }
}
