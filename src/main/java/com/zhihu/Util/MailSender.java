package com.zhihu.Util;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freemarker;

    public boolean sendWithHTMLTemplate(String to, String subject, String template,
                                        Map<String, Object> model){
        try{
            String nickName = MimeUtility.encodeText("头条资讯");
            InternetAddress from = new InternetAddress(nickName + "<w928028422@sina.com>");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            Template tmpl = freemarker.getConfiguration().getTemplate(template);
            String result = FreeMarkerTemplateUtils.processTemplateIntoString(tmpl, model);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(result, true);
            mailSender.send(message);
            return true;

        }catch (Exception e){
            logger.error("邮件发送异常" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.sina.com");
        mailSender.setPort(110);
        mailSender.setDefaultEncoding("utf8");
    }
}
