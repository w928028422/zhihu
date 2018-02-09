package com.zhihu.async.handler;

import com.zhihu.async.EventHandler;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventType;
import com.zhihu.Service.MessageService;
import com.zhihu.Util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        String content = "你上次的登录ip异常";
        messageService.addMessage(content, 1, model.getActorId());
        /*Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登录异常", "welcome.ftl", map);*/
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
