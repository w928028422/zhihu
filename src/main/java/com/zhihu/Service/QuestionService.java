package com.zhihu.Service;

import com.zhihu.DAO.CategoryDAO;
import com.zhihu.DAO.QuestionDAO;
import com.zhihu.Model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public List<Question> getQuestions(int userId, int offset, int limit){
        if(userId == 0) {
            return questionDAO.selectLatestQuestions(offset, limit);
        }
        else {
            return questionDAO.selectByUserId(userId, offset, limit);
        }
    }

    public int addQuestion(String title, String content, int userId, int categoryId){
        title = HtmlUtils.htmlEscape(title);
        content = HtmlUtils.htmlEscape(content);
        Question question = new Question();
        question.setTitle(sensitiveService.filter(title));
        question.setContent(sensitiveService.filter(content));
        question.setCommentCount(0);
        question.setAnswerCount(0);
        question.setCreatedDate(new Date());
        question.setUserId(userId);
        question.setCategoryId(categoryId);
        categoryDAO.updateQuestionCount(categoryDAO.selectById(categoryId).getQuestionCount() + 1, categoryId);
        questionDAO.addQuestion(question);
        return question.getId();
    }

    public List<Question> getQuestionsByCategory(int categoryId, int offset, int limit){
        return questionDAO.selectByCategoryId(categoryId, offset, limit);
    }

    public Question getQuestion(int questionId){
        return questionDAO.selectById(questionId);
    }

}
