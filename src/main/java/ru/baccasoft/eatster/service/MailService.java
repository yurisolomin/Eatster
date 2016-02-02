package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class MailService {
    private static final Logger LOG = Logger.getLogger(MailService.class);
    
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage preConfiguredMessage;
    
    public boolean send(String to, String subject, String text) {
        LOG.debug("send:to={0}, subject={1}, text={2}", to, subject, text);
        SimpleMailMessage message = new SimpleMailMessage(preConfiguredMessage);
//        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        LOG.debug("message: from={0}, to={1}, subject={2}, text={3}", 
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
}
