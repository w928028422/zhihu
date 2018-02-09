package com.zhihu.async.handler;


import com.zhihu.Service.SearchService;
import com.zhihu.async.EventHandler;
import com.zhihu.async.EventModel;
import com.zhihu.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler{
    @Autowired
    private SearchService searchService;

    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Override
    public void doHandle(EventModel model) {
        try{
            searchService.indexQuestion(model.getEntityId(), model.getExt("title"), model.getExt("content"));
        }catch (Exception e){
            logger.error("添加索引错误!" + e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
