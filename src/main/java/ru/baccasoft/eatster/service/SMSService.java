package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.utils.logging.Logger;
import org.json.simple.JSONObject;
import ru.baccasoft.eatster.appconfig.AppProp;

@SpringComponent
public class SMSService {
    private static final Logger LOG = Logger.getLogger(SMSService.class);
    private static final String PROVIDER_WEBSMS = "websms";
    private static final String PROVIDER_ATOMPARK = "atompark";
    @Autowired
    AppProp appProp;
    
    private String readStreamToString(InputStream in, String encoding) throws IOException {
        StringBuilder b = new StringBuilder();
        InputStreamReader r = new InputStreamReader(in, encoding);
        int c;
        while ((c = r.read()) != -1) {
            b.append((char) c);
        }
        return b.toString();
    }
/*    
    private boolean sendSMS_atompark(String phone,String text
            ,String URL,String login,String password,String sender) {
        logger.debug("sendSMS_atompark executing");
        RequestBuilder Request= new RequestBuilder(URL);
        API ApiSms=new API(Request, login, password);
        ArrayList<Phones> phoneSend=new ArrayList();
        phoneSend.add(new Phones("","",phone));
        try {
            String xml = ApiSms.sendSms(sender,text,phoneSend);
            logger.debug("response={0}",xml);
            //ожидаем это
            //<?xml version="1.0" encoding="UTF-8"?><RESPONSE><status>1</status><credits>0.8333</credits><amount>1.25</amount><currency>RUR</currency></RESPONSE>
            //не будем выпендриваться. 
            xml = xml.toLowerCase().replace(" ","");
            String okPattern = "<response><status>1</status>";
            if (xml.contains(okPattern)) {
                logger.debug("Ok.");
                return true;
            }
            logger.debug("Fail. Pattern {0} not found",okPattern);
            return false;
        } catch (IOException ex) {
            logger.warn("Fail. SMS exception: {0}",ex.getMessage());
            return false;
        }
    }
*/
    private boolean sendSMS_websms(String phone,String text
            ,String url,String login,String password,String sender) {
        LOG.debug("sendSMS_websms executing");
        String encode = "UTF-8";
//        String encode = "windows-1251";
        JSONObject json = new JSONObject();
        json.put("http_username",login);
        json.put("http_password",password);
        json.put("message",text);
        json.put("phone_list",phone);
        json.put("fromPhone",sender);
        try {
            LOG.debug("query={0}",url + "?json=" +json.toJSONString());
            String query = url + "?json=" + URLEncoder.encode(json.toJSONString(),encode);
            URLConnection conn = new URL(query).openConnection();
            String response = readStreamToString(conn.getInputStream(), encode);
            LOG.debug("response={0}",response);
            String okPattern = "\"OK\"";
            if (response.contains(okPattern)) {
                LOG.debug("Ok.");
                return true;
            }
            LOG.debug("Fail. Pattern {0} not found",okPattern);
            return false;
        } catch (Exception ex) {
            LOG.warn("Fail. SMS exception: {0}",ex.getMessage());
            return false;
        }
    }
    
    public boolean sendSMS(String phone,String text) {
        LOG.debug("sendSMS: phone={0}, text={1}",phone,text);
        String pattern = "\\+\\d{11}";
        if (!phone.matches(pattern)) {
            LOG.debug("Fail. Phone {0} not matches with pattern {1}",phone,pattern);
            return false;
        }
        String provider = appProp.getProperty("sms.provider");
        String URL = appProp.getProperty("sms.url");
        String login = appProp.getProperty("sms.login");
        String password = appProp.getProperty("sms.password");
        String sender = appProp.getProperty("sms.sender");
        LOG.debug("provider={0}, url={1}, login={2}, pwd={3}, sender={4}",provider,URL,login,password,sender);
        if (provider == null) {
            LOG.error("Fail. Property sms.provider not found");
            return false;
        }
        if (provider.equals(PROVIDER_WEBSMS)) {
            return sendSMS_websms(phone,text,URL,login,password,sender);
        }
//        if (provider.equals(PROVIDER_ATOMPARK)) {
//            return sendSMS_atompark(phone,text,URL,login,password,sender);
//        }
        LOG.debug("Fail. Provider {0} not supported",provider);
        return false;
    }
    
}
