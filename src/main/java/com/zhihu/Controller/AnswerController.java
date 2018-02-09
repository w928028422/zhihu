package com.zhihu.Controller;

import com.zhihu.Model.HostHolder;
import com.zhihu.Service.AnswerService;
import com.zhihu.Service.QuestionService;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventProducer;
import com.zhihu.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

    @RequestMapping(value = {"/addAnswer"}, method = {RequestMethod.POST})
    public String addAnswer(@RequestParam("questionId") int questionId,
                            @RequestParam("content") String content){
        try {
            if (hostHolder.getUser() == null) {
                return "redirect:/reglogin";
            }
            answerService.addAnswer(content, questionId, hostHolder.getUser().getId());
            eventProducer.fireEvent(new EventModel(EventType.ANSWER)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(questionId)
            );
        }catch (Exception e){
            logger.error("添加回答失败");
        }
        return "redirect:/question/" + questionId;
    }
}
