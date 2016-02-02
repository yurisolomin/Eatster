package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class PartnerService {
    private static final Logger LOG = Logger.getLogger(PartnerService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "partner";
    private static final String SQL_COLUMNS = "id,deleted,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin";
    private static final String SQL_SELECT_ALL = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false order by id";
    private static final String SQL_SELECT_ITEM = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where id = ?";
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (deleted,modified,name,password,contact_name,contact_post,contact_email,contact_note,contact_phone,admin) values(false,now(),?,?,?,?,?,?,?,false)";
    private static final String SQL_UPDATE_ITEM = "update "+SQL_TABLE+" set modified=now(),name=?,password=?,contact_name=?,contact_post=?,contact_email=?,contact_note=?,contact_phone=? where id = ?";
    private static final String SQL_DELETE_ITEM = "update "+SQL_TABLE+" set modified=now(),deleted=true where id = ?" ;
    private static final String SQL_SELECT_BY_PARTNER = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where name = ? and admin = false and deleted = false";
    private static final String SQL_SELECT_BY_ADMIN = "select " + SQL_COLUMNS + " from " + SQL_TABLE + " where name = ? and admin = true and deleted = false";
    private static final PartnerMapper MAPPER = new PartnerMapper();

    private static final int GEN_PASSWORD_LENGTH = 8;
/*	
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage preConfiguredMessage;
*/     
    @Autowired
    MailService mailService;
    @Autowired
    AppProp appProp;
    
    
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class PartnerMapper implements RowMapper<PartnerModel> {
        @Override
        public PartnerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            PartnerModel item = new PartnerModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setName(rs.getString(++index));
            item.setPassword(rs.getString(++index));
            item.setContactName(rs.getString(++index));
            item.setContactPost(rs.getString(++index));
            item.setContactEmail(rs.getString(++index));
            item.setContactNote(rs.getString(++index));
            item.setContactPhone(rs.getString(++index));
            item.setAdmin(rs.getBoolean(++index));
            return item;
        }
    }	
    @Transactional(readOnly=true)
    public List<PartnerModel> findAll() {
        LOG.debug("findAll:");
        List<PartnerModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    @Transactional(readOnly=true)
    public PartnerModel getItem(long id) {
        LOG.debug("getItem: id={0}",id);
        List<PartnerModel> list = jdbc.query(SQL_SELECT_ITEM, new Object[]{id}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional
    public long insertItem(final PartnerModel item) {
        LOG.debug("insertItem: item={0}",item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setString(++index, item.getName());
                    ps.setString(++index, item.getPassword());
                    ps.setString(++index, item.getContactName());
                    ps.setString(++index, item.getContactPost());
                    ps.setString(++index, item.getContactEmail());
                    ps.setString(++index, item.getContactNote());
                    ps.setString(++index, item.getContactPhone());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    @Transactional
    public int updateItem(PartnerModel item) {
        LOG.debug("updateItem: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_ITEM, 
                new Object[]{
                    item.getName(), 
                    item.getPassword(),
                    item.getContactName(),
                    item.getContactPost(),
                    item.getContactEmail(),
                    item.getContactNote(),
                    item.getContactPhone(),
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

    @Transactional(readOnly = true)
    private PartnerModel auth(String name, String password, boolean authAdmin) {
        LOG.debug("auth: name={0},password={1},authAdmin={2}", name, password,authAdmin);
        String queryText;
        if (authAdmin) {
            queryText = SQL_SELECT_BY_ADMIN;
        } else {
            queryText = SQL_SELECT_BY_PARTNER;
        }
        if (name == null || password == null) {
            LOG.debug("Fail. name or password is invalid");
            return null;
        }
        if (name.isEmpty()) {
            LOG.debug("Fail. name is empty");
            return null;
        }
        if (password.isEmpty()) {
            LOG.debug("Fail. password is empty");
            return null;
        }
        List<PartnerModel> list = jdbc.query(queryText, new Object[]{name}, MAPPER);
        if (list.isEmpty()) {
            LOG.debug("Fail. name not found");
            return null;
        }
        if (list.size() > 1) {
            LOG.warn("Fail. Found users={0}", list.size());
            return null;
        }
        PartnerModel item = list.get(0);
        LOG.debug("item found={0}", item);
        if (!item.getPassword().equals(password)) {
            LOG.debug("Fail. Bad password");
            return null;
        }
        LOG.debug("Ok.");
        return item;
    }
    
    public PartnerModel authPartner(String name, String password) {
        return auth( name, password, false);
    }
    
    public PartnerModel authAdmin(String name, String password) {
        return auth( name, password, true);
    }
    
    @Transactional(readOnly=true)
    public PartnerModel getPartner(String name) {
        LOG.debug("getPartner: name={0}",name);
        List<PartnerModel> list = jdbc.query(SQL_SELECT_BY_PARTNER, new Object[]{name}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        if (list.size() > 1) {
            LOG.warn("Fail. Found partners={0}", list.size());
            return null;
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    @Transactional(readOnly=true)
    public PartnerModel getAdmin(String name) {
        LOG.debug("getAdmin: name={0}",name);
        List<PartnerModel> list = jdbc.query(SQL_SELECT_BY_ADMIN, new Object[]{name}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        if (list.size() > 1) {
            LOG.warn("Fail. Found admins={0}", list.size());
            return null;
        }
        LOG.warn("Fail. item not found");
        return null;
    }

    private String genPassword() {
        StringBuilder sb = new StringBuilder();
        String setLetters = "ncjkfldieortuysgarqvwfszcepotlsdmnbdks";
        String setDigits = "06943857195837264859602";
        Random rand = new Random(System.nanoTime());
        int sizeLetters = 2 + rand.nextInt(3);
        int sizeDigits  = GEN_PASSWORD_LENGTH - sizeLetters;
        for (int i= 0; i < sizeLetters; i++) {
            int k = rand.nextInt(setLetters.length());
            sb.append(setLetters.charAt(k));
        }
        for (int i= 0; i < sizeDigits; i++) {
            int k = rand.nextInt(setDigits.length());
            sb.append(setDigits.charAt(k));
        }
        return sb.toString();        
    }

/*    
    private boolean sendPassword(String to, String password) {
        LOG.debug("sendPassword:to={0}, password={1}", to, password);
        SimpleMailMessage message = new SimpleMailMessage(preConfiguredMessage);
//        message.setFrom(from);
        message.setTo(to);
//        message.setSubject(subject);
        String text = message.getText()+" "+password;
        message.setText(text);
        LOG.debug("message: from={0}, to={1}, subject={2}, msg={3}", 
                message.getFrom(), 
                to, 
                message.getSubject(), 
                message.getText()
                );
        try {
            mailSender.send(message);
            LOG.debug("Ok.");
            return true;
        } catch(Exception e) {
            LOG.error("Fail. Error:"+e.getMessage());
            return false;
        }
    }
*/
    @Transactional
    public long registrationPartner(PartnerModel partnerModel) {
        LOG.debug("registrationPartner:name={0}", partnerModel.getName());
        PartnerModel partnerAlreadyExists = getPartner(partnerModel.getName());
        if (partnerAlreadyExists != null) {
            LOG.debug("Fail. Partner already exists");
            return 0;
        }
        String password = genPassword();
        partnerModel.setId(0);
        partnerModel.setAdmin(false);
        partnerModel.setPassword(password);
        long insertedId = insertItem(partnerModel);
        if (!mailService.send(partnerModel.getName(),"Регистрация на Eatster","Ваш пароль "+password)) {
            LOG.warn("Fail. Mail not sent.");
            throw new RuntimeException("Error on partner registration!");
        
        }
        //отправить уведомление о регистрации ресторана
        String notificationEmail = appProp.getNotificationEmail();
        if (!notificationEmail.equals("")) {
            String contactData = "E-mail регистрации: "+partnerModel.getName()
                        +". Контактные данные:"
                        +" ФИО "+partnerModel.getContactName()
                        +", телефон "+partnerModel.getContactPhone();
            if (!mailService.send(notificationEmail,"Регистрация на Eatster нового ресторана",contactData)) {
                LOG.warn("Fail. Mail not sent.");
                throw new RuntimeException("Error on partner registration!");
            }
        }
        LOG.debug("Ok.");
        return insertedId;
    }
/*(readOnly=false, rollbackFor=RuntimeException.class)*/    
}
