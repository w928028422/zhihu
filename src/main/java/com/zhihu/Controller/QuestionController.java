package com.zhihu.Controller;

import com.zhihu.Model.*;
import com.zhihu.Service.*;
import com.zhihu.Util.ZhihuUtil;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventProducer;
import com.zhihu.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionController{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value = {"/question/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("category") String category){
        try{
            if(hostHolder.getUser() == null){
                return ZhihuUtil.getJSONString(999, "请登录后提问!");
            }
            int categoryId = categoryService.addCategory(category);
            int questionId = questionService.addQuestion(title, content, hostHolder.getUser().getId(), categoryId);
            eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(questionId)
                    .setExt("title", title).setExt("content", content)
            );
            return ZhihuUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加提问失败!" + e.getMessage());
            return ZhihuUtil.getJSONString(1, "添加提问失败!");
        }
    }

    @RequestMapping(value = {"/question/{questionId}"})
    public String questionDetail(@PathVariable("questionId") int questionId, Model model){
        Question question = questionService.getQuestion(questionId);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));
        model.addAttribute("category", categoryService.getCategory(question.getCategoryId()));
        List<ViewObject> viewObjects = new ArrayList<>();
        List<Answer> answers = answerService.getAnswersByQuestion(questionId, 0, 10);
        for(Answer answer : answers){
            ViewObject viewObject = new ViewObject();
            viewObject.set("answer", answer);
            viewObject.set("user", userService.getUser(answer.getUserId()));
            if(hostHolder.getUser() != null) {
                viewObject.set("liked", likeService.getLikeStatus(
                        hostHolder.getUser().getId(), answer.getId(), EntityType.ANSWER));
            }else{
                    viewObject.set("liked", 0);
            }
            viewObject.set("likeCount", likeService.likeCount(answer.getId(), EntityType.ANSWER));
            viewObjects.add(viewObject);
        }
        model.addAttribute("answers", viewObjects);
        List<ViewObject> followUsers = new ArrayList<>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(questionId, EntityType.QUESTION, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed",
                    followService.isFollower(hostHolder.getUser().getId(), questionId, EntityType.QUESTION));
        } else {
            model.addAttribute("followed", false);
        }
        return "detail";
    }

}
