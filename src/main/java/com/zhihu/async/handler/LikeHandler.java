package com.zhihu.async.handler;

import com.zhihu.async.EventHandler;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventType;
import com.zhihu.Model.User;
import com.zhihu.Service.MessageService;
import com.zhihu.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        User user = userService.getUser(model.getActorId());
        String content = String.format("用户%s赞了你的回答 <a href=\"/question/%d\">%s</a>",
                user.getName(), Integer.valueOf(model.getExt("questionId")), model.getExt("answerName"));
        messageService.addMessage(content, 1, model.getEntityOwnerId());
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
