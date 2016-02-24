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
import ru.baccasoft.eatster.model.UserSMSModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class UserSMSService {
    private static final Logger LOG = Logger.getLogger(UserSMSService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "user_sms";
    private static final String SQL_COLUMNS = "id,phone,text";
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (modified,phone,text) values(now(),?,?)";
    private static final String SQL_SELECT_BY_PHONE = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where phone = ? and modified > now() - ?::interval order by id";
    private static final UserSMSMapper MAPPER = new UserSMSMapper();
	
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class UserSMSMapper implements RowMapper<UserSMSModel> {
        @Override
        public UserSMSModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserSMSModel item = new UserSMSModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setPhone(rs.getString(++index));
            return item;
        }
    }	
/*    
    @Transactional(readOnly=true)
    public List<UserSMSModel> findAll() {
        LOG.debug("findAll:");
        List<UserSMSModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
*/
/*    
    @Transactional(readOnly=true)
    public UserSMSModel getItemByPhone(String id) {
        LOG.debug("getItem: id={0}",id);
        List<UserSMSModel> list = jdbc.query(SQL_SELECT_ITEM_BY_PHONE, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }
*/
    @Transactional(readOnly=true)
    public List<UserSMSModel> findByPhone(String phone, int lastHours) {
        LOG.debug("findByPhone: phone={0},lastHours={1}",phone,lastHours);
        String hours = ""+lastHours+" hours";
        List<UserSMSModel> list = jdbc.query(SQL_SELECT_BY_PHONE, new Object[]{phone,hours}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional
    public long insertItem(final UserSMSModel item) {
        LOG.debug("insertItem: item={0}",item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setString(++index, item.getPhone());
                    ps.setString(++index, item.getText());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }
    
}
