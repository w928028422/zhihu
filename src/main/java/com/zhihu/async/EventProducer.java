package com.zhihu.async;

import com.alibaba.fastjson.JSON;
import com.zhihu.Util.JedisAdapter;
import com.zhihu.Util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {
    @Autowired
    private JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    public boolean fireEvent(EventModel model){
        try {
            String value = JSON.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, value);
            return true;
        }catch (Exception e){
            logger.error("事件生产者出错!" + e.getMessage());
            return false;
        }
    }
}
