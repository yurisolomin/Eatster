package ru.baccasoft.eatster.thrift.server;

import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.baccasoft.eatster.model.AverageCheckModel;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.CuisineModel;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.ParkingModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.model.RestaurantPhotosModel;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.eatster.service.AverageCheckService;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.service.CuisineService;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.ParkingService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.service.UserBonusService;
import ru.baccasoft.eatster.service.UserService;
import ru.baccasoft.eatster.thrift.generated.ThriftActionData;
import ru.baccasoft.eatster.thrift.generated.ThriftMyScoresData;
import ru.baccasoft.eatster.thrift.generated.ThriftOperationData;
import ru.baccasoft.eatster.thrift.generated.ThriftPaginationInfo;
import ru.baccasoft.eatster.thrift.generated.ThriftPosition;
import ru.baccasoft.eatster.thrift.generated.ThriftService;
import ru.baccasoft.eatster.thrift.generated.ThriftRestaurantData;
import ru.baccasoft.eatster.thrift.generated.ThriftRestaurantFilter;
import ru.baccasoft.eatster.thrift.generated.ThriftUserData;
import ru.baccasoft.misc.thrift.common_2015_08.generated.ThriftException;
import ru.baccasoft.misc.thrift.common_2015_08.generated.ThriftExceptionType;
import ru.baccasoft.misc.thrift.common_2015_08.generated.ThriftPingResponse;
import ru.baccasoft.misc.thrift.common_2015_08.generated.ThriftRequestCommonData;
import ru.baccasoft.misc.thrift.common_2015_08.generated.commonConstants;

public class ThriftHandler extends AbstractThriftProcessor<ThriftException, ThriftRequestCommonData> implements ThriftService.Iface {

    private static final Logger LOG = LoggerFactory.getLogger(ThriftHandler.class.getName());
//    public static final String SERVICE_VERSION = "eatster_2016-01-12.003";
    public static final String SERVICE_VERSION = "eatster_2015-12-22.002";

    private final UserService userService;
    private final PushTokenService pushTokenService;
    private final RestaurantService restaurantService;
    private final CuisineService cuisineService;
    private final ActionService actionService;
    private final PhotoService photoService;
    private final AverageCheckService averageCheckService;
    private final OperationService operationService;
    private final ParkingService parkingService;
    private final UserBonusService userBonusService;

    public ThriftHandler(UserService userService, PushTokenService pushTokenService, RestaurantService restaurantService, CuisineService cuisineService, ActionService actionService, PhotoService photoService, AverageCheckService averageCheckService, OperationService operationService, ParkingService parkingService, UserBonusService userBonusService) {
        this.userService = userService;
        this.pushTokenService = pushTokenService;
        this.restaurantService = restaurantService;
        this.cuisineService = cuisineService;
        this.actionService = actionService;
        this.photoService = photoService;
        this.averageCheckService = averageCheckService;
        this.operationService = operationService;
        this.parkingService = parkingService;
        this.userBonusService = userBonusService;
    }

    private String platformToString(String platform) {
        if (platform == null) {
            return "";
        }
        if (platform.equals(commonConstants.PLATFORM_TYPE_ANDROID)){
            return PushTokenService.PLATFORM_TYPE_ANDROID;
        }
        if (platform.equals(commonConstants.PLATFORM_TYPE_IOS)){
            return PushTokenService.PLATFORM_TYPE_IOS;
        }
        if (platform.equals(commonConstants.PLATFORM_TYPE_TEST)){
            return PushTokenService.PLATFORM_TYPE_TEST;
        }
        if (platform.equals(commonConstants.PLATFORM_TYPE_UNKNOWN)){
            return PushTokenService.PLATFORM_TYPE_UNKNOWN;
        }
        if (platform.equals(commonConstants.PLATFORM_TYPE_WIN)){
            return PushTokenService.PLATFORM_TYPE_WIN;
        }
        return "";
    }
    //проверка данных

    private void checkPlatform(ThriftRequestCommonData request) throws ThriftException, TException {
        LOG.trace("checkPlatform " + request.getClientPlatform() + "/" + request.getDeviceId() + ", version=" + request.getProtocolVersion());
        //проверим платформу
        String platform = platformToString(request.getClientPlatform());
        if (platform.isEmpty()) {
            String msg = "Fail. Available platforms: " + PushTokenService.PLATFORM_SUPPORT;
            throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, msg, "");
        }
        LOG.trace("Ok.");
    }

    private void checkVersion(ThriftRequestCommonData request) throws ThriftException, TException {
        LOG.debug("checkVersion: serverVersion="+SERVICE_VERSION+", protocolVersion=" + request.getProtocolVersion());
        if (!SERVICE_VERSION.equals(request.getProtocolVersion())) {
            LOG.warn("Service version mismatch: expected ''"+SERVICE_VERSION+"'' but received ''"+request.getProtocolVersion()+"''");
            String url = "";//getUpdateUrl(request.getClientPlatform());
            throw new ThriftException(
                    ThriftExceptionType.SERVICE_VERSION_MISMATCH,
                    "Версия приложения устарела",
                    "Service version mismatch. Expected " + SERVICE_VERSION,
                    url);
        }
        LOG.debug("Ok.");
    }

    private void saveToken(ThriftRequestCommonData request) {
        String platform = platformToString(request.getClientPlatform());
        pushTokenService.saveToken(request.getUserLogin(), request.getDeviceId(), request.getPushToken(), platform);
    }
    
    private ThriftUserData checkRequestCommonData(ThriftRequestCommonData request, boolean loginRequired, String serviceName) throws ThriftException, TException {
        LOG.debug(serviceName + ".checkRequest: clientPlatform=" + request.getClientPlatform() + ", deviceId=" + request.getDeviceId() + ", pushToken=" + request.getPushToken() + ", protocolVersion=" + request.getProtocolVersion()+", clientVersion="+request.getClientVersion());
        checkPlatform(request);
        checkVersion(request);
        if (!loginRequired) {
            LOG.debug("Ok. No auth");
            return null;
        }
        String phone = request.getUserLogin();
        LOG.debug("phone="+phone);
        UserModel user = userService.auth(phone, request.getAuthToken());
        if (user == null) {
            String msg = "Login failed. User '" + request.getUserLogin() + "', password '" + request.getAuthToken() + "'";
            LOG.warn(msg);
            throw new ThriftException(ThriftExceptionType.AUTHENTICATION_FAILED, msg, "Введенный Вами код неверен", "");
        }
        String promocode = user.getPromocode();
        if (promocode == null) {
            promocode = "";
        }
        ThriftUserData userData = new ThriftUserData(
                user.getId(),
                user.getName(),
                user.getBirthday(),
                user.getGender(),
                user.getEmail(),
                promocode, //may be null
                user.getPhone(),
                user.getFriendPromocode()
        );
        LOG.debug("Ok. Auth.user="+userData);
        return userData;
    }

    private void checkPhoneLength(String phone) throws ThriftException {
        if (phone == null) {
            phone = "";
        }
        String pattern = "\\+\\d{11}";
        if (!phone.matches(pattern)) {
            String msg = "Mask phone expected +7NNNNNNNNNN";
            LOG.warn(msg);
            throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Длина номера телефона ожидается 12 знаков (+7NNNNNNNNNN)", "");
        }
    }

    //копирование данных из массива RestaurantModel массив ThriftRestaurantData
    private void copyRestaurantArrayToThrift(List<RestaurantModel> listFrom,int offset, List<ThriftRestaurantData> listThrift) {
        LOG.debug("copyRestaurantArrayToThrift: listFrom.size="+listFrom.size()+", offset="+offset);
//        for(RestaurantModel restaurant: listFrom) {
        for(int index=offset; index<listFrom.size(); ++index) {
            RestaurantModel restaurant = listFrom.get(index);
            //кухни
            //отдадим кухни в том порядке, в каком они указаны в карточке ресторана
            long cuisineId1 = restaurant.getCuisineId1();
            Long cuisineId2 = restaurant.getCuisineId2();
            Long cuisineId3 = restaurant.getCuisineId3();
            List<String> listCuisines = new ArrayList();
            CuisineModel cuisine = cuisineService.getItem(cuisineId1);
            listCuisines.add(cuisine.getName());
            if (cuisineId2 != null) {
                cuisine = cuisineService.getItem(cuisineId2);
                listCuisines.add(cuisine.getName());
            }
            if (cuisineId3 != null) {
                cuisine = cuisineService.getItem(cuisineId3);
                listCuisines.add(cuisine.getName());
            }
            //акции
            List<ActionModel> actions = actionService.findByRestaurantPublished(restaurant.getId());
            List<ThriftActionData> listActions = new ArrayList();
            for(ActionModel action: actions) {
                listActions.add( new ThriftActionData(
                        action.getId(),
                        action.getName(),
                        action.getComment(),
                        action.getPhotoUrlParams(),
                        action.getActionType(),
                        action.getStartTime(),
                        action.getEndTime(),
                        action.getActionDays(),
                        action.getActionSubType()
                    ));
            }
            //средний чек
            AverageCheckModel averageCheck = averageCheckService.getItem(restaurant.getAverageCheckId());
            //SubwayModel subway = subwayService.getItem(restaurant.getSubwayId());
            ThriftPosition position = new ThriftPosition(restaurant.getCoordLat(),restaurant.getCoordLon());
            RestaurantPhotosModel photos = photoService.findPublishedByRestaurant(restaurant.getId());
            ParkingModel parking = parkingService.getItem(restaurant.getParkingId());
            listThrift.add( new ThriftRestaurantData(
                    restaurant.getId(),
                    restaurant.getName(),
                    listCuisines,
                    listActions,
                    averageCheck.getName(),
                    position,
                    restaurant.getDistance(),
                    restaurant.getAddress(),
                    restaurant.getInfoWorkdays(),
                    restaurant.getInfoHolidays(),
                    photos.getMainPhoto(),
                    photos.getPhotos(),
                    restaurant.getWebsite(),
                    restaurant.getDescription(),
                    restaurant.getPhone(),
                    restaurant.getSubwayName(),
                    restaurant.getEntertainments(),
                    restaurant.getPaymentCards(),
                    parking.getName(),
                    restaurant.isWifi(),
                    restaurant.isKidsMenu(),
                    photos.getLogo()
                    ));
        }
        LOG.debug("Ok. listThrift.size="+listThrift.size());
    }

    
    @Override
    public ThriftPingResponse ping(ThriftRequestCommonData request) throws ThriftException, TException {
        LOG.debug("Ping: clientPlatform=" + request.getClientPlatform() + ", deviceId=" + request.getDeviceId() + ", pushToken=" + request.getPushToken() + ", protocolVersion=" + request.getProtocolVersion()+", clientVersion="+request.getClientVersion());
        checkPlatform(request);
        checkVersion(request);
        ThriftPingResponse response = new ThriftPingResponse();
        response.setServerTimestamp(System.currentTimeMillis());
        response.setNewVersionAvailable(false);
        response.setUpdateUrl("");
        response.setUpdateMessage("");
        response.setShouldCleanClientData(false);
        LOG.debug("Ok");
        return response;
    }

    @Override
    protected String SERVICE_VERSION() {
        return SERVICE_VERSION;
    }

    @Override
    protected Logger LOG() {
        return LOG;
    }

    @Override
    public ThriftUserData login(ThriftRequestCommonData request) throws ThriftException, TException {
        LOG.debug("login: clientPlatform=" + request.getClientPlatform() + ", deviceId=" + request.getDeviceId() + ", pushToken=" + request.getPushToken() + ", protocolVersion=" + request.getProtocolVersion()+", clientVersion="+request.getClientVersion());
        ThriftUserData user = checkRequestCommonData(request, true, "login");
        if (user != null) {
            saveToken(request);
        }
        LOG.debug("Ok. User="+user);
        return user;
    }

    @Override
    public void registration(ThriftRequestCommonData request) throws ThriftException, TException {
        LOG.debug("registration: userLogin=" + request.getUserLogin());
        checkRequestCommonData(request, false, "registration");
        String phone = request.getUserLogin();
        checkPhoneLength(phone);
        try {
            userService.registration(phone);
            saveToken(request);
            LOG.debug("Ok.");
        } catch(Exception e) {
            String msg = "Error on registration";
            LOG.warn(msg+":"+e.getMessage());
            throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Ошибка при регистрации", "");
        }
    }

    @Override
    public void saveUserData(ThriftRequestCommonData request, ThriftUserData userData) throws ThriftException, TException {
        LOG.debug("saveUserData: userData="+userData);
        checkRequestCommonData(request, true, "saveUserData");
        //
        UserModel user = userService.getItem(userData.getId());
        user.setPhone(userData.getPhone());
        user.setName(userData.getName());
        user.setBirthday(userData.getBirthday());
        user.setGender(userData.getGender());
        user.setEmail(userData.getEmail());
        //
        String friendPromocodeOld = user.getFriendPromocode();
        String friendPromocodeNew = userData.getFriendPromocode();
        //если промокод не пустой
        if (!friendPromocodeNew.isEmpty()) {
            //если он отличается от того, который был
            if (!friendPromocodeNew.equals(friendPromocodeOld)) {
                //ошибка, если происходит смена существующего промокода
                if (!friendPromocodeOld.isEmpty()) {
                    String msg = "Error on change promocode. Old promocode not empty!";
                    LOG.warn(msg);
                    throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Промо-код друга уже указан. Не могу сменить промо-код друга", "");
                }
                //ищем друга
                UserModel friendUserModel = userService.getItemByPromocode(friendPromocodeNew);
                if (friendUserModel == null) {
                    String msg = "Error on set promocode. Friend not found!";
                    LOG.warn(msg);
                    throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Извините, но промо-код не верен. Пожалуйста, уточните код и продолжите регистрацию.", "");
                }
                //ошибка, если промокод друга равен личному промокоду
                String userPromocode = user.getPromocode();
                if (userPromocode == null) {
                    userPromocode = "";
                }
                if (friendPromocodeNew.equals(userPromocode)) {
                    String msg = "Error on set promocode. Friend promocode equal user promocode!";
                    LOG.warn(msg);
                    throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Извините, но промо-код не верен. Пожалуйста, уточните код и продолжите регистрацию.", "");
                }
                //данные друга
                user.setFriendPromocode(friendPromocodeNew);
                user.setFriendBonusDateEnd("");
                user.setFriendUserId(friendUserModel.getId());
                //готовим к началу бонусного периода
                user.setBonusActivated(false);
                user.setBonusDateEnd("");
            }
        }
        //promocode is readonly field
        //user.setPromocode(userData.getPromocode());
        //
        int updated = userService.updateItem(user);
        if (updated == 0) {
            String msg = "Update user failed. User not found: " + user;
            LOG.warn(msg);
            throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Ошибка сохранения профиля. Не найден пользователь.", "");
        }
        saveToken(request);
        LOG.debug("Ok.");
    }
/*
    @Override
    public List<ThriftRestaurantData> getNearestRestaurants(ThriftRequestCommonData request, double latitude, double longitude, int lastIdOfPrevFetch) throws ThriftException, TException {
        checkRequestCommonData(request,false,"getNearestRestaurants");
        //
        List<ThriftRestaurantData> listThrift =  new ArrayList();
        List<RestaurantModel> listFrom = restaurantService.findNearest(latitude,longitude, lastIdOfPrevFetch);
        copyRestaurantArrayToThrift(listFrom,listThrift);
        return listThrift;
    }
*/
    @Override
    public ThriftMyScoresData getMyScores(ThriftRequestCommonData request) throws ThriftException, TException {
        LOG.debug("getMyScores:");
        ThriftUserData user = checkRequestCommonData(request, true, "getMyScores");
        List<OperationModel> inOperations = operationService.findByUser(user.getId());
        ThriftMyScoresData myScores = new ThriftMyScoresData();
        List<ThriftOperationData> outOperations = new ArrayList();
        int iTotal = 0, iSpent = 0;
        for(OperationModel inOper: inOperations) {
            iTotal += inOper.getAddScore();
            iSpent += inOper.getDecScore();
            if (inOper.getDecScore() != 0) {
                ThriftOperationData outOper = new ThriftOperationData (
                        inOper.getOperDate(),
                        inOper.getOperTime(),
                        inOper.getRestaurantId(),
                        inOper.getRestaurantName(),
                        0,
                        -inOper.getDecScore()
                    );
                outOperations.add(outOper);
            }
            if (inOper.getAddScore() != 0) {
                ThriftOperationData outOper = new ThriftOperationData (
                        inOper.getOperDate(),
                        inOper.getOperTime(),
                        inOper.getRestaurantId(),
                        inOper.getRestaurantName(),
                        inOper.getCheckSum() - inOper.getDecScore(),
                        inOper.getAddScore()
                    );
                outOperations.add(outOper);
            }
        }
        int bonusPeriodEstimate = userBonusService.getPeriodEstimateOnCurDate(user.getId());
        myScores.setBalance(iTotal - iSpent);
        myScores.setSpent(iSpent);
        myScores.setTotal(iTotal);
        myScores.setBonusPeriodEstimate(bonusPeriodEstimate);
        myScores.setOperations(outOperations);
        LOG.debug("Ok");
        return myScores;
    }

    @Override
    public List<ThriftRestaurantData> getNearestRestaurants(ThriftRequestCommonData request, ThriftPosition currentPosition, ThriftPaginationInfo pagination, ThriftRestaurantFilter filter) throws ThriftException, TException {
        LOG.debug("getNearestRestaurants:");
        checkRequestCommonData(request,false,"getNearestRestaurants");
        //
        List<ThriftRestaurantData> listThrift =  new ArrayList();
        List<RestaurantModel> listFrom = restaurantService.findNearest(
                currentPosition.getLatitude(),
                currentPosition.getLongitude(), 
                pagination.getOffset() + pagination.getCount(), 
                filter.getSubwayId(),
                filter.getAverageCheckId(),
                filter.getActionTypeId(),
                filter.getCuisineId()
        );
        copyRestaurantArrayToThrift(listFrom,pagination.getOffset(),listThrift);
        LOG.debug("Ok");
        return listThrift;
    }
/*
    @Override
    public ThriftChangesData getChanges(ThriftRequestCommonData request, long previousTimestamp) throws ThriftException, TException {
        ThriftChangesData changes = new ThriftChangesData();
        Timestamp timestamp = new Timestamp(previousTimestamp);
        //SubwayModel
        List<SubwayModel> changesSubway = subwayService.findChanged(timestamp);
        List<ThriftSubwayData> changedSubway = new ArrayList();
        for(SubwayModel item: changesSubway) {
            changedSubway.add(new ThriftSubwayData(item.getId(),item.getName()));
        }
        changes.setChangedSubway(changedSubway);
        //AverageCheckModel
        List<AverageCheckModel> changesAverageCheck = averageCheckService.findChanged(timestamp);
        List<ThriftAverageCheckData> changedAverageCheck = new ArrayList();
        for(AverageCheckModel item: changesAverageCheck) {
            changedAverageCheck.add(new ThriftAverageCheckData(item.getId(),item.getName()));
        }
        changes.setChangedAverageCheck(changedAverageCheck);
        //ActionTypeModel
        List<ActionTypeModel> changesActionType = actionTypeService.findChanged(timestamp);
        List<ThriftActionTypeData> changedActionType = new ArrayList();
        for(ActionTypeModel item: changesActionType) {
            changedActionType.add(new ThriftActionTypeData(item.getId(),item.getName()));
        }
        changes.setChangedActionType(changedActionType);
        //CuisineModel
        List<CuisineModel> changesCuisine = cuisineService.findChanged(timestamp);
        List<ThriftCuisineData> changedCuisine = new ArrayList();
        for(CuisineModel item: changesCuisine) {
            changedCuisine.add(new ThriftCuisineData(item.getId(),item.getName()));
        }
        changes.setChangedCuisine(changedCuisine);
        //
        return changes;
    }
*/
    @Override
    public ThriftRestaurantData getRestaurant(ThriftRequestCommonData request, long restaurantId) throws ThriftException, TException {
        LOG.debug("getRestaurant: restaurantId="+restaurantId);
        checkRequestCommonData(request,false,"getRestaurant");
        //
        RestaurantModel restaurantModel = restaurantService.getItem(restaurantId);
        if (restaurantModel == null) {
            String msg = "Restaurant not found by restaurantId="+restaurantId;
            LOG.warn(msg);
            throw new ThriftException(ThriftExceptionType.UNDEFINED, msg, "Ресторан по идентификатору "+restaurantId+" не найден", "");
        }
        List<ThriftRestaurantData> listThrift =  new ArrayList();
        List<RestaurantModel> listFrom = new ArrayList();
        listFrom.add(restaurantModel);
        copyRestaurantArrayToThrift(listFrom,0,listThrift);
        LOG.debug("Ok");
        return listThrift.get(0);
    }

    @Override
    public List<ThriftRestaurantData> getRestaurants(ThriftRequestCommonData request, List<Long> restaurantIds) throws ThriftException, TException {
        LOG.debug("getRestaurants: restaurantIds.size="+restaurantIds.size());
        checkRequestCommonData(request,false,"getRestaurants");
        //
        List<RestaurantModel> listFrom = new ArrayList();
        for(Long restaurantId:restaurantIds) {
            RestaurantModel restaurantModel = restaurantService.getItem(restaurantId);
            if (restaurantModel == null) {
                String msg = "Restaurant not found by restaurantId="+restaurantId;
                LOG.warn(msg);
                continue;
            }
        listFrom.add(restaurantModel);
        }
        List<ThriftRestaurantData> listThrift =  new ArrayList();
        copyRestaurantArrayToThrift(listFrom,0,listThrift);
        LOG.debug("Ok. return size="+listThrift.size());
        return listThrift;
    }
    
}
