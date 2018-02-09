package com.zhihu.Service;

import com.zhihu.DAO.AnswerDAO;
import com.zhihu.DAO.CommentDAO;
import com.zhihu.DAO.QuestionDAO;
import com.zhihu.Model.Comment;
import com.zhihu.Model.EntityType;
import com.zhihu.Model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType, int offset, int limit){
        return commentDAO.selectByEntity(entityId, entityType, offset, limit);
    }

    public Comment addComment(String content, int entityId, int entityType, int userId){
        Comment comment = new Comment();
        comment.setCreatedDate(new Date());
        comment.setEntityId(entityId);
        comment.setEntityType(entityType);
        comment.setStatus(0);
        comment.setUserId(userId);
        content = HtmlUtils.htmlEscape(content);
        comment.setContent(sensitiveService.filter(content));
        commentDAO.addComment(comment);
        return comment;
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId){
        return commentDAO.updateComment(commentId, 1) > 0;
    }

    public void updateCount(int entityId, int entityType){
        if(entityType == EntityType.QUESTION_COMMENT){
            questionDAO.updateCommentCount(questionDAO.selectById(entityId).getCommentCount(), entityId);
        }else if(entityType == EntityType.ANSWER_COMMET){
            answerDAO.updateCommentCount(questionDAO.selectById(entityId).getCommentCount(), entityId);
        }
    }
}
