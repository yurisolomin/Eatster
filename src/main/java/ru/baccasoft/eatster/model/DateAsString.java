package ru.baccasoft.eatster.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DateAsString {

    private final Date emptyDate;
    //private final String EMPTY_DATE_VALUE = "1900-01-01";

    private DateAsString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        emptyDate = new Date((calendar.getTime()).getTime());
    }
    
    private Date valueOf(String dateInString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(dateInString);
    }

    private Date valueOf(String dateInString,String timeInString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.parse(dateInString+" "+timeInString);
    }

    public boolean isEmpty(Date date) {
        if (date == null) {
            return true;
        }
        return date.getTime() == emptyDate.getTime();
    }

    public boolean isEmpty(String dateInString) {
        return isEmpty(toDate(dateInString));
    }

    public Date toDate(String dateInString) {
        if (dateInString == null) {
            return emptyDate;
        }
        Date result = emptyDate;
        try {
            result = valueOf(dateInString);
        } catch (ParseException ex) {
        }
        return result;
    }
    
    public Date toDate(String dateInString, String timeInString) {
        if (dateInString == null) {
            return emptyDate;
        }
        if (timeInString == null) {
            return emptyDate;
        }
        Date result = emptyDate;
        try {
            result = valueOf(dateInString,timeInString);
        } catch (ParseException ex) {
        }
        return result;
    }

    public String toString(Date date) {
        if (isEmpty(date)) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    public String toString(Date date, String format) {
        if (isEmpty(date)) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public Date curDate() {
        return new Date();
//        Calendar currenttime = Calendar.getInstance();
//        return currenttime.getTime();
//        return new Date((currenttime.getTime()).getTime());
    }

    public String curTimeAsHHMM() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(cal.getTime());
    }

    public String curDateAsString() {
        Date curdate = curDate();
        return toString(curdate);
    }

    public Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date); // Now use today date.
        c.add(Calendar.DATE, days);
//        return new Date((c.getTime()).getTime());
        return c.getTime();
    }

    public Date addHours(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
//        return new Date((c.getTime()).getTime());
    }
    
    public Date getStartMonth(int year, int month) {
        if (month < 1 || month > 12) {
            throw new RuntimeException("Error month on getStartMonth(" + year + "," + month + ")");
        }
        --month;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date((calendar.getTime()).getTime());
    }

    public String getStartMonthAsString(int year, int month) {
        return toString(getStartMonth(year, month));
    }

    public Date getEndMonth(int year, int month) {
        if (month < 1 || month > 12) {
            throw new RuntimeException("Error month on getEndMonth(" + year + "," + month + ")");
        }
        --month;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date((calendar.getTime()).getTime());
    }

    public String getEndMonthAsString(int year, int month) {
        return toString(getEndMonth(year, month));
    }

    public String fixAsString(String dateInString) {
        if (dateInString == null) {
            return "";
        }
        String result = "";
        try {
            result = toString(valueOf(dateInString));
        } catch (ParseException ex) {
        }
        return result;
    }

    public int compare(String dateInString1,String dateInString2) {
        Date date1 = toDate(dateInString1);
        Date date2 = toDate(dateInString2);
        return date1.compareTo(date2);
    }
    
    public static DateAsString getInstance() {
        return DateAsStringHolder.INSTANCE;
    }

    private static class DateAsStringHolder {

        private static final DateAsString INSTANCE = new DateAsString();
    }
}
