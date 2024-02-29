package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.entity.Message;
import com.chenzhihui.community.mapper.MessageMapper;
import com.chenzhihui.community.service.MessageService;
import com.chenzhihui.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Message)表服务实现类
 *
 * @author makejava
 * @since 2023-05-19 23:46:29
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


    @Resource
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    // 查询当前用户对会话列表，针对每个会话返回一条最新的私信
    @Override
    public List<Message> selectConversations(int userId, int offset, int limit) {
        IPage<Message> page = new Page<>(offset, limit);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
//        queryWrapper.apply("id, from_id, to_id, conversation_id, content, status, create_time"); // 设置查询字段
        StringBuilder subQuery = new StringBuilder();
        subQuery.append("select max(id) from message where status != 2 and from_id != 1 ")
                .append("and (from_id = ").append(userId)
                .append(" or to_id = ").append(userId)
                .append(") group by conversation_id");
        queryWrapper.inSql("id", subQuery.toString()); // 设置查询条件
        queryWrapper.orderByDesc("id"); // 设置id字段降序
        IPage<Message> messageIPage = messageMapper.selectPage(page, queryWrapper);
        // 结果List
        ArrayList<Message> messages = new ArrayList<>();
        // 将结果转换为List<Message>的形式
        for(Message message :  messageIPage.getRecords()) {
            messages.add(message);
            System.out.println(message.getFromId() + " " + message.getContent());
        }
        return messages;
    }

    @Override
    public int selectConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    @Override
    public List<Message> selectLetters(String conversationId, int offset, int limit) {
        IPage<Message> page = new Page<>(offset, limit);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId);
        queryWrapper.ne("status",2);
        queryWrapper.ne("from_id",1);
        queryWrapper.orderByDesc("create_time");
        IPage<Message> messageIPage = messageMapper.selectPage(page, queryWrapper);
        // 结果List
        ArrayList<Message> messages = new ArrayList<>();
        // 将结果转换为List<Message>的形式
        for(Message message :  messageIPage.getRecords()) {
            messages.add(message);
            System.out.println(message.getFromId() + " " + message.getContent());
        }
        return messages;
    }

    @Override
    public int selectLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public int selectLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    // 新增私信
    @Override
    public int insertMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int readMessage(List<Integer> ids) {
        System.out.println("我先在执行messageService中的设置已读方法");
        return messageMapper.updateStatus(ids, 1);
    }

    @Override
    public Message selectLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    @Override
    public int selectNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    @Override
    public int selectNoticeUnreadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    @Override
    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }

}

