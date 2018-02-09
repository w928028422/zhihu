package com.zhihu.Controller;

import com.zhihu.Model.Answer;
import com.zhihu.Model.EntityType;
import com.zhihu.Model.HostHolder;
import com.zhihu.Service.AnswerService;
import com.zhihu.Service.LikeService;
import com.zhihu.Util.ZhihuUtil;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventProducer;
import com.zhihu.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("entityId") int entityId, @RequestParam("entityType") int entityType){
        if(hostHolder.getUser() == null){
            return ZhihuUtil.getJSONString(999, "请登录后点赞!");
        }
        Answer answer = answerService.getAnswer(entityId);
        String answerName = answer.getContent();
        if(answerName.length() > 20) {
            answerName = answer.getContent().substring(0, 20) + "...";
        }
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(entityId)
                .setEntityOwnerId(answerService.getAnswer(entityId).getUserId())
                .setEntityType(EntityType.ANSWER)
                .setExt("questionId", String.valueOf(answer.getQuestionId()))
                .setExt("answerName", answerName)
        );
        long likeCount = likeService.like(hostHolder.getUser().getId(), entityId, entityType);
        return ZhihuUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("entityId") int entityId, @RequestParam("entityType") int entityType){
        if(hostHolder.getUser() == null){
            return ZhihuUtil.getJSONString(999, "请登录后点赞!");
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), entityId, entityType);
        return ZhihuUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
