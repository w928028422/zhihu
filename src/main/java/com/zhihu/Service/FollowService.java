package com.zhihu.Service;

import com.zhihu.Util.JedisAdapter;
import com.zhihu.Util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean follow(int userId, int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedisAdapter.multi(jedis);
        transaction.zadd(followerKey, date.getTime(), String.valueOf(userId));
        transaction.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(transaction, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
    }

    public boolean unfollow(int userId, int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedisAdapter.multi(jedis);
        transaction.zrem(followerKey, String.valueOf(userId));
        transaction.zrem(followeeKey, String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(transaction, jedis);
        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> stringSet){
        List<Integer> followers = new ArrayList<>();
        for(String str : stringSet){
            followers.add(Integer.valueOf(str));
        }
        return followers;
    }

    public List<Integer> getFollowers(int entityId, int entityType, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityId, int entityType, int offset, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, offset, count));
    }

    public List<Integer> getFollowees(int entityId, int entityType, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int entityId, int entityType, int offset, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, count));
    }

    public long getFollowerCount(int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return jedisAdapter.zcard(followerKey);
    }

    public long getFolloweeCount(int entityId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    public boolean isFollower(int userId, int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
