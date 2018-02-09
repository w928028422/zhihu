package com.zhihu.Service;

import com.zhihu.DAO.FeedDAO;
import com.zhihu.Model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedService {
    @Autowired
    private FeedDAO feedDAO;

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    public int addFeed(int userId, int type, String data){
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(type);
        feed.setUserId(userId);
        feed.setData(data);
        feedDAO.addFeed(feed);
        return feed.getId();
    }

    public Feed getFeedById(int id){
        return feedDAO.selectById(id);
    }

    public List<Feed> getFeedsByUser(int userId, int offset, int limit){
        return feedDAO.selectByUserId(userId, offset, limit);
    }
}
