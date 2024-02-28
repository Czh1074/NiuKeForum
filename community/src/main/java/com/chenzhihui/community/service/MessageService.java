package com.chenzhihui.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhihui.community.entity.Message;

import java.util.List;

/**
 * (Message)表服务接口
 *
 * @author makejava
 * @since 2023-05-19 23:46:29
 */
public interface MessageService extends IService<Message> {

    // 查询当前用户对会话列表，针对每个会话返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量（所有会话数量）
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表（相当于a和b之间的对话）
    List<Message> selectLetters(String ConversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量（会话的右侧，显示几条对话）
    int selectLetterCount(String ConversationId);

    // 查询未读私信的数量（左上角）
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息-发送私信
    int insertMessage(Message message);

    // 修改消息阅读状态
    int readMessage(List<Integer> ids);

    // 查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题下所包含的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知的数量
    int selectNoticeUnreadCount(int userId, String topic);

}

