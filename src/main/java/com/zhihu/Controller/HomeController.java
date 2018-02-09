package com.zhihu.Controller;

import com.zhihu.Model.EntityType;
import com.zhihu.Model.HostHolder;
import com.zhihu.Model.Question;
import com.zhihu.Model.ViewObject;
import com.zhihu.Service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FeedService feedService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<ViewObject> viewObjects = new ArrayList<>();
        try{
            List<Question> questions = questionService.getQuestions(userId, offset, limit);
            for(Question question : questions){
                ViewObject vo = new ViewObject();
                vo.set("question", question);
                vo.set("user", userService.getUser(question.getUserId()));
                vo.set("category", categoryService.getCategory(question.getCategoryId()));
                vo.set("followCount", followService.getFollowerCount(question.getId(), EntityType.QUESTION));
                viewObjects.add(vo);
            }
        }catch (Exception e){
            logger.error("获取问题错误!" + e.getMessage());
        }
        return viewObjects;
    }

    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(@RequestParam(value = "pop", defaultValue = "0") int pop, Model model){
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    @RequestMapping(value = {"/user/{userId}"}, method = {RequestMethod.GET})
    public String userIndex(@PathVariable("userId") int userId, Model model){
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        ViewObject viewObject = new ViewObject();
        viewObject.set("user", userService.getUser(userId));
        viewObject.set("answerCount", answerService.getAnswerCountByUser(userId));
        viewObject.set("followerCount", followService.getFollowerCount(userId, EntityType.USER));
        viewObject.set("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
        if (hostHolder.getUser() != null) {
            viewObject.set("followed", followService.isFollower(hostHolder.getUser().getId(), userId, EntityType.USER));
        } else {
            viewObject.set("followed", false);
        }
        model.addAttribute("profileUser", viewObject);
        model.addAttribute("agreeCount", likeService.likeCountByUser(userId));
        model.addAttribute("feeds", feedService.getFeedsByUser(userId, 0, 10));
        return "profile";
    }

    @RequestMapping(value = {"/test"})
    @ResponseBody
    public String test(){
        return "Test OK!";
    }

}
