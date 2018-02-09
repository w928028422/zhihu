package com.zhihu.Controller;

import com.zhihu.Model.*;
import com.zhihu.Service.AnswerService;
import com.zhihu.Service.CommentService;
import com.zhihu.Service.QuestionService;
import com.zhihu.Util.ZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @RequestMapping(value = {"/addComment"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("entityId") int entityId,
                             @RequestParam("content") String content,
                             @RequestParam("entityType") int entityType){
        try{
            if(hostHolder.getUser() == null){
                return ZhihuUtil.getJSONString(1, "请登录后评论!");
            }
            Map<String, Object> map = new HashMap<>();
            Comment comment = commentService.addComment(content, entityId,
                    entityType, hostHolder.getUser().getId());
            commentService.updateCount(entityId, entityType);
            map.put("userId", hostHolder.getUser().getId());
            map.put("content", content);
            map.put("headUrl", hostHolder.getUser().getHeadUrl());
            map.put("createdDate", comment.getCreatedDate());
            return ZhihuUtil.getJSONString(0, map);
        }catch (Exception e){
            logger.error("添加评论失败!");
            return ZhihuUtil.getJSONString(1);
        }
    }
}
