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
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.UserReportModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class UserReportService {

    private static final Logger LOG = Logger.getLogger(UserReportService.class);
    private JdbcTemplate jdbc;
    private static final String SQL_SELECT = ""
    +" select id,deleted,name,birthday,gender,email,phone,registration_date,bonus_activated,bonus_date_end,friend_promocode,promocode"
    +" , COALESCE(uf.friends_count,0) as friends_count"
    +" , COALESCE(uf.friends_bonus_activated_count,0) as friends_bonus_activated_count"
    +" , COALESCE(uf.friends_bonus_date_end,'') as friends_bonus_date_end"
    +" , COALESCE(op.scores_total,0) as scores_total"
    +" , COALESCE(op.scores_spent,0) as scores_spent"
    +" , COALESCE(op.scores_balance,0) as scores_balance"
    +" , COALESCE(op.oper_count,0) as oper_count"
    +" , COALESCE(op.oper_sum,0) as oper_sum"
    +" , uf.*"
    +" , op.*"
    +" from \"user\" as u"
    +" left join (select friend_user_id"
    +"            ,count(*) as friends_count "
    +"            ,sum(case bonus_activated when true then 1 else 0 end) as friends_bonus_activated_count"
    +"            ,max(friend_bonus_date_end) as friends_bonus_date_end"
    +"            from \"user\""
    +"            group by friend_user_id) as uf on uf.friend_user_id = u.id"
    +" left join (select user_id"
    +"            ,sum(add_score) as scores_total"
    +"            ,sum(dec_score) as scores_spent"
    +"            ,sum(add_score)-sum(dec_score) as scores_balance"
    +"            ,count(*) as oper_count"
    +"            ,sum(check_sum) as oper_sum"
    +"            from operation"
    +"            where status = '"+OperationModel.STATUS_CONFIRMED+"'"
    +"            group by user_id"
    +"            ) as op on op.user_id = u.id"
    ;
    private static final String SQL_SELECT_ALL = SQL_SELECT + " order by u.name";
    private static final String SQL_SELECT_BY_USER_ID = SQL_SELECT + " where u.id = ?";
    private static final String SQL_SELECT_BY_USER_MASK = SQL_SELECT + " where upper(u.name) like upper(?)";
    private static final UserReportMapper MAPPER = new UserReportMapper();
    
    @Autowired
    UserBonusService userBonusService;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    private static final class UserReportMapper implements RowMapper<UserReportModel> {

        @Override
        public UserReportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserReportModel item = new UserReportModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setBirthday(rs.getString(++index));
            item.setGender(rs.getString(++index));
            item.setEmail(rs.getString(++index));
            item.setPhone(rs.getString(++index));
            item.setRegistrationDate(rs.getString(++index));
            item.setBonusActivated(rs.getBoolean(++index));
            item.setBonusDateEnd(rs.getString(++index));
            item.setFriendPromocode(rs.getString(++index));
            item.setPromocode(rs.getString(++index));
            item.setFriendsCount(rs.getInt(++index));
            item.setFriendsBonusActivatedCount(rs.getInt(++index));
            item.setFriendsBonusDateEnd(rs.getString(++index));
            item.setScoresTotal(rs.getInt(++index));
            item.setScoresSpent(rs.getInt(++index));
            item.setScoresBalance(rs.getInt(++index));
            item.setOperCount(rs.getInt(++index));
            item.setOperSum(rs.getInt(++index));
            //считаем бонусный период на текущую дату
            String curDate = DateAsString.getInstance().curDateAsString();
            //моя дата окончания бонусного периода
            String endDate = item.getBonusDateEnd();
            //если она меньше бонусного периода от моих друзей, то используем 
            //максимальную из дат окончания бонусного периода от моих друзей
            if (endDate.compareTo(item.getFriendsBonusDateEnd()) < 0 ) {
                endDate = item.getFriendsBonusDateEnd();
            }
            int bonusPeriodEstimate = UserBonusService.estimate(curDate, endDate);
            item.setBonusPeriodEstimate(bonusPeriodEstimate);
            return item;
        }
    }

    @Transactional(readOnly = true)
    public List<UserReportModel> findAll() {
        LOG.debug("findAll:");
        List<UserReportModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);
        LOG.debug("Ok. return items {0}", list.size());
        return list;
    }
 
    @Transactional(readOnly = true)
    public List<UserReportModel> findByUserId(long userId) {
        LOG.debug("findByUserId:userId={0}",userId);
        List<UserReportModel> list = jdbc.query(SQL_SELECT_BY_USER_ID, new Object[] {userId}, MAPPER);
        LOG.debug("Ok. return items {0}", list.size());
        return list;
    }
    
    @Transactional(readOnly = true)
    public List<UserReportModel> findByUserMask(String userMask) {
        LOG.debug("findByUserMask:userMask={0}",userMask);
        List<UserReportModel> list = jdbc.query(SQL_SELECT_BY_USER_MASK, new Object[] {userMask}, MAPPER);
        LOG.debug("Ok. return items {0}", list.size());
        return list;
    }
}
