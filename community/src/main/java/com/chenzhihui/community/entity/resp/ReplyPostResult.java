package com.chenzhihui.community.entity.resp;

import java.util.Date;

/**
 * 回复：帖子 + 评论
 *
 * @Author: ChenZhiHui
 * @DateTime: 2024/2/29 18:39
 **/
public class ReplyPostResult {
    // 帖子id
    private int postId;
    // 帖子标题
    private String postTitle;
    // 帖子回复内容
    private String replyContent;
    // 回复时间
    private Date replyCreateTime;

    public ReplyPostResult(int postId, String postTitle, String replyContent, Date replyCreateTime) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.replyContent = replyContent;
        this.replyCreateTime = replyCreateTime;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getReplyCreateTime() {
        return replyCreateTime;
    }

    public void setReplyCreateTime(Date replyCreateTime) {
        this.replyCreateTime = replyCreateTime;
    }

    public ReplyPostResult() {
    }

    @Override
    public String toString() {
        return "ReplyPostResult{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", replyCreateTime=" + replyCreateTime +
                '}';
    }
}
