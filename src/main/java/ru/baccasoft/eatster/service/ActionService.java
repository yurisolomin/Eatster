package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.io.InputStream;
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
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.image.DisplaySize;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class ActionService {
    private static final Logger LOG = Logger.getLogger(ActionService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "action";
    private static final String SQL_SELECT =
         "select a.id,a.deleted,a.name,restaurant_id,status,a.actiontype_id,actionsubtype_id,photo_urlparams,comment,onmonday,ontuesday,onwednesday,onthursday,onfriday,onsaturday,onsunday"
        +"  ,t.name as actiontype"
        +"  ,st.name as actionsubtype"
        +"  ,a.start_time"
        +"  ,a.end_time"
        +" from action as a"
        +" join actiontype as t on t.id = a.actiontype_id"
        +" join actionsubtype as st on st.id = a.actionsubtype_id";
    private static final String FIELD_ORDER_BY_STATUS = 
         "(case status"
        +" when '"+ActionModel.STAT_PUBLISHED+"' then 0"
        +" when '"+ActionModel.STAT_MODERATION+"' then 1"
        +" when '"+ActionModel.STAT_REJECTED+"' then 2"
        +" when '"+ActionModel.STAT_INACTIVE+"' then 3"
        +" when '"+ActionModel.STAT_ARCHIVE+"' then 4"
        +" else 100"
        +" end)";
    private static final String SQL_SELECT_ALL = SQL_SELECT+" where a.deleted = false order by a.start_time";
    private static final String SQL_SELECT_ITEM = SQL_SELECT+" where a.id = ? and a.deleted = false";
    private static final String SQL_SELECT_BY_REST_PUBLISHED = SQL_SELECT+" where a.deleted = false and a.restaurant_id = ? and a.status = ? order by a.start_time";
    private static final String SQL_SELECT_BY_REST_ALL = SQL_SELECT+" where a.deleted = false and a.restaurant_id = ? order by "+FIELD_ORDER_BY_STATUS;
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (deleted,modified,name,restaurant_id,status,actiontype_id,actionsubtype_id,photo_urlparams,comment,onmonday,ontuesday,onwednesday,onthursday,onfriday,onsaturday,onsunday,start_time,end_time) values(false,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_ITEM = "update "+SQL_TABLE+" set modified=now(),name=?,restaurant_id=?,status=?,actiontype_id=?,actionsubtype_id=?,photo_urlparams=?,comment=?,onmonday=?,ontuesday=?,onwednesday=?,onthursday=?,onfriday=?,onsaturday=?,onsunday=?,start_time=?,end_time=? where id = ?";
    private static final String SQL_DELETE_ITEM = "update "+SQL_TABLE+" set modified=now(),deleted=true where id = ?" ;
    private static final String SQL_SELECT_ALL_TO_MODERATION = SQL_SELECT+" where a.deleted = false and a.status = '"+ActionModel.STAT_MODERATION+"'order by id desc";
    private static final ActionMapper MAPPER = new ActionMapper();
	
    @Autowired
    AppProp appProp;
    @Autowired
    PhotoService photoService;
    
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class ActionMapper implements RowMapper<ActionModel> {
        @Override
        public ActionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ActionModel item = new ActionModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setRestaurantId(rs.getInt(++index));     
            item.setStatus(rs.getString(++index));
            item.setActionTypeId(rs.getLong(++index));     
            item.setActionSubTypeId(rs.getLong(++index));  
            item.setPhotoUrlParams(rs.getString(++index));
            item.setComment(rs.getString(++index));
            item.setOnMonday(rs.getBoolean(++index));
            item.setOnTuesday(rs.getBoolean(++index));
            item.setOnWednesday(rs.getBoolean(++index));
            item.setOnThursday(rs.getBoolean(++index));
            item.setOnFriday(rs.getBoolean(++index));
            item.setOnSaturday(rs.getBoolean(++index));
            item.setOnSunday(rs.getBoolean(++index));
            item.setActionType(rs.getString(++index));
            item.setActionSubType(rs.getString(++index));
            item.setStartTime(rs.getString(++index));
            item.setEndTime(rs.getString(++index));
            return item;
        }
    }	
    @Transactional(readOnly=true)
    public List<ActionModel> findAll() {
        LOG.debug("findAll:");
        List<ActionModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public List<ActionModel> findAllToModeration() {
        LOG.debug("findAllToModeration:");
        List<ActionModel> list = jdbc.query(SQL_SELECT_ALL_TO_MODERATION, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public ActionModel getItem(long id) {
        LOG.debug("getItem: id={0}",id);
        List<ActionModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional
    public long insertItem(final ActionModel item) {
        LOG.debug("insertItem: item={0}",item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setString(++index, item.getName());
                    ps.setLong(++index, item.getRestaurantId());     
                    ps.setString(++index, item.getStatus());
                    ps.setLong(++index, item.getActionTypeId());     
                    ps.setLong(++index, item.getActionSubTypeId());  
                    ps.setString(++index, item.getPhotoUrlParams());
                    ps.setString(++index, item.getComment());
                    ps.setBoolean(++index, item.isOnMonday());
                    ps.setBoolean(++index, item.isOnTuesday());
                    ps.setBoolean(++index, item.isOnWednesday());
                    ps.setBoolean(++index, item.isOnThursday());
                    ps.setBoolean(++index, item.isOnFriday());
                    ps.setBoolean(++index, item.isOnSaturday());
                    ps.setBoolean(++index, item.isOnSunday());
                    ps.setString(++index, item.getStartTime());
                    ps.setString(++index, item.getEndTime());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    @Transactional
    public int updateItem(ActionModel item) {
        LOG.debug("updateItem: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_ITEM, 
                new Object[]{
                    item.getName(), 
                    item.getRestaurantId(),     
                    item.getStatus(),
                    item.getActionTypeId(),     
                    item.getActionSubTypeId(),  
                    item.getPhotoUrlParams(),
                    item.getComment(),
                    item.isOnMonday(),
                    item.isOnTuesday(),
                    item.isOnWednesday(),
                    item.isOnThursday(),
                    item.isOnFriday(),
                    item.isOnSaturday(),
                    item.isOnSunday(),
                    item.getStartTime(),
                    item.getEndTime(),
                    item.getId()} 
        );
        LOG.debug("Ok. return {0}",result);
        return result;
    }
    
    @Transactional
    public int deleteItem(long id) {
        LOG.debug("deleteItem: id={0}",id);
        int result = jdbc.update(SQL_DELETE_ITEM, new Object[]{id} );
        LOG.debug("Ok. return {0}",result);
        return result;
    }

    @Transactional(readOnly=true)
    public List<ActionModel> findByRestaurantPublished(long restaurantId) {
        LOG.debug("findByRestaurantPublished: restaurantId={0}",restaurantId);
        List<ActionModel> list = jdbc.query(SQL_SELECT_BY_REST_PUBLISHED, new Object[]{restaurantId,ActionModel.STAT_PUBLISHED}, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public List<ActionModel> findByRestaurantAll(long restaurantId) {
        LOG.debug("findByRestaurantAll: restaurantId={0}",restaurantId);
        List<ActionModel> list = jdbc.query(SQL_SELECT_BY_REST_ALL, new Object[]{restaurantId}, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
/*    
    @Transactional
    public void uploadPhoto(long actionId,InputStream fileInputStream, String fileName) {
        LOG.debug("uploadPhoto: actionId={0},fileName={1}",actionId,fileName);
        ActionModel actionModel = getItem(actionId);
        if (actionModel == null) {
            LOG.warn("Warn. Action not found");
            return;
        }
        DisplaySize displayFull = null;
        DisplaySize displayMini = null; //не используем пока
        PhotoModel photoModel = photoService.uploadScaledPhoto(actionId,PhotoModel.TYPE_ACT_PHOTO, fileInputStream, fileName, displayFull, displayMini);
        LOG.debug("Photo uploaded with urlParams={0}",photoModel.getPhotoUrlParams());
        actionModel.setPhotoUrlParams(photoModel.getPhotoUrlParams());
        updateItem(actionModel);
        LOG.debug("Ok.");
        }
*/
}
