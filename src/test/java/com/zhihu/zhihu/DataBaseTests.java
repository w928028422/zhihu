package com.zhihu.zhihu;

import com.zhihu.DAO.QuestionDAO;
import com.zhihu.DAO.UserDAO;
import com.zhihu.Model.Question;
import com.zhihu.Model.User;
import com.zhihu.Util.ZhihuUtil;
import com.zhihu.ZhihuApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZhihuApplication.class)
public class DataBaseTests {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Test
    public void test(){
        Random random = new Random();
        User user = new User();
        String salt = UUID.randomUUID().toString().replaceAll("-","").substring(0,5);
        user.setName("admin");
        user.setSalt(salt);
        user.setPassword(ZhihuUtil.MD5("admin" + salt));
        user.setHeadUrl(String.format("https://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        userDAO.addUser(user);
        for (int i = 0; i < 30; i++) {
            User u = new User();
            u.setName("User" + String.valueOf(i));
            u.setSalt(UUID.randomUUID().toString().replaceAll("-","").substring(0,5));
            u.setPassword(ZhihuUtil.MD5("User" + String.valueOf(i) + u.getSalt()));
            u.setHeadUrl(String.format("https://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            userDAO.addUser(u);
            for (int j = 0; j < 3; j++) {
                Question question = new Question();
                question.setCategoryId(1);
                question.setCommentCount(0);
                question.setTitle("第一次" +
                        ZhihuUtil.TIYANS[random.nextInt(ZhihuUtil.TIYANS.length)] + "是一种什么体验？");
                question.setCreatedDate(new Date());
                question.setContent("本人从未体验过" + ZhihuUtil.TIYANS[random.nextInt(ZhihuUtil.TIYANS.length)]
                        + ",想请各位答主分享一下自己的经历");
                question.setUserId(u.getId());
                questionDAO.addQuestion(question);
            }
        }
    }
}
