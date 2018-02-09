package com.zhihu.Util;

public class RedisKeyUtil {
    private static String SPLIT = "-";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";
    private static String BIZ_FOLLOWER = "FOLLOWER";
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityId, int entityType){
        return BIZ_LIKE + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    public static String getDisLikeKey(int entityId, int entityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    public static String getTimeLineKey(int userId){
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

    public static String getFollowerKey(int entityId, int entityType){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
}
