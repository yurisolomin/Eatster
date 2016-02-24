package ru.baccasoft.eatster.service;

//import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.model.ActionSubTypeModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class ActionSubTypeService {
    private static final Logger LOG = Logger.getLogger(ActionSubTypeService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "actionsubtype";
    private static final String SQL_COLUMNS = "id,deleted,name,actiontype_id";
    private static final String SQL_SELECT_ALL = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false order by actiontype_id,id";
    private static final String SQL_SELECT_ITEM = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where id = ?"; // and deleted = false если Id прописан где-то пусть будет и элемент
    private static final String SQL_SELECT_BY_TYPE = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where actiontype_id = ? and deleted = false order by id";
    
    private static final ActionSubTypeMapper MAPPER = new ActionSubTypeMapper();
	
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class ActionSubTypeMapper implements RowMapper<ActionSubTypeModel> {
        @Override
        public ActionSubTypeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ActionSubTypeModel item = new ActionSubTypeModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setActionTypeId(rs.getLong(++index));
            return item;
        }
    }	
    @Transactional(readOnly=true)
    public List<ActionSubTypeModel> findAll() {
        LOG.debug("findAll:");
        List<ActionSubTypeModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public ActionSubTypeModel getItem(long id) {
        LOG.debug("getItem: id={0}",id);
        List<ActionSubTypeModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional(readOnly=true)
    public List<ActionSubTypeModel> findByActionType(long actiontypeId) {
        LOG.debug("findByActionType: actiontypeId={0}",actiontypeId);
        List<ActionSubTypeModel> list = jdbc.query(SQL_SELECT_BY_TYPE, new Object[]{actiontypeId}, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    
}
