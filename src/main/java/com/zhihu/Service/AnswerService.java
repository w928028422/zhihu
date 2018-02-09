package com.zhihu.Service;

import com.zhihu.DAO.AnswerDAO;
import com.zhihu.DAO.QuestionDAO;
import com.zhihu.Model.Answer;
import com.zhihu.Model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public List<Answer> getAnswersByQuestion(int questionId, int offset, int limit){
        return answerDAO.selectByQuestion(questionId, offset, limit);
    }

    public int addAnswer(String content, int questionId,  int userId){
        content = HtmlUtils.htmlEscape(content);
        Answer answer = new Answer();
        answer.setContent(sensitiveService.filter(content));
        answer.setCreatedDate(new Date());
        answer.setCommentCount(0);
        answer.setQuestionId(questionId);
        answer.setUserId(userId);
        answer.setStatus(0);
        questionDAO.updateAnswerCount(questionDAO.selectById(questionId).getAnswerCount() + 1, questionId);
        return answerDAO.addAnswer(answer);
    }

    public int getAnswerCountByUser(int userId){
        return answerDAO.getAnswerCountByUser(userId);
    }

    public boolean deleteComment(int answerId){
        return answerDAO.updateAnswer(answerId, 1) > 0;
    }

    public Answer getAnswer(int entityId) {
        return answerDAO.selectById(entityId);
    }

    public List<Integer> getIdsByUser(int userId){
        return answerDAO.selectIdByUser(userId);
    }
}
