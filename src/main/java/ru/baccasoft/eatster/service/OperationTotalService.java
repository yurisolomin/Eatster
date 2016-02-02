package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.OperationTotalModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class OperationTotalService {
    private static final Logger LOG = Logger.getLogger(OperationTotalService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_SELECT_TOTAL = "select "
        +" COALESCE( sum(case when score > 0 then score else 0 end), 0) as scores_total,"
        +" COALESCE( sum(case when score < 0 then -score else 0 end), 0) as scores_spent,"
        +"  count(*) as oper_count,"
        +"  COALESCE(sum(check_sum),0) as oper_sum"
        +" from operation where status = '"+OperationModel.STATUS_CONFIRMED+"'";
    private static final String SQL_SELECT_TOTAL_BY_REST = SQL_SELECT_TOTAL+" and restaurant_id=?";
    private static final String SQL_SELECT_TOTAL_BY_USER = SQL_SELECT_TOTAL+" and user_id=?";
    private static final OperationTotalMapper MAPPER = new OperationTotalMapper();

    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class OperationTotalMapper implements RowMapper<OperationTotalModel> {
        @Override
        public OperationTotalModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OperationTotalModel item = new OperationTotalModel();
            int index = 0;
            item.setScoresTotal(rs.getInt(++index));
            item.setScoresSpent(rs.getInt(++index));
            item.setOperCount(rs.getInt(++index));
            item.setOperSum(rs.getInt(++index));
            item.setScoresBalance(item.getScoresTotal()-item.getScoresSpent());
            return item;
        }
    }	
    
    @Transactional(readOnly=true)
    public OperationTotalModel getByRestaurant(long restaurantId) {
        LOG.debug("getByRestaurant: restaurantId={0}",restaurantId);
        List<OperationTotalModel> list = jdbc.query(SQL_SELECT_TOTAL_BY_REST, new Object[]{restaurantId}, MAPPER );
        LOG.debug("Ok. return items {0}",list.get(0));
        return list.get(0);
    }
    
    @Transactional(readOnly=true)
    public OperationTotalModel getAll() {
        LOG.debug("getAll:");
        List<OperationTotalModel> list = jdbc.query(SQL_SELECT_TOTAL, MAPPER );
        LOG.debug("Ok. return items {0}",list.get(0));
        return list.get(0);
    }

    @Transactional(readOnly=true)
    public OperationTotalModel getByUser(long userId) {
        LOG.debug("getByUser: userId={0}",userId);
        List<OperationTotalModel> list = jdbc.query(SQL_SELECT_TOTAL_BY_USER, new Object[]{userId}, MAPPER );
        LOG.debug("Ok. return items {0}",list.get(0));
        return list.get(0);
    }
    
}
