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
import ru.baccasoft.eatster.model.AverageCheckModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class AverageCheckService {
    private static final Logger LOG = Logger.getLogger(AverageCheckService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "averagecheck";
    private static final String SQL_COLUMNS = "id,deleted,name";
    private static final String SQL_SELECT_ALL = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false order by name";
    private static final String SQL_SELECT_ITEM = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where id = ? and deleted = false";
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (deleted,modified,name) values(false,now(),?)";
    private static final String SQL_UPDATE_ITEM = "update "+SQL_TABLE+" set modified=now(),name=? where id = ?";
    private static final String SQL_DELETE_ITEM = "update "+SQL_TABLE+" set modified=now(),deleted=true where id = ?" ;
    private static final String SQL_SELECT_CHANGED = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where modified > ?";
    private static final AverageCheckMapper MAPPER = new AverageCheckMapper();
	
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class AverageCheckMapper implements RowMapper<AverageCheckModel> {
        @Override
        public AverageCheckModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            AverageCheckModel item = new AverageCheckModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            return item;
        }
    }	
    @Transactional(readOnly=true)
    public List<AverageCheckModel> findAll() {
        LOG.debug("findAll:");
        List<AverageCheckModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public AverageCheckModel getItem(long id) {
        LOG.debug("getItem: id={0}",id);
        List<AverageCheckModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        if (list.size() > 1) {
            LOG.debug("Fail. Found items={0}",list.size());
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional
    public long insertItem(final AverageCheckModel item) {
        LOG.debug("insertItem: item={0}",item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setString(++index, item.getName());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    @Transactional
    public int updateItem(AverageCheckModel item) {
        LOG.debug("updateItem: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_ITEM, 
                new Object[]{
                    item.getName(), 
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
    public List<AverageCheckModel> findChanged(Timestamp timestamp) {
        LOG.debug("findChanged: timestamp={0}",timestamp);
        List<AverageCheckModel> list = jdbc.query(SQL_SELECT_CHANGED, new Object[]{timestamp}, MAPPER);		
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
}
