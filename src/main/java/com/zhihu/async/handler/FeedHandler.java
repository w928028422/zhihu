package com.zhihu.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zhihu.Model.Answer;
import com.zhihu.Model.EntityType;
import com.zhihu.Model.Question;
import com.zhihu.Model.User;
import com.zhihu.Service.*;
import com.zhihu.Util.JedisAdapter;
import com.zhihu.Util.RedisKeyUtil;
import com.zhihu.async.EventHandler;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedHandler implements EventHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdapter jedisAdapter;

    private String getData(EventModel model){
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if(actor == null){
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        if(model.getType() == EventType.ANSWER ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.QUESTION)){
            Question question = questionService.getQuestion(model.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }else if(model.getType() == EventType.LIKE && model.getEntityType() == EntityType.ANSWER){
            map.put("questionId", model.getExt("questionId"));
            map.put("answerName", model.getExt("answerName"));
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        String data = getData(model);
        if(data == null){
            return;
        }
        int feedId = feedService.addFeed(model.getActorId(), model.getType().getValue(), data);
        List<Integer> followers = followService.getFollowers(model.getActorId(), EntityType.USER, Integer.MAX_VALUE);
        followers.add(1);
        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimeLineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feedId));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.ANSWER, EventType.FOLLOW, EventType.LIKE});
    }
}
