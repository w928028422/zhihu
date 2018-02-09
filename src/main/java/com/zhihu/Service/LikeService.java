package com.zhihu.Service;

import com.zhihu.Model.EntityType;
import com.zhihu.Util.JedisAdapter;
import com.zhihu.Util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private AnswerService answerService;

    public int getLikeStatus(int userId, int entityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(disLikeKey, String.valueOf(userId))){
            return -1;
        }
        return 0;
    }

    public long like(int userId, int entityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long likeCount(int enityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(enityId, entityType);
        return jedisAdapter.scard(likeKey);
    }

    public int likeCountByUser(int userId) {
        List<Integer> answerIds = answerService.getIdsByUser(userId);
        int agreeCount = 0;
        for (Integer answerId : answerIds) {
            agreeCount += likeCount(answerId, EntityType.ANSWER);
        }
        return agreeCount;
    }
}
