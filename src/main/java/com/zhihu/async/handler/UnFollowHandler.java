package com.zhihu.async.handler;

import com.zhihu.Model.EntityType;
import com.zhihu.Model.User;
import com.zhihu.Service.MessageService;
import com.zhihu.Service.UserService;
import com.zhihu.async.EventHandler;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UnFollowHandler implements EventHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel model) {
        User user = userService.getUser(model.getActorId());
        String content;
        if(model.getEntityType() == EntityType.QUESTION){
            content = "用户" + user.getName() + "取消关注了你的问题 <a href=\"/question/"
                    + model.getEntityId() + "\">" + model.getExt("questionName") +  "</a>";
        }else {
            content = "用户" + user.getName() + "取消关注了你";
        }
        messageService.addMessage(content, 1, model.getEntityOwnerId());
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.UNFOLLOW);
    }
}
