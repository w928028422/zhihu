package com.zhihu.Controller;

import com.zhihu.Model.EntityType;
import com.zhihu.Model.Question;
import com.zhihu.Model.ViewObject;
import com.zhihu.Service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private FollowService followService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping(value = {"/search"}, method = {RequestMethod.POST})
    public String search(Model model, @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count",defaultValue = "10") int count){
        try{
            List<Question> questions = searchService.searchQuestion(keyword, offset, count, "<font color=\"red\">", "</font>");
            List<ViewObject> viewObjects = new ArrayList<>();
            for(Question question : questions){
                Question q = questionService.getQuestion(question.getId());
                ViewObject vo = new ViewObject();
                if(question.getContent() != null){
                    q.setContent(question.getContent());
                }
                if(question.getTitle() != null){
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("user", userService.getUser(q.getUserId()));
                vo.set("category", categoryService.getCategory(q.getCategoryId()));
                vo.set("followCount", followService.getFollowerCount(question.getId(), EntityType.QUESTION));
                viewObjects.add(vo);
            }
            model.addAttribute("vos", viewObjects);
        }catch (Exception e){
            logger.error("搜索错误!" + e.getMessage());
        }
        return "result";
    }
}
