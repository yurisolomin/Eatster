package ru.baccasoft.eatster.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class RestaurantModel extends CommonNamedModel {
    public static final String STAT_ACTIVE = "active";
    public static final String STAT_INACTIVE = "inactive";
    public static final String STAT_UNAUTH = "unauthorized";
    private static final String STAT_ACTIVE_RUS = "Активно";
    private static final String STAT_INACTIVE_RUS = "Не активно";
    private static final String STAT_UNAUTH_RUS = "Неавторизированное";
    
    private String description;
    private String status;//active,inactive,authemail
    private Long cuisineId1;
    private Long cuisineId2;
    private Long cuisineId3;
    private String address;
    private double coordLat;
    private double coordLon;
    private int distance; //readonly
    private Long subwayId;
    private String phone;
    private String website;
    private Long averageCheckId;
    private boolean wifi;
    private Long parkingId;
    private boolean kidsMenu;
    private String entertainments;//128
    private boolean cardsVisa;
    private boolean cardsMasterCard;
    private boolean cardsMaestro;
    private boolean cardsUnionPay;
    private boolean cardsVisaElectron;
    private boolean cardsAmericanExpress;
    private boolean cardsDinersClub;
    private boolean cardsPro100;
    private boolean cardsJCB;
    private boolean cardsMIR;

    private boolean workdayMonday;
    private boolean workdayTuesday;
    private boolean workdayWednesday;
    private boolean workdayThursday;
    private boolean workdayFriday;
    private boolean workdaySaturday;
    private boolean workdaySunday;
    private String workdayStartTime;
    private String workdayEndTime;
    private String holidayStartTime;
    private String holidayEndTime;

    private String legalName;
    private String legalAddress;
    private String director;
    private String OGRN;
    private String INN;
    private String KPP;
    private String bank;
    private String BIC;
    private String custAccount;
    private String corrAccount;

    private long partnerId;
    private String subwayName; //из связанной таблицы

    public static class RestaurantStatus {
        String id;
        String name;
        public RestaurantStatus(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    public RestaurantModel() {
        super();
        description = "";
        status = "";
        cuisineId1 = null;
        cuisineId2 = null;
        cuisineId3 = null;
        address = "";
        coordLat = 0;
        coordLon = 0;
        distance = 0;
        subwayId = null;
        phone = "";
        website = "";
        averageCheckId = null;
        wifi = false;
        parkingId = null;
        kidsMenu = false;
        entertainments = "";//128
        cardsVisa = false;
        cardsMasterCard = false;
        cardsMaestro = false;
        cardsUnionPay = false;
        cardsVisaElectron = false;
        cardsAmericanExpress = false;
        cardsDinersClub = false;
        cardsPro100 = false;
        cardsJCB = false;
        cardsMIR = false;
//        infoWorkdays = "";
//        infoHolidays = "";
        workdaySunday = false;
        workdayMonday = false;
        workdayTuesday = false;
        workdayWednesday = false;
        workdayThursday = false;
        workdayFriday = false;
        workdaySaturday = false;
        workdayStartTime = "";
        workdayEndTime = "";
        holidayStartTime = "";
        holidayEndTime = "";
        //Юридические данные
        legalName = "";
        legalAddress = "";
        director = "";
        OGRN = "";
        INN = "";
        KPP = "";
        bank = "";
        BIC = "";
        custAccount = "";
        corrAccount = "";
/*        //Контактные данные
        contactName = "";
        contactPost = "";
        contactEmail = "";
        contactNote = "";
        contactPhone = "";
        //авторизация
        userName = "";
        userPassword = "";
        userEmail = "";
*/
        partnerId = 0;
        subwayName = "";
    }

    public boolean isCardsAmericanExpress() {
        return cardsAmericanExpress;
    }

    public void setCardsAmericanExpress(boolean cardsAmericanExpress) {
        this.cardsAmericanExpress = cardsAmericanExpress;
    }

    public boolean isCardsDinersClub() {
        return cardsDinersClub;
    }

    public void setCardsDinersClub(boolean cardsDinersClub) {
        this.cardsDinersClub = cardsDinersClub;
    }

    public boolean isCardsPro100() {
        return cardsPro100;
    }

    public void setCardsPro100(boolean cardsPro100) {
        this.cardsPro100 = cardsPro100;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCuisineId1() {
        return cuisineId1;
    }

    public void setCuisineId1(Long cuisineId1) {
        this.cuisineId1 = cuisineId1;
    }

    public Long getCuisineId2() {
        return cuisineId2;
    }

    public void setCuisineId2(Long cuisineId2) {
        this.cuisineId2 = cuisineId2;
    }

    public Long getCuisineId3() {
        return cuisineId3;
    }

    public void setCuisineId3(Long cuisineId3) {
        this.cuisineId3 = cuisineId3;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCoordLat() {
        return coordLat;
    }

    public void setCoordLat(double coordLat) {
        this.coordLat = coordLat;
    }

    public double getCoordLon() {
        return coordLon;
    }

    public void setCoordLon(double coordLon) {
        this.coordLon = coordLon;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Long getSubwayId() {
        return subwayId;
    }

    public void setSubwayId(Long subwayId) {
        this.subwayId = subwayId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getAverageCheckId() {
        return averageCheckId;
    }

    public void setAverageCheckId(Long averageCheckId) {
        this.averageCheckId = averageCheckId;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public Long getParkingId() {
        return parkingId;
    }

    public void setParkingId(Long parkingId) {
        this.parkingId = parkingId;
    }

    public boolean isKidsMenu() {
        return kidsMenu;
    }

    public void setKidsMenu(boolean kidsMenu) {
        this.kidsMenu = kidsMenu;
    }

    public String getEntertainments() {
        return entertainments;
    }

    public void setEntertainments(String entertainments) {
        this.entertainments = entertainments;
    }

    public boolean isCardsVisa() {
        return cardsVisa;
    }

    public void setCardsVisa(boolean cardsVisa) {
        this.cardsVisa = cardsVisa;
    }

    public boolean isCardsMasterCard() {
        return cardsMasterCard;
    }

    public void setCardsMasterCard(boolean cardsMasterCard) {
        this.cardsMasterCard = cardsMasterCard;
    }

    public boolean isCardsMaestro() {
        return cardsMaestro;
    }

    public void setCardsMaestro(boolean cardsMaestro) {
        this.cardsMaestro = cardsMaestro;
    }

    public boolean isCardsUnionPay() {
        return cardsUnionPay;
    }

    public void setCardsUnionPay(boolean cardsUnionPay) {
        this.cardsUnionPay = cardsUnionPay;
    }

    public boolean isCardsVisaElectron() {
        return cardsVisaElectron;
    }

    public void setCardsVisaElectron(boolean cardsVisaElectron) {
        this.cardsVisaElectron = cardsVisaElectron;
    }

    public boolean isWorkdaySunday() {
        return workdaySunday;
    }

    public void setWorkdaySunday(boolean workdaySunday) {
        this.workdaySunday = workdaySunday;
    }

    public boolean isWorkdayMonday() {
        return workdayMonday;
    }

    public void setWorkdayMonday(boolean workdayMonday) {
        this.workdayMonday = workdayMonday;
    }

    public boolean isWorkdayTuesday() {
        return workdayTuesday;
    }

    public void setWorkdayTuesday(boolean workdayTuesday) {
        this.workdayTuesday = workdayTuesday;
    }

    public boolean isWorkdayWednesday() {
        return workdayWednesday;
    }

    public void setWorkdayWednesday(boolean workdayWednesday) {
        this.workdayWednesday = workdayWednesday;
    }

    public boolean isWorkdayThursday() {
        return workdayThursday;
    }

    public void setWorkdayThursday(boolean workdayThursday) {
        this.workdayThursday = workdayThursday;
    }

    public boolean isWorkdayFriday() {
        return workdayFriday;
    }

    public void setWorkdayFriday(boolean workdayFriday) {
        this.workdayFriday = workdayFriday;
    }

    public boolean isWorkdaySaturday() {
        return workdaySaturday;
    }

    public void setWorkdaySaturday(boolean workdaySaturday) {
        this.workdaySaturday = workdaySaturday;
    }

    public String getWorkdayStartTime() {
        return workdayStartTime;
    }

    public void setWorkdayStartTime(String workdayStartTime) {
        this.workdayStartTime = workdayStartTime;
    }

    public String getWorkdayEndTime() {
        return workdayEndTime;
    }

    public void setWorkdayEndTime(String workdayEndTime) {
        this.workdayEndTime = workdayEndTime;
    }

    public String getHolidayStartTime() {
        return holidayStartTime;
    }

    public void setHolidayStartTime(String holidayStartTime) {
        this.holidayStartTime = holidayStartTime;
    }

    public String getHolidayEndTime() {
        return holidayEndTime;
    }

    public void setHolidayEndTime(String holidayEndTime) {
        this.holidayEndTime = holidayEndTime;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getOGRN() {
        return OGRN;
    }

    public void setOGRN(String OGRN) {
        this.OGRN = OGRN;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getKPP() {
        return KPP;
    }

    public void setKPP(String KPP) {
        this.KPP = KPP;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public String getCustAccount() {
        return custAccount;
    }

    public void setCustAccount(String custAccount) {
        this.custAccount = custAccount;
    }

    public String getCorrAccount() {
        return corrAccount;
    }

    public void setCorrAccount(String corrAccount) {
        this.corrAccount = corrAccount;
    }

    public List<String> getPaymentCards() {
        List<String> list = new ArrayList();
        if (cardsVisa) {
            list.add("Visa");
        }
        if (cardsMasterCard) {
            list.add("MasterCard");
        }
        if (cardsMaestro) {
            list.add("Maestro");
        }
        if (cardsUnionPay) {
            list.add("UnionPay");
        }
        if (cardsVisaElectron) {
            list.add("VisaElectron");
        }
        if (cardsAmericanExpress) {
            list.add("AmericanExpress");
        }
        if (cardsDinersClub) {
            list.add("DinersClub");
        }
        if (cardsPro100) {
            list.add("Pro100");
        }
        if (cardsJCB) {
            list.add("JCB");
        }
        if (cardsMIR) {
            list.add("МИР");
        }
        return list;
    }

    // Дни недели в акции. Битовая маска (понедельник и суббота = 2 + 128 = 130)
    // 2 - понедельник
    // 4 - вторник
    // 8 - среда
    // 16 - четверг
    // 32 - пятница
    // 64 - суббота
    // 128 - воскресенье
    // 256 - зарезервировано на случай 32го мая
/*    
    public int getWorkDays() {
        int mask = 0;
        if (workdayMonday) {
            mask += 2;
        }
        if (workdayTuesday) {
            mask += 4;
        }
        if (workdayWednesday) {
            mask += 8;
        }
        if (workdayThursday) {
            mask += 16;
        }
        if (workdayFriday) {
            mask += 32;
        }
        if (workdaySaturday) {
            mask += 64;
        }
        if (workdaySunday) {
            mask += 128;
        }
        return mask;
    }
*/
    private String buildInfoWorkDays() {
        int workdays = 0;
        if (workdayMonday) {
            workdays += 2;
        }
        if (workdayTuesday) {
            workdays += 4;
        }
        if (workdayWednesday) {
            workdays += 8;
        }
        if (workdayThursday) {
            workdays += 16;
        }
        if (workdayFriday) {
            workdays += 32;
        }
        //
        String[] weekday = {"Пн", "Вт", "Ср", "Чт", "Пт"};
        int[] weekbit = {2, 4, 8, 16, 32};
        //найдем первую рабочую дату
        String info = "";
        int countWorkDays = 0;
        int index = 0;
        for (; index<weekday.length; ++index) {
            boolean workday = ((workdays & weekbit[index]) != 0);
            if (workday) {
                ++countWorkDays;
                //если подряд идет несколько рабочих дней
                if (countWorkDays > 1) {
                    continue;
                }
                if (!info.isEmpty()) {
                    info += ",";
                }
                info += weekday[index];
                continue;
            }
            //если встретился выходной
            //если подряд не было несколько рабочих дней
            if (countWorkDays <= 1) {
                countWorkDays = 0;
                continue;
            }
            info += "-" + weekday[index - 1];
            countWorkDays = 0;
        }
        if (countWorkDays > 1) {
            info += "-" + weekday[index - 1];
        }
        return info;
    }
  /*  
    private String getInfoDays(boolean forWorkDays) {
        int workdays = getWorkDays();
        String[] weekday = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
        int[] weekbit = {2, 4, 8, 16, 32, 64, 128};
        //найдем первую рабочую дату
        String info = "";
        int countWorkDays = 0;
        int index = 0;
        for (; index<7; ++index) {
            boolean workday = ((workdays & weekbit[index]) != 0);
            if (workday == forWorkDays) {
//            if ((workdays & weekbit[index]) != 0) {
                ++countWorkDays;
                //если подряд идет несколько рабочих дней
                if (countWorkDays > 1) {
                    continue;
                }
                if (!info.isEmpty()) {
                    info += ",";
                }
                info += weekday[index];
                continue;
            }
            //если встретился выходной
            //если подряд не было несколько рабочих дней
            if (countWorkDays <= 1) {
                countWorkDays = 0;
                continue;
            }
            info += "-" + weekday[index - 1];
            countWorkDays = 0;
        }
        if (countWorkDays > 1) {
            info += "-" + weekday[index - 1];
        }
        return info;
    }
*/    
    private static String buildInfoTime(String startTime, String endTime) {
        String midnight = "00:00";
        if (midnight.equals(startTime) && midnight.equals(endTime)) {
            return "круглосуточно";
        }
        return startTime+"-"+endTime;
    }
    
    public String getInfoWorkdays() {
        String result = buildInfoWorkDays();
        if (result.isEmpty()) {
            return "";
        }
        if (workdayStartTime.isEmpty() && workdayEndTime.isEmpty()) {
            return result;
        }
        return  result + " "+buildInfoTime(workdayStartTime,workdayEndTime);
    }
    
    public String getInfoHolidays() {
        String result = "";
        if (workdaySaturday) {
            result = "Сб";
        }
        if (workdaySunday) {
            if (!result.isEmpty()) {
                result += "-";
            }
            result += "Вс";
        }
        if (result.isEmpty()) {
            return "";
        }
        if (holidayStartTime.isEmpty() && holidayEndTime.isEmpty()) {
            return result;
        }
        return  result + " "+buildInfoTime(holidayStartTime,holidayEndTime);
    }
/*    
    private char decimalSeparator() {
        DecimalFormat format=(DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols=format.getDecimalFormatSymbols();
        char sep=symbols.getDecimalSeparator();
        return sep;
    }
    
    private String coordToString(double coord) {
        String result = String.format("%.6f",coord);
        result = result.replace(decimalSeparator(),'.');
        return result;
    }
    
    private double coordToDoubleDef(String coordAsString, double defValue) {
        coordAsString = coordAsString.trim();
        coordAsString = coordAsString.replace('.',decimalSeparator());
        coordAsString = coordAsString.replace(',',decimalSeparator());
        double result;
        try {
            result = Double.parseDouble(coordAsString);
        } catch (Exception e) {
            result = defValue;
        }
        return result;
    }
    
    public String getCoordLatAsString() {
        return coordToString(coordLat);
    }
    public void setCoordLatAsString(String coordAsString) {
        coordLat = coordToDoubleDef(coordAsString,coordLat);
    }
    
    public String getCoordLonAsString() {
        return coordToString(coordLon);
    }
    public void setCoordLonAsString(String coordAsString) {
        coordLon = coordToDoubleDef(coordAsString,coordLon);
    }
*/    
    public static List<RestaurantStatus> getStatusList() {
        List<RestaurantStatus> list = new ArrayList();
        list.add(new RestaurantStatus(STAT_ACTIVE,STAT_ACTIVE_RUS));
        list.add(new RestaurantStatus(STAT_INACTIVE,STAT_INACTIVE_RUS));
        list.add(new RestaurantStatus(STAT_UNAUTH,STAT_UNAUTH_RUS));
        return list;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public String getSubwayName() {
        return subwayName;
    }

    public void setSubwayName(String subwayName) {
        this.subwayName = subwayName;
    }

    public boolean isCardsJCB() {
        return cardsJCB;
    }

    public void setCardsJCB(boolean cardsJCB) {
        this.cardsJCB = cardsJCB;
    }

    public boolean isCardsMIR() {
        return cardsMIR;
    }

    public void setCardsMIR(boolean cardsMIR) {
        this.cardsMIR = cardsMIR;
    }
    
    
}
