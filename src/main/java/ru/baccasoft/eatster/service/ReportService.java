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
import ru.baccasoft.eatster.model.OperationReportModel;
import ru.baccasoft.eatster.model.ReportModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class ReportService {
    private static final Logger LOG = Logger.getLogger(ReportService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_SELECT_ALL = "select rp.id, r.id as restaurant_id, r.name as restaurant_name,"
        +" rp.scores_total, rp.scores_spent, rp.scores_balance, rp.oper_count, rp.check_sum, rp.status, rp.report_year, rp.report_month"
        +" from report rp"
        +" join restaurant r on r.id = rp.restaurant_id";
//    private static final String SQL_CLEAR_MONTH = "update report set scores_total=0, scores_spent=0, scores_balance=0, oper_count=0, check_sum=0 where report_year = ? and report_month = ?";
//    private static final String SQL_UPDATE_RESTAURANT = "update report set scores_total=?, scores_spent=?, scores_balance=?, oper_count=?, check_sum=? where restaurant_id = ? and report_year = ? and report_month = ?";
    private static final String SQL_SELECT_BY_MONTH = SQL_SELECT_ALL+" where rp.report_year = ? and rp.report_month = ?";
    private static final String SQL_SELECT_BY_MONTH_LIMIT_ONE = SQL_SELECT_ALL+" where rp.report_year = ? and rp.report_month = ? limit 1";
    private static final String SQL_SELECT_REST_BY_MONTH = SQL_SELECT_ALL+" where rp.restaurant_id = ? and rp.report_year = ? and rp.report_month = ?";
    private static final String SQL_SELECT_REST_ALL = SQL_SELECT_ALL+" where rp.restaurant_id = ? order by rp.report_year desc, rp.report_month desc";
    private static final String SQL_SELECT_BY_MONTH_IN_STATUS = SQL_SELECT_ALL+" where rp.report_year = ? and rp.report_month = ? and rp.status = ?";
    private static final String SQL_SELECT_BY_MONTH_NOTIN_STATUS = SQL_SELECT_ALL+" where rp.report_year = ? and rp.report_month = ? and rp.status <> ?";
    private static final String SQL_DELETE_BY_MONTH = "delete from report where report_year = ? and report_month = ?";
//    private static final String SQL_SELECT_BY_RESTAURANT_LAST = SQL_SELECT_ALL+" where restaurant_id = ? order by report_year desc, report_month desc limit 1";
    private static final String SQL_INSERT_ITEM = "insert into report (modified,restaurant_id,scores_total,scores_spent,scores_balance,oper_count,check_sum,status,report_year,report_month) values(now(),?,?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_STATUS = "update report set modified=now(),status=? where id = ?";
    private static final ReportMapper MAPPER = new ReportMapper();
    
    @Autowired
    OperationReportService operationReportService;
    
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class ReportMapper implements RowMapper<ReportModel> {
        @Override
        public ReportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReportModel item = new ReportModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setRestaurantId(rs.getInt(++index));
            item.setRestaurantName(rs.getString(++index));
            item.setScoresTotal(rs.getInt(++index));
            item.setScoresSpent(rs.getInt(++index));
            item.setScoresBalance(rs.getInt(++index));
            item.setOperCount(rs.getInt(++index));
            item.setCheckSum(rs.getInt(++index));
            item.setStatus(rs.getString(++index));
            item.setReportYear(rs.getInt(++index));
            item.setReportMonth(rs.getInt(++index));
            return item;
        }
    }	
    
    @Transactional(readOnly=true)
    public List<ReportModel> findByMonth(int year, int month) {
        LOG.debug("findByMonth: year={0},month={1}",year,month);
        List<ReportModel> list = jdbc.query(SQL_SELECT_BY_MONTH, new Object[]{year,month}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public List<ReportModel> findByMonthInStatus(int year, int month, String status) {
        LOG.debug("findByMonthInStatus: year={0},month={1},status={2}",year,month,status);
        List<ReportModel> list = jdbc.query(SQL_SELECT_BY_MONTH_IN_STATUS, new Object[]{year,month,status}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }

    @Transactional(readOnly=true)
    public List<ReportModel> findByMonthNotInStatus(int year, int month, String status) {
        LOG.debug("findByMonthNotInStatus: year={0},month={1},status={2}",year,month,status);
        List<ReportModel> list = jdbc.query(SQL_SELECT_BY_MONTH_NOTIN_STATUS, new Object[]{year,month,status}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
/*
    @Transactional(readOnly=true)
    public ReportModel getByRestaurantLastMonth(long restaurantId) {
        LOG.debug("getByRestaurantLastMonth: restaurantId={0}",restaurantId);
        List<ReportModel> list = jdbc.query(SQL_SELECT_BY_RESTAURANT_LAST, new Object[]{restaurantId}, MAPPER );
        LOG.debug("list.size=",list.size());
        if (list.isEmpty()) {
            LOG.debug("Ok. return null");
            return null;
        }
        LOG.debug("Ok. return item={0}",list.get(0));
        return list.get(0);
    }
*/
    @Transactional(readOnly=true)
    public ReportModel getByRestaurant(long restaurantId, int year, int month) {
        LOG.debug("getItemByMonth: restaurantId={0},year={1},month={2}",restaurantId,year,month);
        List<ReportModel> list = jdbc.query(SQL_SELECT_REST_BY_MONTH, new Object[]{restaurantId,year,month}, MAPPER );
        if (list.isEmpty()) {
            LOG.debug("Fail. Item not found");
            return null;
        }
        LOG.debug("Ok. return item={0}",list.get(0));
        return list.get(0);
    }

    @Transactional(readOnly=true)
    public List<ReportModel> findByRestaurant(long restaurantId) {
        LOG.debug("findByRestaurant: restaurantId={0}",restaurantId);
        List<ReportModel> list = jdbc.query(SQL_SELECT_REST_ALL, new Object[]{restaurantId}, MAPPER );
        LOG.debug("Ok. return items={0}",list.size());
        return list;
    }
    
    @Transactional
    public long insertItem(final ReportModel item) {
        LOG.debug("insertItem: item={0}",item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setLong(++index, item.getRestaurantId());     
                    ps.setInt(++index, item.getScoresTotal());
                    ps.setInt(++index, item.getScoresSpent());
                    ps.setInt(++index, item.getScoresBalance());
                    ps.setInt(++index, item.getOperCount());
                    ps.setInt(++index, item.getCheckSum());     
                    ps.setString(++index, item.getStatus());
                    ps.setInt(++index, item.getReportYear());     
                    ps.setInt(++index, item.getReportMonth());
                    return ps;
                }

            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    //надо узнать если в этом месяце оплаченные записи.
    @Transactional(readOnly=true)
    public boolean canCalculate(int year, int month) {
        LOG.debug("canCalculate: year={0},month={1}",year,month);
        List<ReportModel> listRestPaid = findByMonthInStatus(year,month,ReportModel.STAT_PAID);
        LOG.debug("listPaid.size={0}",listRestPaid.size());
        boolean result = listRestPaid.isEmpty();
        LOG.debug("canCalculate: Ok. result={0}",result);
        return result;
    }
    
    //надо узнать если в этом месяце оплаченные записи.
    @Transactional(readOnly=true)
    public boolean alreadyCalculated(int year, int month) {
        LOG.debug("alreadyCalculated: year={0},month={1}",year,month);
        List<ReportModel> list = jdbc.query(SQL_SELECT_BY_MONTH_LIMIT_ONE, new Object[]{year,month}, MAPPER );
        boolean result = !list.isEmpty();
        LOG.debug("alreadyCalculated: Ok. result={0}",result);
        return result;
    }

    @Transactional
    public void calculate(int year, int month)  {
        LOG.debug("calculate: year={0},month={1}",year,month);
        //надо узнать если в этом месяце оплаченные записи.
        if (!canCalculate(year,month)) {
            //если такие есть, то пересчитать не сможем
            LOG.warn("Warning! Found paid records");
            throw new RuntimeException("Error on calculate month! Found paid records in year="+year+", month="+month);
        }
        //удалим период
        int deleted = jdbc.update(SQL_DELETE_BY_MONTH, new Object[]{year,month});
        LOG.debug("delete records={0}",deleted);
        //проведем расчет периода
        List<OperationReportModel> listRestCalculated = operationReportService.getByMonth(year, month);
        LOG.debug("listRestCalculated.size={0}",listRestCalculated.size());
        for(OperationReportModel calc: listRestCalculated) {
            ReportModel reportModel = new ReportModel();
            reportModel.setRestaurantId(calc.getRestaurantId());
            reportModel.setReportYear(year);
            reportModel.setReportMonth(month);
            reportModel.setStatus(ReportModel.STAT_FORMED);
            reportModel.setScoresTotal(calc.getScoresTotal());
            reportModel.setScoresSpent(calc.getScoresSpent());
            reportModel.setScoresBalance(calc.getScoresBalance());
            reportModel.setOperCount(calc.getOperCount());
            reportModel.setCheckSum(calc.getCheckSum());
            insertItem(reportModel);
        }
        int yearPrev = year;
        int monthPrev = month - 1;
        if (monthPrev == 0) {
            monthPrev = 12;
            --yearPrev;
        }
        //просмотрим неоплаченные записи прошлого месяца
        List<ReportModel> listRestNotPaid = findByMonthNotInStatus(yearPrev,monthPrev,ReportModel.STAT_PAID);
        for (ReportModel restNotPaid:listRestNotPaid) {
            long restaurantId = restNotPaid.getRestaurantId();
            ReportModel reportModel = getByRestaurant(restaurantId,year,month);
            if (reportModel == null) {
                reportModel = new ReportModel();
                reportModel.setRestaurantId(restaurantId);
                reportModel.setReportYear(year);
                reportModel.setReportMonth(month);
                reportModel.setStatus(ReportModel.STAT_NOTPAID);
                reportModel.setScoresTotal(0);
                reportModel.setScoresSpent(0);
                reportModel.setScoresBalance(0);
                reportModel.setOperCount(0);
                reportModel.setCheckSum(0);
                insertItem(reportModel);
            } else {
                reportModel.setStatus(ReportModel.STAT_NOTPAID);
                updateStatus(reportModel);
            }
        }
        LOG.debug("calculate: Ok.");
    }
    
    @Transactional
    public int updateStatus(ReportModel item) {
        LOG.debug("updateStatus: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_STATUS, 
                new Object[]{
                    item.getStatus(), 
                    item.getId()} 
        );
        LOG.debug("Ok. return {0}",result);
        return result;
    }
/*    
    @Transactional
    public void setAllFormedToNotPaid(int year, int month)  {
        LOG.debug("setAllFormedToNotPaid: year={0},month={1}",year,month);
        List<ReportModel> list = findByMonthInStatus(year, month, ReportModel.STAT_FORMED);
        for(ReportModel item: list) {
            LOG.debug("Change status {0} to {1} for restaurant Id={2},name={3}",item.getStatus(),ReportModel.STAT_NOTPAID,item.getRestaurantId(),item.getRestaurantName());
            item.setStatus(ReportModel.STAT_NOTPAID);
            updateStatus(item);
        }
        LOG.debug("setAllFormedToNotPaid:Ok. changed={1}",list.size());
    }
*/    
/*
    @Scheduled(cron = "10 * * * * *")
    public void cronTask(){
        System.out.println(new Date() + " This runs in a cron schedule");
    }
*/
}
