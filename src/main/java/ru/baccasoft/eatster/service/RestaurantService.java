package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class RestaurantService {

    private static final Logger LOG = Logger.getLogger(RestaurantService.class);

    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "restaurant";
    private static final String SQL_DATA_COLUMNS
            = "deleted,name,description,status,cuisine_id1,cuisine_id2,cuisine_id3,address,"
            + "coord_lat,coord_lon,subway_id,phone,website,averagecheck_id,"
            + "wifi,parking_id,kids_menu,entertainments,"
            + "cards_visa,cards_mastercard,cards_maestro,cards_unionpay,cards_visaelectron,cards_americanexpress,cards_dinersclub,cards_pro100,cards_jcb,cards_mir,"
            + "workday_starttime,workday_endtime,holiday_starttime,holiday_endtime,"
            + "workday_monday,workday_tuesday,workday_wednesday,workday_thursday,workday_friday,workday_saturday,workday_sunday,"
            + "legal_name,legal_address,director,OGRN,INN,KPP,bank,BIC,cust_account,corr_account"
            + ",partner_id"
            ;
    private static final String SQL_SELECT_COLUMNS = "id,"+SQL_DATA_COLUMNS+ ",(select name from subway where subway.id = restaurant.subway_id) as subwayName";
    private static final String SQL_SELECT_ITEM = "select " + SQL_SELECT_COLUMNS + ", 0 as distance from " + SQL_TABLE + " where id = ? and deleted = false";
    private static final String SQL_SELECT_NEAREST = "select " + SQL_SELECT_COLUMNS + ", fnCalcDistance(coord_lat,coord_lon,?,?) as distance from " + SQL_TABLE
            + " where deleted = false and status = ?";
    private static final String SQL_UPDATE_ITEM = "update "+SQL_TABLE+" set modified=now(),"
            + "deleted=?,name=?,description=?,status=?,cuisine_id1=?,cuisine_id2=?,cuisine_id3=?,address=?,"
            + "coord_lat=?,coord_lon=?,subway_id=?,phone=?,website=?,averagecheck_id=?,"
            + "wifi=?,parking_id=?,kids_menu=?,entertainments=?,"
            + "cards_visa=?,cards_mastercard=?,cards_maestro=?,cards_unionpay=?,cards_visaelectron=?,cards_americanexpress=?,cards_dinersclub=?,cards_pro100=?,cards_jcb=?,cards_mir=?,"
            + "workday_starttime=?,workday_endtime=?,holiday_starttime=?,holiday_endtime=?,"
            + "workday_monday=?,workday_tuesday=?,workday_wednesday=?,workday_thursday=?,workday_friday=?,workday_saturday=?,workday_sunday=?,"
            + "legal_name=?,legal_address=?,director=?,OGRN=?,INN=?,KPP=?,bank=?,BIC=?,cust_account=?,corr_account=?"
            + " where id = ?";

    private static final String SQL_INSERT = "insert into "+SQL_TABLE+" (modified,"+SQL_DATA_COLUMNS+") values(now()"
            + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_SELECT_ALL = "select "+SQL_SELECT_COLUMNS+", 0 as distance from "+SQL_TABLE+" where deleted = false order by name";
    private static final String SQL_DELETE_ITEM = "update " + SQL_TABLE + " set modified=now(),deleted=true where id = ?";
    private static final String SQL_SELECT_BY_PARTNER = "select " + SQL_SELECT_COLUMNS + ", 0 as distance from " + SQL_TABLE + " where partner_id = ? and deleted = false order by name";
    private static final String SQL_SELECT_BY_NAME = "select " + SQL_SELECT_COLUMNS + ", 0 as distance from " + SQL_TABLE + " where trim(upper(name)) = trim(upper(?))";
/*
    private static final String TAG_ID_LIST = "#ID_LIST";
    private static final String SQL_SELECT_ITEMS = "select " + SQL_SELECT_COLUMNS + ", 0 as distance from " + SQL_TABLE + " where id in ("+TAG_ID_LIST+") and deleted = false";
*/    
//    private static final String SQL_SELECT_BY_USEREMAIL = "select " + SQL_SELECT_COLUMNS + ", 0 as distance from " + SQL_TABLE + " where user_email = ? and deleted = false";
//    private static final String SQL_SELECT_BY_USERNAME = "select " + SQL_SELECT_COLUMNS + ", 0 as distance from " + SQL_TABLE + " where user_name = ? and deleted = false";
    private static final RestaurantMapper MAPPER = new RestaurantMapper();
//    private static final IdMapper ID_MAPPER = new IdMapper();

    @Autowired
    MailService mailService;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    private static final class RestaurantMapper implements RowMapper<RestaurantModel> {

        @Override
        public RestaurantModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            RestaurantModel item = new RestaurantModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setDescription(rs.getString(++index));
            item.setStatus(rs.getString(++index));
            item.setCuisineId1(rs.getLong(++index));
            item.setCuisineId2(rs.getLong(++index));
            if (rs.wasNull()) {
                item.setCuisineId2(null);
            }
            item.setCuisineId3(rs.getLong(++index));
            if (rs.wasNull()) {
                item.setCuisineId3(null);
            }
            item.setAddress(rs.getString(++index));
            item.setCoordLat(rs.getDouble(++index));
            item.setCoordLon(rs.getDouble(++index));
            item.setSubwayId(rs.getLong(++index));
            item.setPhone(rs.getString(++index));
            item.setWebsite(rs.getString(++index));
            item.setAverageCheckId(rs.getLong(++index));
            item.setWifi(rs.getBoolean(++index));
            item.setParkingId(rs.getLong(++index));
            item.setKidsMenu(rs.getBoolean(++index));
            item.setEntertainments(rs.getString(++index));
            item.setCardsVisa(rs.getBoolean(++index));
            item.setCardsMasterCard(rs.getBoolean(++index));
            item.setCardsMaestro(rs.getBoolean(++index));
            item.setCardsUnionPay(rs.getBoolean(++index));
            item.setCardsVisaElectron(rs.getBoolean(++index));
            item.setCardsAmericanExpress(rs.getBoolean(++index));
            item.setCardsDinersClub(rs.getBoolean(++index));
            item.setCardsPro100(rs.getBoolean(++index));
            item.setCardsJCB(rs.getBoolean(++index));
            item.setCardsMIR(rs.getBoolean(++index));
            item.setWorkdayStartTime(rs.getString(++index));
            item.setWorkdayEndTime(rs.getString(++index));
            item.setHolidayStartTime(rs.getString(++index));
            item.setHolidayEndTime(rs.getString(++index));
            item.setWorkdayMonday(rs.getBoolean(++index));
            item.setWorkdayTuesday(rs.getBoolean(++index));
            item.setWorkdayWednesday(rs.getBoolean(++index));
            item.setWorkdayThursday(rs.getBoolean(++index));
            item.setWorkdayFriday(rs.getBoolean(++index));
            item.setWorkdaySaturday(rs.getBoolean(++index));
            item.setWorkdaySunday(rs.getBoolean(++index));
            item.setLegalName(rs.getString(++index));
            item.setLegalAddress(rs.getString(++index));
            item.setDirector(rs.getString(++index));
            item.setOGRN(rs.getString(++index));
            item.setINN(rs.getString(++index));
            item.setKPP(rs.getString(++index));
            item.setBank(rs.getString(++index));
            item.setBIC(rs.getString(++index));
            item.setCustAccount(rs.getString(++index));
            item.setCorrAccount(rs.getString(++index));
            item.setPartnerId(rs.getInt(++index));
            item.setSubwayName(rs.getString(++index));
            //дистанция всегда последняя
            item.setDistance(rs.getInt(++index));
            return item;
        }
    }

    @Transactional(readOnly=true)
    public List<RestaurantModel> findByPartner(long partnerId) {
        LOG.debug("findByPartner: partnerId={0}",partnerId);
        List<RestaurantModel> list = jdbc.query(SQL_SELECT_BY_PARTNER, new Object[]{partnerId}, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    
    /*
    private static final class IdMapper implements RowMapper<Long> {
        @Override
        public Long mapRow(ResultSet rs, int i) throws SQLException {
            Long id = rs.getLong(1);
            return id;
        }
    }	
     */
 /*    
    private static final String SQL_SELECT_NEAREST_ID = "select id, fnCalcDistance(coord_lat,coord_lon,?,?) as distance from "+SQL_TABLE
                +" where deleted = false"
                +" order by distance,id";
    private static final String SQL_SELECT_NEAREST = "select "+SQL_COLUMNS+", fnCalcDistance(coord_lat,coord_lon,?,?) as distance from "+SQL_TABLE
                +" where deleted = false";
    
    @Transactional(readOnly=true)
    public List<RestaurantModel> findNearest(double coord_lat, double coord_lon, int offset, int count) {
        LOG.debug("findNearest: coord_lat={0},coord_lon={1},offset={2},count={3}",coord_lat,coord_lon,offset,count);
        int limit = offset + count;
        String SelectIds = SQL_SELECT_NEAREST_ID + " limit " + limit;
        LOG.debug("query select ids={0}",SelectIds);
        List<Long> listIds = jdbc.query(SelectIds, new Object[]{coord_lat,coord_lon}, ID_MAPPER);
        if (listIds.isEmpty()) {
            return new ArrayList();
        }
        LOG.debug("return items={0}",listIds.size());
        String SelectNearest = SQL_SELECT_NEAREST;
        String separator = " and id in (";
        for(int index=offset; index<offset+count; ++index) {
            if (index >= listIds.size()) {
                break;
            }
            SelectNearest += separator;
            SelectNearest += listIds.get(index);
            separator = ",";
        }
        SelectNearest += ") order by distance,id";
        LOG.debug("query text={0}",SelectNearest);
        List<RestaurantModel> listRest = jdbc.query(SelectNearest, new Object[]{coord_lat,coord_lon},MAPPER);
        LOG.debug("Ok. return items {0}",listRest.size());
        return listRest;
    }
     */
    @Transactional(readOnly = true)
    public List<RestaurantModel> findNearest(double coord_lat, double coord_lon, int limit, long subwayId, long averageCheckId, long actionTypeId, long cuisineId) {
        LOG.debug("findNearest: coord_lat={0},coord_lon={1},limit={2}", coord_lat, coord_lon, limit);
        String QueryText = SQL_SELECT_NEAREST;
        if (subwayId > 0) {
            QueryText += " and subway_id = " + subwayId;
        }
        if (averageCheckId > 0) {
            QueryText += " and averagecheck_id = " + averageCheckId;
        }
        if (cuisineId > 0) {
            QueryText += " and " + cuisineId + " in (cuisine_id1,cuisine_id2,cuisine_id3)";
        }
        if (actionTypeId > 0) {
            QueryText += " and id in (select restaurant_id from action where actiontype_id = " + actionTypeId + " and status = '" + ActionModel.STAT_PUBLISHED + "' and deleted = false)";
        }
        QueryText += " order by distance,id limit " + limit;
        LOG.debug("query text={0}", QueryText);
        List<RestaurantModel> list = jdbc.query(QueryText, new Object[]{coord_lat, coord_lon, RestaurantModel.STAT_ACTIVE}, MAPPER);
        LOG.debug("Ok. return items {0}", list.size());
        return list;
    }

    @Transactional(readOnly=true)
    public List<RestaurantModel> findAll() {
        LOG.debug("findAll:");
        List<RestaurantModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly = true)
    public RestaurantModel getItem(long id) {
        LOG.debug("getItem: id={0}", id);
        List<RestaurantModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER);
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}", list.get(0));
            return list.get(0);
        }
        LOG.debug("Fail. item not found");
        return null;
    }

    @Transactional(readOnly = true)
    public RestaurantModel getItem(String name) {
        LOG.debug("getItem: name={0}", name);
        List<RestaurantModel> list = jdbc.query(SQL_SELECT_BY_NAME, new Object[]{name}, MAPPER);
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}", list.get(0));
            return list.get(0);
        }
        LOG.debug("Fail. item not found");
        return null;
    }
    
    @Transactional
    private long insertItem(final RestaurantModel item) {
        LOG.debug("insertItem: item={0}", item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update( new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
                int index = 0;
                ps.setBoolean(++index, item.isDeleted());
                ps.setString(++index, item.getName());
                ps.setString(++index, item.getDescription());
                ps.setString(++index, item.getStatus());
                ps.setLong(++index, item.getCuisineId1());
                ps.setObject(++index, item.getCuisineId2());
                ps.setObject(++index, item.getCuisineId3());
                ps.setString(++index, item.getAddress());
                ps.setDouble(++index, item.getCoordLat());
                ps.setDouble(++index, item.getCoordLon());
                ps.setLong(++index, item.getSubwayId());
                ps.setString(++index, item.getPhone());
                ps.setString(++index, item.getWebsite());
                ps.setLong(++index, item.getAverageCheckId());
                ps.setBoolean(++index, item.isWifi());
                ps.setLong(++index, item.getParkingId());
                ps.setBoolean(++index, item.isKidsMenu());
                ps.setString(++index, item.getEntertainments());
                ps.setBoolean(++index, item.isCardsVisa());
                ps.setBoolean(++index, item.isCardsMasterCard());
                ps.setBoolean(++index, item.isCardsMaestro());
                ps.setBoolean(++index, item.isCardsUnionPay());
                ps.setBoolean(++index, item.isCardsVisaElectron());
                ps.setBoolean(++index, item.isCardsAmericanExpress());
                ps.setBoolean(++index, item.isCardsDinersClub());
                ps.setBoolean(++index, item.isCardsPro100());
                ps.setBoolean(++index, item.isCardsJCB());
                ps.setBoolean(++index, item.isCardsMIR());
                ps.setString(++index, item.getWorkdayStartTime());
                ps.setString(++index, item.getWorkdayEndTime());
                ps.setString(++index, item.getHolidayStartTime());
                ps.setString(++index, item.getHolidayEndTime());
                ps.setBoolean(++index, item.isWorkdayMonday());
                ps.setBoolean(++index, item.isWorkdayTuesday());
                ps.setBoolean(++index, item.isWorkdayWednesday());
                ps.setBoolean(++index, item.isWorkdayThursday());
                ps.setBoolean(++index, item.isWorkdayFriday());
                ps.setBoolean(++index, item.isWorkdaySaturday());
                ps.setBoolean(++index, item.isWorkdaySunday());
                ps.setString(++index, item.getLegalName());
                ps.setString(++index, item.getLegalAddress());
                ps.setString(++index, item.getDirector());
                ps.setString(++index, item.getOGRN());
                ps.setString(++index, item.getINN());
                ps.setString(++index, item.getKPP());
                ps.setString(++index, item.getBank());
                ps.setString(++index, item.getBIC());
                ps.setString(++index, item.getCustAccount());
                ps.setString(++index, item.getCorrAccount());
                ps.setLong(++index, item.getPartnerId());
                return ps;
            }
        },
                keyHolder);
        LOG.debug("Ok. return id={0}", keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }
    
    @Transactional
    public int updateItem(RestaurantModel item) {
        LOG.debug("updateItem: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_ITEM, 
                new Object[]{
                    item.isDeleted(),
                    item.getName(),
                    item.getDescription(),
                    item.getStatus(),
                    item.getCuisineId1(),
                    item.getCuisineId2(),
                    item.getCuisineId3(),
                    item.getAddress(),
                    item.getCoordLat(),
                    item.getCoordLon(),
                    item.getSubwayId(),
                    item.getPhone(),
                    item.getWebsite(),
                    item.getAverageCheckId(),
                    item.isWifi(),
                    item.getParkingId(),
                    item.isKidsMenu(),
                    item.getEntertainments(),
                    item.isCardsVisa(),
                    item.isCardsMasterCard(),
                    item.isCardsMaestro(),
                    item.isCardsUnionPay(),
                    item.isCardsVisaElectron(),
                    item.isCardsAmericanExpress(),
                    item.isCardsDinersClub(),
                    item.isCardsPro100(),
                    item.isCardsJCB(),
                    item.isCardsMIR(),
                    item.getWorkdayStartTime(),
                    item.getWorkdayEndTime(),
                    item.getHolidayStartTime(),
                    item.getHolidayEndTime(),
                    item.isWorkdayMonday(),
                    item.isWorkdayTuesday(),
                    item.isWorkdayWednesday(),
                    item.isWorkdayThursday(),
                    item.isWorkdayFriday(),
                    item.isWorkdaySaturday(),
                    item.isWorkdaySunday(),
                    item.getLegalName(),
                    item.getLegalAddress(),
                    item.getDirector(),
                    item.getOGRN(),
                    item.getINN(),
                    item.getKPP(),
                    item.getBank(),
                    item.getBIC(),
                    item.getCustAccount(),
                    item.getCorrAccount(),
/*
                    item.getContactName(),
                    item.getContactPost(),
                    item.getContactEmail(),
                    item.getContactNote(),
                    item.getContactPhone(),
                    item.getUserName(),
                    item.getUserPassword(),
                    item.getUserEmail(),
*/                    
                    item.getId()
                } 
        );
        LOG.debug("Ok. return {0}",result);
        return result;
    }
    
    @Transactional
    public int deleteItem(long id) {
        LOG.debug("deleteItem: id={0}", id);
        int result = jdbc.update(SQL_DELETE_ITEM, new Object[]{id});
        LOG.debug("Ok. return {0}", result);
        return result;
    }
    
/*    
    @Transactional(readOnly = true)
    public List<RestaurantModel> getItems(List<Long> listId) {
        LOG.debug("getItems: listId.size={0}",listId.size());
        String ids = "0";
        for(Long id: listId) {
            ids += ","+id;
        }
        String query = SQL_SELECT_ITEMS.replace(TAG_ID_LIST,ids);
        List<RestaurantModel> list = jdbc.query(query, MAPPER);
        LOG.debug("Ok.");
        return list;
    }
*/    
/*
    @Transactional(readOnly = true)
    public RestaurantModel auth(String user_email, String user_password) {
        LOG.debug("auth: user_email={0},user_password={1}", user_email, user_password);
        if (user_email == null || user_password == null) {
            LOG.debug("Fail. user_email or password is invalid");
            return null;
        }
        List<RestaurantModel> list = jdbc.query(SQL_SELECT_BY_USEREMAIL, new Object[]{user_email}, MAPPER);
        if (list.isEmpty()) {
            LOG.debug("Fail. e-mail not found");
            return null;
        }
        if (list.size() > 1) {
            LOG.warn("Fail. Found users={0}", list.size());
            return null;
        }
        RestaurantModel item = list.get(0);
        LOG.debug("item found={0}", item);
        //если регистрация была то пароль должен совпадать
        if (!item.getUserPassword().equals(user_password)) {
            LOG.debug("Fail. Bad password");
            return null;
        }
        LOG.debug("Ok.");
        return item;
    }
*/
    @Transactional
    public long registrationItem(String partnerEMail,final RestaurantModel item) {
        LOG.debug("registrationItem. item={0}",item);
        long insertedId = insertItem(item);
        LOG.debug("insertedId={0}",insertedId);
//        if (!mailService.send(partnerEMail,"Регистрация на Eatster","Ваша регистрация прошла успешно.")) {
        if (!mailService.sendDetailsOnRestaurantRegistration(partnerEMail)) {
            LOG.debug("Fail. Mail not sent.");
            throw new RuntimeException("Error on restaurant registration!");
        }
        LOG.debug("Ok.");
        return insertedId;
    }

}
