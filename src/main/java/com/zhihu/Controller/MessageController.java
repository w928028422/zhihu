package com.zhihu.Controller;

import com.zhihu.Model.HostHolder;
import com.zhihu.Model.Message;
import com.zhihu.Model.User;
import com.zhihu.Model.ViewObject;
import com.zhihu.Service.MessageService;
import com.zhihu.Service.UserService;
import com.zhihu.Util.ZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @RequestMapping(value = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try {
            if (hostHolder.getUser() == null) {
                return ZhihuUtil.getJSONString(1, "请登录后发私信!");
            }
            User user = userService.getUserByName(toName);
            if (user == null) {
                return ZhihuUtil.getJSONString(1, "收信人不存在!");
            }
            messageService.addMessage(HtmlUtils.htmlEscape(content), hostHolder.getUser().getId(), user.getId());
            return ZhihuUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("发信错误" + e.getMessage());
            return ZhihuUtil.getJSONString(1, "发信错误!");
        }
    }

    @RequestMapping(value = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversations(Model model){
        try{
            List<ViewObject> viewObjects = new ArrayList<>();
            List<Message> messages = messageService.getConversationList(hostHolder.getUser().getId(), 0, 10);
            for(Message message : messages){
                ViewObject vo = new ViewObject();
                vo.set("conversation", message);
                if(hostHolder.getUser().getId() == message.getFromId()){
                    vo.set("user", userService.getUser(message.getToId()));
                }else{
                    vo.set("user", userService.getUser(message.getFromId()));
                }
                vo.set("unRead", messageService.getUnReadCount(hostHolder.getUser().getId(), message.getConversationId()));
                viewObjects.add(vo);
            }
            model.addAttribute("conversations", viewObjects);
        }catch (Exception e){
            logger.error("");
        }
        return "letter";
    }

    @RequestMapping(value = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(@RequestParam("conversationId") String conversationId, Model model){
        try {
            List<ViewObject> viewObjects = new ArrayList<>();
            List<Message> messages = messageService.getConversationDetail(conversationId, 0, 10);
            for(Message message : messages){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("headUrl", userService.getUser(message.getFromId()).getHeadUrl());
                viewObjects.add(vo);
            }
            model.addAttribute("messages", viewObjects);
        }catch (Exception e){
            logger.error("");
        }
        return "letterDetail";
    }
}
