package com.zhihu.Service;

import com.zhihu.DAO.LoginTicketDAO;
import com.zhihu.DAO.UserDAO;
import com.zhihu.Model.LoginTicket;
import com.zhihu.Model.User;
import com.zhihu.Util.ZhihuUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectUserById(id);
    }

    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            map.put("msg", "用户名或密码不能为空!");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg", "用户名不存在!");
            return map;
        }

        String passwd = ZhihuUtil.MD5(password + user.getSalt());
        if(!user.getPassword().equals(passwd)){
            map.put("msg", "密码错误!");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            map.put("msg", "用户名或密码不能为空!");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg", "用户名已经存在!");
            return map;
        }

        user = new User();
        user.setName(username);
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
        user.setPassword(ZhihuUtil.MD5(password + salt));
        user.setSalt(salt);
        user.setHeadUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 12);
        loginTicket.setExpired(date);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicket.setUserId(userId);
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }

}
