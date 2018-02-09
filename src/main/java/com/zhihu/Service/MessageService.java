package com.zhihu.Service;

import com.zhihu.DAO.MessageDAO;
import com.zhihu.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public int addMessage(String content, int fromId, int toId){
        Message message = new Message();
        message.setContent(sensitiveService.filter(content));
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(String.format("%d_%d", Math.min(fromId, toId), Math.max(fromId, toId)));
        messageDAO.addMessage(message);
        return 0;
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getUnReadCount(int userId, String conversationId) {
        return messageDAO.getUnreadCount(userId, conversationId);
    }
}
