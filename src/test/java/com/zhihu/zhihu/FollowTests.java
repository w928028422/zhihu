package com.zhihu.zhihu;

import com.zhihu.Model.EntityType;
import com.zhihu.Service.FollowService;
import com.zhihu.ZhihuApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ZhihuApplication.class)
public class FollowTests {
    @Autowired
    private FollowService followService;

    @Test
    public void test(){
        for (int i = 1; i < 20; i++) {
            for (int j = 1; j < 5; j++) {
                followService.follow(i, j + 1, EntityType.USER);
            }
            followService.follow(i, 2 * i, EntityType.QUESTION);
        }
    }
}
