package com.zhihu.Controller;

import com.zhihu.Model.Category;
import com.zhihu.Model.EntityType;
import com.zhihu.Model.Question;
import com.zhihu.Model.ViewObject;
import com.zhihu.Service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private List<ViewObject> getQuestions(int categoryId, int offset, int limit) {
        List<ViewObject> viewObjects = new ArrayList<>();
        try{
            List<Question> questions = questionService.getQuestionsByCategory(categoryId, offset, limit);
            for(Question question : questions){
                ViewObject vo = new ViewObject();
                vo.set("question", question);
                vo.set("user", userService.getUser(question.getUserId()));
                vo.set("followCount", followService.getFollowerCount(question.getId(), EntityType.QUESTION));
                viewObjects.add(vo);
            }
        }catch (Exception e){
            logger.error("获取问题错误!" + e.getMessage());
        }
        return viewObjects;
    }
    @RequestMapping(value = "/categories/{categoryId}", method = {RequestMethod.GET})
    public String getCategoryQuesion(@PathVariable("categoryId") int categoryId, Model model){
        Category category = categoryService.getCategory(categoryId);
        model.addAttribute("vos", getQuestions(categoryId, 0, 10));
        model.addAttribute("category", category);
        return "category";
    }
}
