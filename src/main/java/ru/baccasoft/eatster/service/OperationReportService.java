package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.OperationReportModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class OperationReportService {
    private static final Logger LOG = Logger.getLogger(OperationReportService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_SELECT_TOTAL = "select r.id as restaurant_id, r.name as restaurant_name,"
        +" COALESCE( sum(case when o.score > 0 then  o.score else 0 end), 0) as scores_total,"
        +" COALESCE( sum(case when o.score < 0 then -o.score else 0 end), 0) as scores_spent,"
        +" count(*) as oper_count,"
        +" COALESCE( sum(check_sum), 0) as check_sum"
        +" from restaurant r"
        +" join operation o on o.restaurant_id = r.id"
        +" where o.oper_date between ? and ? and o.status = '"+OperationModel.STATUS_CONFIRMED+"'"
        +" group by r.id, r.name";
    private static final OperationReportMapper MAPPER = new OperationReportMapper();

/*
select r.id as restaurant_id, r.name as restaurant_name, 
COALESCE( sum(case when o.score > 0 then  o.score else 0 end), 0) as scores_total,
COALESCE( sum(case when o.score < 0 then -o.score else 0 end), 0) as scores_spent,
--COALESCE( sum(case when o.check_sum > 0 then  o.check_sum else 0 end), 0) as check_sum_total,
--COALESCE( sum(case when o.check_sum < 0 then -o.check_sum else 0 end), 0) as check_sum_spent,
count(*) as oper_count,
0
from restaurant r
join operation o on o.restaurant_id = r.id
where oper_date between '2015-01-01' and '2015-12-31'
group by r.id
    */    
    
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class OperationReportMapper implements RowMapper<OperationReportModel> {
        @Override
        public OperationReportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OperationReportModel item = new OperationReportModel();
            int index = 0;
            item.setRestaurantId(rs.getInt(++index));
            item.setRestaurantName(rs.getString(++index));
            item.setScoresTotal(rs.getInt(++index));
            item.setScoresSpent(rs.getInt(++index));
            item.setOperCount(rs.getInt(++index));
            item.setCheckSum(rs.getInt(++index));
            item.setScoresBalance(item.getScoresTotal()-item.getScoresSpent());
            return item;
        }
    }	
/*    
    @Transactional(readOnly=true)
    public List<OperationReportModel> getByPeriod(String dateStart, String dateEnd) {
        LOG.debug("getByPeriod: dateStart={0},dateEnd={1}",dateStart,dateEnd);
        dateStart = DateAsString.getInstance().fixAsString(dateStart);
        dateEnd = DateAsString.getInstance().fixAsString(dateEnd);
        LOG.debug("query dates: dateStart={0},dateEnd={1}",dateStart,dateEnd);
        List<OperationReportModel> list = jdbc.query(SQL_SELECT_TOTAL, new Object[]{dateStart,dateEnd}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
*/    
    @Transactional(readOnly=true)
    public List<OperationReportModel> getByMonth(int year, int month) {
        LOG.debug("getByMonth: year={0},month={1}",year,month);
        String dateStart = DateAsString.getInstance().getStartMonthAsString(year, month);
        String dateEnd = DateAsString.getInstance().getEndMonthAsString(year, month);
        LOG.debug("query dates: dateStart={0},dateEnd={1}",dateStart,dateEnd);
        List<OperationReportModel> list = jdbc.query(SQL_SELECT_TOTAL, new Object[]{dateStart,dateEnd}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
}
