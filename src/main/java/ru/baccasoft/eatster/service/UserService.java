package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.eatster.model.UserSMSModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class);
    private static final boolean SMS_ENABLED = true;

    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "\"user\"";
    private static final String SQL_COLUMNS = "id,deleted,name,birthday,gender,email,phone,registration_date,referrals,password,promocode,bonus_activated,bonus_date_end,friend_user_id,friend_bonus_date_end,friend_promocode";
    private static final String SQL_SELECT_ALL = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where deleted = false order by name";
    private static final String SQL_SELECT_ITEM = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where id = ? and deleted = false";
    private static final String SQL_INSERT_ITEM = "insert into " + SQL_TABLE + " (deleted,modified,name,birthday,gender,email,phone,registration_date,referrals,password,promocode,bonus_activated,bonus_date_end,friend_user_id,friend_bonus_date_end,friend_promocode) values(false,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_ITEM = "update " + SQL_TABLE + " set modified=now(),name=?,birthday=?,gender=?,email=?,phone=?,registration_date=?,referrals=?,password=?,promocode=?,bonus_activated=?,bonus_date_end=?,friend_user_id=?,friend_bonus_date_end=?,friend_promocode=? where id = ?";
    private static final String SQL_DELETE_ITEM = "update " + SQL_TABLE + " set modified=now(),deleted=true where id = ?";
    private static final String SQL_SELECT_BY_PHONE = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where phone = ? and deleted = false";
    private static final String SQL_SELECT_BY_PROMOCODE = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where promocode = ?";
    private static final String SQL_SELECT_FRIEND_BONUS_DATEEND = "select max(friend_bonus_date_end) from " + SQL_TABLE + " where bonus_activated = true and friend_user_id = ?";
    private static final UserMapper MAPPER = new UserMapper();
    private static final String SMS_POSTFIX = " - ваш код для регистрации в приложении Eatster";
    private static final int GEN_PROMOCODE_LENGTH = 6;
    private static final int GEN_PROMOCODE_TRYCOUNT = 10;

    @Autowired
    SMSService smsService;
    @Autowired
    AppProp appProp;
    @Autowired
    UserSMSService userSMSService;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    private static final class UserMapper implements RowMapper<UserModel> {

        @Override
        public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserModel item = new UserModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setBirthday(rs.getString(++index));
            item.setGender(rs.getString(++index));
            item.setEmail(rs.getString(++index));
            item.setPhone(rs.getString(++index));
            item.setRegistrationDate(rs.getString(++index));
            item.setReferrals(rs.getInt(++index));
            item.setPassword(rs.getString(++index));
            item.setPromocode(rs.getString(++index));
            item.setBonusActivated(rs.getBoolean(++index));
            item.setBonusDateEnd(rs.getString(++index));
            item.setFriendUserId(rs.getLong(++index));
            if (rs.wasNull()) {
                item.setFriendUserId(null);
            }            
            item.setFriendBonusDateEnd(rs.getString(++index));
            item.setFriendPromocode(rs.getString(++index));
            return item;
        }
    }

    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        LOG.debug("findAll:");
        List<UserModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);
        LOG.debug("Ok. return items {0}", list.size());
        return list;
    }

    @Transactional(readOnly = true)
    public UserModel getItem(long id) {
        LOG.debug("getItem: id={0}", id);
        List<UserModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER);
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}", list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional
    private long insertItem(final UserModel item) {
        LOG.debug("insertItem: item={0}", item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[]{"id"});
                int index = 0;
                ps.setString(++index, item.getName());
                ps.setString(++index, item.getBirthday());
                ps.setString(++index, item.getGender());
                ps.setString(++index, item.getEmail());
                ps.setString(++index, item.getPhone());
                ps.setString(++index, item.getRegistrationDate());
                ps.setInt(++index, item.getReferrals());
                ps.setString(++index, item.getPassword());
                ps.setString(++index, item.getPromocode());
                ps.setBoolean(++index, item.isBonusActivated());
                ps.setString(++index, item.getBonusDateEnd());
                ps.setObject(++index, item.getFriendUserId()); //may be null
                ps.setString(++index, item.getFriendBonusDateEnd());
                ps.setString(++index, item.getFriendPromocode());
                return ps;
            }
        },
                keyHolder);
        LOG.debug("Ok. return id={0}", keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    @Transactional
    public int updateItem(UserModel item) {
        LOG.debug("updateItem: item={0}", item);
        int result = jdbc.update(SQL_UPDATE_ITEM,
                new Object[]{
                    item.getName(),
                    item.getBirthday(),
                    item.getGender(),
                    item.getEmail(),
                    item.getPhone(),
                    item.getRegistrationDate(),
                    item.getReferrals(),
                    item.getPassword(),
                    item.getPromocode(),
                    item.isBonusActivated(),
                    item.getBonusDateEnd(),
                    item.getFriendUserId(),
                    item.getFriendBonusDateEnd(),
                    item.getFriendPromocode(),
                    item.getId()}
        );
        LOG.debug("Ok. return {0}", result);
        return result;
    }

    @Transactional
    public int deleteItem(long id) {
        LOG.debug("deleteItem: id={0}", id);
        int result = jdbc.update(SQL_DELETE_ITEM, new Object[]{id});
        LOG.debug("Ok. return {0}", result);
        return result;
    }

    @Transactional(readOnly = true)
    public UserModel getItemByPhone(String phone) {
        LOG.debug("getItemByPhone: phone={0}", phone);
        List<UserModel> list = jdbc.query(SQL_SELECT_BY_PHONE, new Object[]{phone}, MAPPER);
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}", list.get(0));
            return list.get(0);
        }
        if (list.size() > 1) {
            LOG.debug("Fail. Found items={0}", list.size());
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    //пока пин создаем вот так просто
    private String genPassword() {
        long curTime = System.currentTimeMillis();
        String pin = new SimpleDateFormat("ssSSS").format(curTime);
        return pin.substring(0, 4);
    }

    private boolean isUserRegistrationDisabled(String phone) {
        int lastHours  = appProp.getUserLimitRegistrationInterval();
        int limitCount = appProp.getUserLimitRegistrationCount();
        List<UserSMSModel> list = userSMSService.findByPhone(phone,lastHours);
        boolean result = (list.size() >= limitCount);
        LOG.debug("isUserRegistrationDisabled: phone={0},prop.hours={1},prop.limit={2},found={3},result={4}", phone,lastHours,limitCount,list.size(),result);
        return result;
    }
            
    @Transactional
    public void registration(String phone) {
        LOG.debug("registration: phone={0}", phone);
        if (isUserRegistrationDisabled(phone)) {
            LOG.debug("Fail. Limit registration is exceeded on phone={0}", phone);
            throw new RuntimeException("Limit registration is exceeded");
        }
        String password = genPassword();
        if (!SMS_ENABLED) {
            LOG.debug("sms disabled. make password from phone number");
            password = phone.substring(8, 12);
        }
        LOG.debug("Generated password={0}", password);
        //добавим пароль в таблицу отправленных смс
        UserSMSModel userSMSModel = new UserSMSModel();
        userSMSModel.setPhone(phone);
        userSMSModel.setText(password);
        userSMSService.insertItem(userSMSModel);
        //
        UserModel item = getItemByPhone(phone);
        if (item == null) {
            item = new UserModel();
            item.setPhone(phone);
            long itemId = insertItem(item);
            item.setId(itemId);
            String promocode = genPromocode();
            LOG.debug("Generated promocode={0}", promocode);
            if (promocode != null) {
                item.setPromocode(promocode);
            }
        }
        LOG.debug("Replace old password={0} to new password={1}", item.getPassword(), password);
        item.setPassword(password);
        updateItem(item);
        if (!SMS_ENABLED) {
            LOG.debug("Ok.");
            return;
        }
        LOG.debug("Send sms " + password + SMS_POSTFIX + " to phone " + phone);
        if (!smsService.sendSMS(phone, password + SMS_POSTFIX)) {
            LOG.debug("Fail. Error on sending sms to phone={0}", phone);
            throw new RuntimeException("Error on on sending sms");
        }
        LOG.debug("Ok.");
    }

    @Transactional
    public UserModel auth(String phone, String password) {
        LOG.debug("auth: phone={0},password={1}", phone, password);
        if (phone == null || password == null) {
            LOG.debug("Fail. phone or password is invalid");
            return null;
        }
        if (password.isEmpty()) {
            LOG.debug("Fail. password is empty");
            return null;
        }
        UserModel item = getItemByPhone(phone);
        if (item == null) {
            LOG.debug("Fail. Phone {0} not found", phone);
            return null;
        }
        String regDate = item.getRegistrationDate();
        if (DateAsString.getInstance().isEmpty(regDate)) {
            regDate = DateAsString.getInstance().curDateAsString();
            item.setRegistrationDate(regDate);
            updateItem(item);
        }
        //Регистрация для Короткова
        boolean KorotkovAuth = false;
        if (phone.equals("+79262209614") && password.equals("1403")) {
            LOG.debug("KorotkovAuth!");
            KorotkovAuth = true;
        }

        //если регистрация была то пароль должен совпадать
        if (!item.getPassword().equals(password) && (KorotkovAuth == false)) {
            LOG.debug("Fail. Bad password");
            return null;
        }
        //для случаев, когда промокод по каким-то причинам не создан
        //почему-бу не попробовать еще раз
        String promocode = item.getPromocode();
        if (promocode == null) {
            LOG.debug("No promocode. Try to generate");
            promocode = genPromocode();
            LOG.debug("Generated promocode={0}", promocode);
            if (promocode != null) {
                item.setPromocode(promocode);
                updateItem(item);
            }
        }
        LOG.debug("Ok.");
        return item;
    }

    @Transactional(readOnly = true)
    public UserModel getItemByPromocode(String promocode) {
        LOG.debug("getItemByPromocode: promocode={0}", promocode);
        List<UserModel> list = jdbc.query(SQL_SELECT_BY_PROMOCODE, new Object[]{promocode}, MAPPER);
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}", list.get(0));
            return list.get(0);
        }
        if (list.size() > 1) {
            LOG.debug("Fail. Found items={0}", list.size());
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }
    
    private String genPromocodeRandom() {
        StringBuilder sb = new StringBuilder();
        String setChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rand = new Random(System.nanoTime());
        for (int i = 0; i < GEN_PROMOCODE_LENGTH; i++) {
            int k = rand.nextInt(setChars.length());
            sb.append(setChars.charAt(k));
        }
        return sb.toString();
    }

    private String genPromocodeDate() {
        final Date date = new Date();
        String result = Long.toHexString(date.getTime()).toUpperCase();
        result = result.substring(result.length() - GEN_PROMOCODE_LENGTH);
        return result;
    }

    private String genPromocode() {
        LOG.debug("genPromocode:");
        for (int i = 0; i < GEN_PROMOCODE_TRYCOUNT; ++i) {
            String promocode = genPromocodeRandom();
            LOG.debug("try={0},promocode={1}",i,promocode);
            List<UserModel> list = jdbc.query(SQL_SELECT_BY_PROMOCODE, new Object[]{promocode}, MAPPER);
            if (list.isEmpty()) {
                LOG.debug("Ok. promocode={0}",promocode);
                return promocode;
            }
        }
        //последняя надежда получения уникального промокода
        String promocode = genPromocodeDate();
        LOG.debug("trytime,promocode={0}",promocode);
        List<UserModel> list = jdbc.query(SQL_SELECT_BY_PROMOCODE, new Object[]{promocode}, MAPPER);
        if (list.isEmpty()) {
            LOG.debug("Ok. promocode={0}",promocode);
            return promocode;
        }
        LOG.warn("Fail. Error on generate promocode");
        return null;
    }
    
    @Transactional(readOnly = true)
    public String getFriendsBonusDateEnd(long id) {
        LOG.debug("getFriendsBonusDateEnd: id={0}", id);
        String bonusDateEnd = jdbc.queryForObject(SQL_SELECT_FRIEND_BONUS_DATEEND, new Object[]{id}, String.class);
        if (bonusDateEnd == null) {
            bonusDateEnd = "";
        }
        LOG.debug("Ok. Result={0}",bonusDateEnd);
        return bonusDateEnd;
    }

    @Transactional(readOnly = true)
    public String getPersonalBonusDateEnd(long id) {
        LOG.debug("getPersonalBonusDateEnd: id={0}", id);
        String bonusDateEnd = "";
        UserModel item = getItem(id);
        if (item != null) {
            bonusDateEnd = item.getBonusDateEnd();
        }
        LOG.debug("Ok. Result={0}",bonusDateEnd);
        return bonusDateEnd;
    }
    

   
}
