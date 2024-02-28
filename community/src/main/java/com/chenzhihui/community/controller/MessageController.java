package com.chenzhihui.community.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.chenzhihui.community.entity.Message;
import com.chenzhihui.community.entity.Pages;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.service.MessageService;
import com.chenzhihui.community.service.UserService;
import com.chenzhihui.community.util.CommunityUtil;
import com.chenzhihui.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * (Message)表控制层
 * todo：导航栏部分到消息没有实时显示，需要在用户登录的时候，就查询未查看的消息数量
 * @author makejava
 * @since 2023-05-19 23:46:29
 */
@Controller
public class MessageController extends ApiController {

    @Resource
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Pages pages) {
        User user = hostHolder.getUser(); // 获取当前用户
        pages.setPath("/letter/list");
        pages.setRows(messageService.selectConversationCount(user.getId()));
        model.addAttribute("pages", pages);

        // 分页查找会话列表
        List<Message> conversationList = messageService.selectConversations(user.getId(), pages.getCurrent(), pages.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if(conversationList != null){
            for(Message message : conversationList) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.selectLetterCount(message.getConversationId()));
                System.out.println("当前私信共有几条会话呢 = " + messageService.selectLetterCount(message.getConversationId()));
                // todo: 查找还未阅读的私信数量
                map.put("unreadCount", messageService.selectLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.selectById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        // 查询未读消息的数量
        int letterUnreadCount = messageService.selectLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "site/letter";
    }

    // 显示会话详情
    @RequestMapping(value = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Model model, Pages pages){
        // 分页显示设置
        pages.setPath("/site/detail/" + conversationId);
        pages.setRows(messageService.selectLetterCount(conversationId));
        pages.setLimit(6);

        // 得到会话内容列表
        List<Message> messageList = messageService.selectLetters(conversationId, pages.getCurrent(), pages.getLimit());
        // 补充其他信息：target用户
        List<Map<String, Object>> letters = new ArrayList<>();
        if(messageList != null){
            for(Message message : messageList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.selectById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信的目标
        model.addAttribute("target", getLetterTarget(conversationId));

        // 设置已读
        List<Integer> ids = getLetterIds(messageList);
        System.out.println("当前是否有未读的私信：" + ids.isEmpty());
        if(!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId) {
        String[] str = conversationId.split("_");
        int id1 = Integer.parseInt(str[0]);
        int id2 = Integer.parseInt(str[1]);
        if(hostHolder.getUser().getId() == id1) {
            return userService.selectById(id2);
        }
        return userService.selectById(id1);
    }

    // 通过当前用户评论or回复来判断，是否被访问，进行状态的修改
    private List<Integer> getLetterIds(List<Message> letterList) {
        ArrayList<Integer> ids = new ArrayList<>();
        if(letterList != null) {
            for(Message message : letterList) {
                if(hostHolder.getUser().getId().intValue() == message.getToId() && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }


    @RequestMapping(value = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content){
        User target = userService.selectByName(toName); // 判断目标在不在
        System.out.println("接收到的name是什么" + toName);
        if(target == null) {
            return CommunityUtil.getJsonString(1,"目标用户不存在！");
        }
        // 设置需要发送的消息
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if(message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.insertMessage(message);
        return CommunityUtil.getJsonString(0);
    }


    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知

        // 查询点赞类通知

        // 查询关注类通知
        return "true";
    }



    /**--------------------------------------------------------------------------------------------------------------**/



}

