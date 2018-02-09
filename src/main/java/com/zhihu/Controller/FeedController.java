package com.zhihu.Controller;

import com.zhihu.Model.EntityType;
import com.zhihu.Model.Feed;
import com.zhihu.Model.HostHolder;
import com.zhihu.Service.FeedService;
import com.zhihu.Service.FollowService;
import com.zhihu.Util.JedisAdapter;
import com.zhihu.Util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @RequestMapping(value = {"/pullFeeds"}, method = {RequestMethod.GET})
    public String getPullFeeds(Model model){
        List<Integer> followees;
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        if(localUserId != 0){
            followees = followService.getFollowees(localUserId, EntityType.USER, Integer.MAX_VALUE);
            List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
            model.addAttribute("feeds", feeds);
        }
        return "feeds";
    }

    @RequestMapping(value = {"/pushFeeds"}, method = {RequestMethod.GET})
    public String getPushFeeds(Model model){
        List<Feed> feeds = new ArrayList<>();
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimeLineKey(localUserId), 0, 10);
        for(String feedId : feedIds){
            Feed feed = feedService.getFeedById(Integer.valueOf(feedId));
            if(feed != null){
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
