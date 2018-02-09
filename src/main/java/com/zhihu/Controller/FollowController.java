package com.zhihu.Controller;

import com.zhihu.Model.*;
import com.zhihu.Service.*;
import com.zhihu.Util.ZhihuUtil;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventProducer;
import com.zhihu.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return ZhihuUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), userId, EntityType.USER);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.USER).setEntityOwnerId(userId)
        );
        return ZhihuUtil.getJSONString(ret?0:1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.USER)));
    }

    @RequestMapping(value = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId){
        if(hostHolder.getUser() == null){
            return ZhihuUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), userId, EntityType.USER);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
                .setEntityType(EntityType.USER).setEntityOwnerId(userId)
        );
        return ZhihuUtil.getJSONString(ret?0:1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.USER)));
    }

    @RequestMapping(value = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return ZhihuUtil.getJSONString(999);
        }
        Question question = questionService.getQuestion(questionId);
        if(question == null){
            return ZhihuUtil.getJSONString(1, "问题不存在!");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), questionId, EntityType.QUESTION);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.QUESTION).setEntityOwnerId(question.getUserId())
                .setExt("questionName", question.getTitle())
        );
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(questionId, EntityType.QUESTION));
        return ZhihuUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(value = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        if(hostHolder.getUser() == null){
            return ZhihuUtil.getJSONString(999);
        }
        Question question = questionService.getQuestion(questionId);
        if(question == null){
            return ZhihuUtil.getJSONString(1, "问题不存在!");
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), questionId, EntityType.QUESTION);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
                .setEntityType(EntityType.QUESTION).setEntityOwnerId(question.getUserId())
                .setExt("questionName", question.getTitle())
        );
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(questionId, EntityType.QUESTION));
        return ZhihuUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(value = {"/user/{userId}/followees"}, method = {RequestMethod.GET})
    public String followees(@PathVariable("userId") int userId, Model model){
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.USER, 10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        }else{
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    @RequestMapping(value = {"/user/{userId}/followers"}, method = {RequestMethod.GET})
    public String followers(@PathVariable("userId") int userId, Model model){
        List<Integer> followerIds = followService.getFollowers(userId, EntityType.USER, 10);
        if(hostHolder.getUser() != null){
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        }else{
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(userId, EntityType.USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds){
        List<ViewObject> viewObjects = new ArrayList<>();
        for(Integer userId : userIds){
            User user = userService.getUser(userId);
            if(user == null){
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("answerCount", answerService.getAnswerCountByUser(userId));
            vo.set("agreeCount", likeService.likeCountByUser(userId));
            vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
            vo.set("followerCount", followService.getFollowerCount(userId, EntityType.USER));
            if(localUserId != 0){
                vo.set("followed", followService.isFollower(localUserId, userId, EntityType.USER));
            }else {
                vo.set("followed", false);
            }
            viewObjects.add(vo);
        }
        return viewObjects;
    }
}
