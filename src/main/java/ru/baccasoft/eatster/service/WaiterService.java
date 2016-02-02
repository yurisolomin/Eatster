package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class WaiterService {
    private static final Logger LOG = Logger.getLogger(WaiterService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "waiter";
    private static final String SQL_COLUMNS = "id,deleted,name,restaurant_id,login,password";
    private static final String SQL_SELECT_ALL = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false order by name";
    private static final String SQL_SELECT_ITEM = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where id = ?";
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (deleted,modified,name,restaurant_id,login,password) values(false,now(),?,?,?,?)";
    private static final String SQL_UPDATE_ITEM = "update "+SQL_TABLE+" set modified=now(),name=?,restaurant_id=?,login=?,password=? where id = ?";
    private static final String SQL_DELETE_ITEM = "update "+SQL_TABLE+" set modified=now(),deleted=true where id = ?" ;
    private static final String SQL_SELECT_BY_REST = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false and restaurant_id = ? order by name";
    private static final String SQL_SELECT_BY_LOGIN = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where login = ? and deleted = false";
    private static final WaiterMapper MAPPER = new WaiterMapper();
	
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class WaiterMapper implements RowMapper<WaiterModel> {
        @Override
        public WaiterModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            WaiterModel item = new WaiterModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setRestaurantId(rs.getInt(++index));
            item.setLogin(rs.getString(++index));
            item.setPassword(rs.getString(++index));
            return item;
        }
    }	
    @Transactional(readOnly=true)
    public List<WaiterModel> findAll() {
        LOG.debug("findAll:");
        List<WaiterModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public WaiterModel getItem(long id) {
        LOG.debug("getItem: id={0}",id);
        List<WaiterModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }
    
    @Transactional(readOnly=true)
    public WaiterModel getItem(String login) {
        LOG.debug("getItem: login={0}",login);
        List<WaiterModel> list = jdbc.query(SQL_SELECT_BY_LOGIN, new Object[]{login}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional
    public long insertItem(final WaiterModel item) {
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
                    ps.setString(++index, item.getLogin());
                    ps.setString(++index, item.getPassword());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    @Transactional
    public int updateItem(WaiterModel item) {
        LOG.debug("updateItem: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_ITEM, 
                new Object[]{
                    item.getName(), 
                    item.getRestaurantId(),
                    item.getLogin(),
                    item.getPassword(),
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
    public List<WaiterModel> findByRestaurant(long restaurantId) {
        LOG.debug("findByRestaurant: restaurantId={0}",restaurantId);
        List<WaiterModel> list = jdbc.query(SQL_SELECT_BY_REST, new Object[]{restaurantId}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly = true)
    public boolean findTheSameLogin(String login, long skipId) {
        LOG.debug("findByLogin: login={0},skipId={1}", login, skipId);
        List<WaiterModel> list = jdbc.query(SQL_SELECT_BY_LOGIN, new Object[]{login}, MAPPER);
        for(WaiterModel item: list) {
            if (item.getId() != skipId) {
                return true;
            }
        }
        return false;
    }
    
    @Transactional(readOnly = true)
    public WaiterModel auth(String login, String password) {
        LOG.debug("auth: login={0},password={1}", login, password);
        if (login == null || password == null) {
            LOG.debug("Fail. login or password is invalid");
            return null;
        }
        if (login.isEmpty()) {
            LOG.debug("Fail. login is empty");
            return null;
        }
        if (password.isEmpty()) {
            LOG.debug("Fail. password is empty");
            return null;
        }
        List<WaiterModel> list = jdbc.query(SQL_SELECT_BY_LOGIN, new Object[]{login}, MAPPER);
        if (list.isEmpty()) {
            LOG.debug("Fail. login not found");
            return null;
        }
        if (list.size() > 1) {
            LOG.warn("Fail. Found waiters={0}", list.size());
            return null;
        }
        WaiterModel item = list.get(0);
        LOG.debug("item found={0}", item);
        if (!item.getPassword().equals(password)) {
            LOG.debug("Fail. Bad password");
            return null;
        }
        LOG.debug("Ok.");
        return item;
    }
    
}
