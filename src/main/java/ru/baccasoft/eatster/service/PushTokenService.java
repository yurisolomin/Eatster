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
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.PushTokenModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class PushTokenService {

    private static final Logger LOG = Logger.getLogger(PushTokenService.class);
    public static final String PLATFORM_TYPE_IOS = "IOS";
    public static final String PLATFORM_TYPE_ANDROID = "ANDROID";
    public static final String PLATFORM_TYPE_WIN = "WIN";
    public static final String PLATFORM_TYPE_UNKNOWN = "UNKNOWN";
    public static final String PLATFORM_TYPE_TEST = "FAKE_TEST_CLIENT";
    public static final String PLATFORM_SUPPORT = PLATFORM_TYPE_IOS + "," + PLATFORM_TYPE_ANDROID + "," + PLATFORM_TYPE_WIN + "," + PLATFORM_TYPE_TEST;

    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "tokens";
    private static final String SQL_COLUMNS = "phone,platform,token,device_id,last_activity";
    private static final String SQL_UPDATE_ITEM = "update " + SQL_TABLE + " set token=?,platform=?, last_activity=now() where phone = ? and device_id=?";
    private static final String SQL_INSERT_ITEM = "insert into " + SQL_TABLE + " (" + SQL_COLUMNS + ") values(?,?,?,?,now())";
    private static final String SQL_SELECT_BY_PHONE = "select "+SQL_COLUMNS+" from " + SQL_TABLE + " where phone = ?";
    private static final String SQL_SELECT_ALL = "select "+SQL_COLUMNS+" from " + SQL_TABLE +" order by phone";
    private static final PushTokenMapper MAPPER = new PushTokenMapper();

    @Autowired
    AppProp appProp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    private static final class PushTokenMapper implements RowMapper<PushTokenModel> {
        @Override
        public PushTokenModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            PushTokenModel item = new PushTokenModel("","","","");
            int index = 0;
            item.setPhone(rs.getString(++index));
            item.setPlatform(rs.getString(++index));
            item.setPushToken(rs.getString(++index));
            item.setDeviceId(rs.getString(++index));
            return item;
        }
    }	
    
    @Transactional
    private int updateItem(PushTokenModel item) {
        LOG.debug("updateItem: item={0}", item);
        int result = jdbc.update(SQL_UPDATE_ITEM,
                new Object[]{
                    item.getPushToken(),
                    item.getPlatform(),
                    item.getPhone(),
                    item.getDeviceId()
                }
        );
        LOG.debug("Ok. return {0}", result);
        return result;
    }

    @Transactional
    private void insertItem(final PushTokenModel item) {
        LOG.debug("insertItem: item={0}", item);
        jdbc.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM);
                        int index = 0;
                        ps.setString(++index, item.getPhone());
                        ps.setString(++index, item.getPlatform());
                        ps.setString(++index, item.getPushToken());
                        ps.setString(++index, item.getDeviceId());
                        return ps;
                    }
                });
        LOG.debug("Ok.");
    }

    @Transactional(readOnly=true)
    public List<PushTokenModel> findAllByPhone(String phone) {
        LOG.trace("findAllByPhone:phone={0}",phone);
        List<PushTokenModel> list = jdbc.query(SQL_SELECT_BY_PHONE, new Object[]{phone}, MAPPER);	
        LOG.trace("Ok. return items {0}",list.size());
        return list;
    }

    @Transactional(readOnly=true)
    public List<PushTokenModel> findAll() {
        LOG.trace("findAll:");
        List<PushTokenModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.trace("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional
    public void saveToken(String phone, String deviceId, String pushToken, String platform) {
        boolean isOk = false;
        try {
            LOG.debug("saveToken: phone={0}, deviceId={1}, pushToken={2}, platform={3}", phone, deviceId, pushToken, platform);
            if (phone == null || deviceId == null || pushToken == null || platform == null) {
                LOG.debug("Fail. Found NULL value");
                return;
            }
            if (phone.isEmpty() || deviceId.isEmpty() || pushToken.isEmpty() || platform.isEmpty()) {
                LOG.debug("Fail. Found EMPTY value");
                return;
            }
            PushTokenModel item = new PushTokenModel(phone, deviceId, pushToken, platform);
            int updated = updateItem(item);
            if (updated == 0) {
                insertItem(item);
            }
            isOk = true;
        } catch (Throwable e) {
            LOG.error(e, "Error found");
        } finally {
            LOG.debug(isOk ? "Ok." : "Fail.");
        }
    }
/*
    public int push(List<PushTokenModel> pushTokens, String msg) {
        LOG.debug("push:msg={0},pushtokens={1}", msg, pushTokens.size());
        Pusher p = new Pusher();
        //IOS
        IosPushAgent iosAgent = new IosPushAgent();
        boolean iosEnabled = false;
        String prodPath = appProp.getPropertyDef("push.ios.prod.keypath","");
        String prodPass = appProp.getPropertyDef("push.ios.prod.password","");
        String devPath = appProp.getPropertyDef("push.ios.dev.keypath","");
        String devPass = appProp.getPropertyDef("push.ios.dev.password","");
        LOG.debug("ios settings: prodPath={0},prodPass={1},devPath={2},devPass={3}", prodPath, prodPass, devPath, devPass);
        if (!devPath.isEmpty()) {
            LOG.debug("ios development agent added");
            iosAgent.addKeystore(devPath, devPass, false);
            iosEnabled = true;
        }
        if (!prodPath.isEmpty()) {
            LOG.debug("ios production agent added");
            iosAgent.addKeystore(prodPath, prodPass, true);
            iosEnabled = true;
        }
        if (iosEnabled) {
            p.addPushAgent(iosAgent);
        }
        //Android
        boolean androidEnabled = false;
        String apiKey = appProp.getProperty("push.android.apikey");
        String collapseKey = appProp.getProperty("push.android.collapsekey");
        LOG.debug("android settings: apikey={0},collapsekey={1}", apiKey, collapseKey);
        if (!apiKey.isEmpty() || !collapseKey.isEmpty()) {
            LOG.debug("android agent added");
            AndroidPushAgent androidAgent = new AndroidPushAgent();
            androidAgent.config(apiKey, collapseKey);
            p.addPushAgent(androidAgent);
            androidEnabled = true;
        }
        //заполним токены
        List<DeviceToken> tokens = new ArrayList();
        for (PushTokenModel token : pushTokens) {
            Platform platform = null;
            if (iosEnabled) {
                if (token.getPlatform().equals(PLATFORM_TYPE_IOS)) {
                    platform = Platform.IOS;
                }
            }
            if (androidEnabled) {
                if (token.getPlatform().equals(PLATFORM_TYPE_ANDROID)) {
                    platform = Platform.ANDROID;
                }
            }
            // else if (token.getPlatform().equals("WIN_PHONE") || token.getPlatform().equals("WIN_PAD")) {
            // platform = Platform.WINDOWS;
            // }
            if (platform != null) {
                tokens.add(new DeviceToken(platform, token.getPushToken()));
            }
        }
        // original text from ru.baccasoft.billboards.push.PushSender
        // Pusher p = new Pusher()
        // .addPushAgent(
        // new IosPushAgent()
        // .addKeystore("key/apnsDevBillboards.p12", "1", false)
        // .addKeystore("key/apnsDistrBillboards.p12", "bacca4billboards", true)
        // );
        // p.addPushAgent(new AndroidPushAgent()
        // .config("AIzaSyD7B2poBwEeWaxPznAmU2grWsGwTYqhuVA", "AllBillboards"));
        //
        if (tokens.size() > 0) {
            LOG.debug("push execute. msg={0},tokens={1}", msg, tokens.size());
            p.push(msg, tokens);
        } else {
            LOG.debug("push skipped. msg={0},tokens={1}", msg, tokens.size());
        }
        return tokens.size();
    }
    
    public int push(String phone, String msg) {
        LOG.debug("push: phone={0},msg={1}", phone, msg );
        List<PushTokenModel> items = findAllByPhone(phone);
        if (items.size() > 0) {
            int sended = push(items,msg);
            LOG.debug("Ok.");
            return sended;
        } else {
            LOG.debug("Fail. No tokens");
            return 0;
        }
    }
*/    
}
