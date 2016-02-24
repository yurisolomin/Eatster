package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class OperationService {
    private static final Logger LOG = Logger.getLogger(OperationService.class);
    private static final boolean SMS_ENABLED = true;
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "operation";
    private static final String SQL_SELECT =
         " select o.id,o.status,o.restaurant_id,o.user_id,o.waiter_id,o.oper_date,o.oper_time,o.check_sum,o.comment,o.cashback_base_rate,o.cashback_bonus_rate,o.dec_score,o.add_score,o.commission_rate"
        +"  ,r.name as restaurant_name"
        +"  ,u.name as user_name"
        +"  ,u.phone as user_phone"
        +"  ,w.name as waiter_name"
        +" from operation as o"
        +" join restaurant as r on r.id = o.restaurant_id"
        +" join \"user\" as u on u.id = o.user_id"
        +" join waiter as w on w.id = o.waiter_id";
    private static final String SQL_SELECT_ALL = SQL_SELECT+" where o.status = '"+OperationModel.STATUS_CONFIRMED+"' order by o.oper_date desc, o.oper_time desc, o.id desc";
    private static final String SQL_SELECT_ITEM = SQL_SELECT+" where o.id = ?";
    private static final String SQL_SELECT_BY_USER = SQL_SELECT+" where o.status = '"+OperationModel.STATUS_CONFIRMED+"' and o.user_id = ? order by o.oper_date desc, o.oper_time desc, o.id desc";
    private static final String SQL_SELECT_BY_REST = SQL_SELECT+" where o.status = '"+OperationModel.STATUS_CONFIRMED+"' and o.restaurant_id = ? order by o.oper_date desc, o.oper_time desc, o.id desc";
    private static final String SQL_SELECT_BY_SMSCODE = SQL_SELECT+" where o.id = ? and o.sms_code=? and date_part('min',now()-o.sms_time) between 0 and 1";
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (status,modified,restaurant_id,user_id,waiter_id,oper_date,oper_time,check_sum,comment,sms_code,cashback_base_rate,cashback_bonus_rate,dec_score,add_score,commission_rate) values(?,now(),?,?,?,to_char(now(),'YYYY-MM-DD'),to_char(now(),'HH24:MI'),?,?,'',?,?,?,?,?)";
    private static final String SQL_DELETE_ITEM = "update "+SQL_TABLE+" set modified=now(),status='"+OperationModel.STATUS_DELETED+"' where id = ?" ;
    private static final String SQL_UPDATE_STATUS = "update "+SQL_TABLE+" set modified=now(),status=? where id = ?" ;
    private static final String SQL_UPDATE_SMSINFO = "update "+SQL_TABLE+" set modified=now(),sms_code=?,sms_time=now() where id = ?" ;
    private static final String SQL_SELECT_LAST_BY_USER = SQL_SELECT+" where o.status = '"+OperationModel.STATUS_CONFIRMED+"' and o.user_id = ? and o.restaurant_id = ? order by o.oper_date desc, o.oper_time desc limit 1";
    private static final OperationMapper MAPPER = new OperationMapper();
    public static final String MASKCODE_IN_SMS = "[CODE]";

    @Autowired
    SMSService smsService;
    @Autowired
    UserService userService;
    
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class OperationMapper implements RowMapper<OperationModel> {
        @Override
        public OperationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OperationModel item = new OperationModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setStatus(rs.getString(++index));
            item.setRestaurantId(rs.getLong(++index));
            item.setUserId(rs.getLong(++index));
            item.setWaiterId(rs.getLong(++index));
            item.setOperDate(rs.getString(++index));
            item.setOperTime(rs.getString(++index));
            item.setCheckSum(rs.getInt(++index));
            item.setComment(rs.getString(++index));
            item.setCashbackBaseRate(rs.getInt(++index));
            item.setCashbackBonusRate(rs.getInt(++index));
            item.setDecScore(rs.getInt(++index));
            item.setAddScore(rs.getInt(++index));
            item.setCommissionRate(rs.getInt(++index));
            item.setRestaurantName(rs.getString(++index));
            item.setUserName(rs.getString(++index));
            item.setUserPhone(rs.getString(++index));
            item.setWaiterName(rs.getString(++index));
            return item;
        }
    }	
    
    @Transactional(readOnly=true)
    public List<OperationModel> findAll() {
        LOG.debug("findAll:");
        List<OperationModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public List<OperationModel> findByUser(long userId) {
        LOG.debug("findByUserId: userId={0}",userId);
        List<OperationModel> list = jdbc.query(SQL_SELECT_BY_USER, new Object[]{userId}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }

    @Transactional(readOnly=true)
    public List<OperationModel> findByRestaurant(long restaurantId) {
        LOG.debug("findByRestaurant: restaurantId={0}",restaurantId);
        List<OperationModel> list = jdbc.query(SQL_SELECT_BY_REST, new Object[]{restaurantId}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }

    

    @Transactional(readOnly=true)
    public OperationModel getItem(long id) {
        LOG.debug("getItem: id={0}",id);
        List<OperationModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    
    @Transactional(readOnly=true)
    public OperationModel getLastByUser(long user_id, long restaurant_id) {
        LOG.debug("getLastByUser: user_id={0},restaurant_id={1}",user_id,restaurant_id);
        List<OperationModel> list = jdbc.query(SQL_SELECT_LAST_BY_USER, new Object[]{user_id,restaurant_id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional
    public long insertItem(final OperationModel item) {
        LOG.debug("insertItem: item={0}",item);
        if (item.getStatus().isEmpty()) {
            String msg = "Status is empty. Expected confirmed,new,deleted";
            LOG.debug("Fail."+msg);
            throw new RuntimeException(msg);
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setString(++index, item.getStatus());
                    ps.setLong(++index, item.getRestaurantId());
                    ps.setLong(++index, item.getUserId());
                    ps.setLong(++index, item.getWaiterId());
//                    ps.setString(++index, item.getOperDate());//YYYY-MM-DD
//                    ps.setString(++index, item.getOperTime());//HH:MM
                    ps.setInt(++index, item.getCheckSum());
                    ps.setString(++index, item.getComment());
                    ps.setInt(++index, item.getCashbackBaseRate());
                    ps.setInt(++index, item.getCashbackBonusRate());
                    ps.setInt(++index, item.getDecScore());
                    ps.setInt(++index, item.getAddScore());
                    ps.setInt(++index, item.getCommissionRate());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    //пока пин создаем вот так просто
    private String genPassword() {
        long curTime = System.currentTimeMillis(); 
        String pin = new SimpleDateFormat("ssSSS").format(curTime);
        return pin.substring(0,4);
    }

    @Transactional
    public long insertItemWithSms(final OperationModel operationModel, String smsMask) {
        LOG.debug("insertItemWithSms: item={0}",operationModel);
        UserModel userModel = userService.getItem(operationModel.getUserId());
        LOG.debug("userModel={0}",userModel);
        if (userModel == null) {
            LOG.debug("Fail.User not found");
            throw new RuntimeException("User not found");
        }
        String phone = userModel.getPhone();
        String password = genPassword();
        if (!SMS_ENABLED) {
            LOG.debug("sms disabled. make password from phone number");
            password = phone.substring(8,12);
        }
        LOG.debug("Generated password={0}",password);
        long operationId = insertItem(operationModel);
        LOG.debug("operationId={0}",operationId);
        int updated = updateItemSMSInfo(operationId,password);
        if (updated != 1) {
            LOG.debug("Fail. Error on updating sms info. Record updated={0}",updated);
            throw new RuntimeException("Error on updating sms info");
        }
        if (!SMS_ENABLED) {
            LOG.debug("Ok.");
            return operationId;
        }
        String sms = smsMask.replace(MASKCODE_IN_SMS, password);
        if (!smsService.sendSMS(phone,sms)) {
            LOG.debug("Fail. Error on sending sms to phone={0}",phone);
            throw new RuntimeException("Error on on sending sms");
        }
        LOG.debug("Ok.");
        return operationId;
    }
    
    @Transactional
    public int updateItemStatus(long id, String status) {
        LOG.debug("updateItemStatus: id={0},status={1}",id,status);
        int updated = jdbc.update(SQL_UPDATE_STATUS, 
                new Object[]{
                    status, 
                    id
                } 
        );
        LOG.debug("Ok. return updated={0}",updated);
        return updated;
    }
    
    @Transactional
    public int updateItemSMSInfo(long id, String smsCode) {
        LOG.debug("updateItemSMSInfo: id={0},smsCode={1}",id,smsCode);
        int updated = jdbc.update(SQL_UPDATE_SMSINFO, 
                new Object[]{
                    smsCode, 
                    id
                } 
        );
        LOG.debug("Ok. return updated={0}",updated);
        return updated;
    }
    
    @Transactional(readOnly=true)
    public OperationModel getItemBySmsCode(long id, String smsCode) {
        LOG.debug("getItemBySmsCode: id={0},smsCode={1}",id,smsCode);
        List<OperationModel> list = jdbc.query(SQL_SELECT_BY_SMSCODE, new Object[]{id,smsCode}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }
        
    @Transactional
    public int deleteItem(long id) {
        LOG.debug("deleteItem: id={0}",id);
        int result = jdbc.update(SQL_DELETE_ITEM, new Object[]{id} );
        LOG.debug("Ok. return {0}",result);
        return result;
    }
/*
    private int roundToCeil(double fValue) {
        String sValue = String.format("%.0f",fValue);
        return Integer.parseInt(sValue);
    }
    
    public void calculateModel(OperationModel operationModel) {
        LOG.debug("calculateModel:operationModel="+operationModel);
        //
        int checkSum = operationModel.getCheckSum();
        int decScore = operationModel.getDecScore();
        int baseRate = operationModel.getCashbackBaseRate();
        int bonusRate = operationModel.getCashbackBonusRate();
        int commissionRate = operationModel.getCommissionRate();
        //
        int clearSum = checkSum - decScore;
        LOG.debug("clearSum(checkSum-decScore)="+clearSum);
        double fAddScore = 1f * clearSum * (baseRate + bonusRate) / 100;
        LOG.debug("fAddScore="+fAddScore);
        int iAddScore = roundToCeil(fAddScore);
        LOG.debug("iAddScore="+iAddScore);
        operationModel.setAddScore(iAddScore);
        double fCommissionSum = 1f * checkSum * commissionRate / 100;
        LOG.debug("fCommissionSum="+fCommissionSum);
        int iCommissionSum = roundToCeil(fCommissionSum);
        LOG.debug("iCommissionSum="+iCommissionSum);
        operationModel.setCommissionSum(iCommissionSum);
        int incomeSum = iCommissionSum - iAddScore;
        LOG.debug("incomeSum="+incomeSum);
        operationModel.setIncomeSum(incomeSum);
        LOG.debug("Ok. operationModel="+operationModel);
    }
*/
}
