package com.chenzhihui.community.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenzhihui.community.constant.CommunityConstant;
import com.chenzhihui.community.entity.Comment;
import com.chenzhihui.community.entity.DiscussPost;
import com.chenzhihui.community.entity.Pages;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.service.CommentService;
import com.chenzhihui.community.service.DiscussPostService;
import com.chenzhihui.community.service.LikeService;
import com.chenzhihui.community.service.UserService;
import com.chenzhihui.community.util.CommunityUtil;
import com.chenzhihui.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * (DiscussPost)表控制层
 *
 * @author makejava
 * @since 2023-05-19 23:47:05
 */
@Controller
@Slf4j
public class DiscussPostController extends ApiController implements CommunityConstant {
    /**
     * 服务对象
     */
    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Resource
    private LikeService likeService;



    // 首页展示
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String selectAllDiscussPost(Model model, Pages pages) {
        // 方法调用栈、SpringMVC会自动实例化Model和Page，并将Page注入Model中
        // 所以，在thymeleaf中可以直接访问Page这中对象的数据
        List<DiscussPost> list = discussPostService.selectDiscussPosts(149, pages.getCurrent(), pages.getLimit());
        // 这里的rows需要查找所有的信息
        int all = discussPostService.selectAllDiscussPosts(149).size();
        pages.setRows(all);
        System.out.println(all);
        pages.setPath("/index");

        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost discussPost : list){
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.selectById(discussPost.getUserId());
                map.put("user", user);
                // 点赞数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

    @RequestMapping(value = "/discussPost/add", method = RequestMethod.POST)
    @ResponseBody
    public String insertDiscussPost(String title, String content) {
        // 1、获取当前用户
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(403, "您还未登录，清闲登录");
        }
        // 2、调用service层方法
        // 3、新建一个DiscussPost对象
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.insertDiscussPost(discussPost);
        return CommunityUtil.getJsonString(0, "发布成功！");
    }

    @RequestMapping(value = "/discuss/detail/{id}", method = RequestMethod.GET)
    public String selectDiscussPostById(@PathVariable("id") int id, Model model, Pages pages) {
        // 通过discussPostId查找得到帖子信息
        DiscussPost discussPost = discussPostService.selectDiscussPostById(id);
        model.addAttribute("post",discussPost);
        // 作者
        User user = userService.selectById(discussPost.getUserId());
        model.addAttribute("user", user);
        // 点赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, id);
        model.addAttribute("likeCount", likeCount);
        // 点赞状态:
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, id);
        model.addAttribute("likeStatus", likeStatus);
        // 评论分页设置
        pages.setRows(discussPost.getCommentCount());
        pages.setPath("/discuss/detail/" + id);
        model.addAttribute("pages", pages);

        // 我们统一定义：帖子的评论成为评论、评论的评论成为回复
        // 评论列表 -> 需要分页处理
        List<Comment> commentList = commentService.selectCommentsByEntity(ENTITY_TYPE_POST, id,
                pages.getCurrent(), pages.getLimit());

        // 评论VO列表，也就是不仅仅是评论信息还包含用户对象等的信息
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment : commentList) {
                // 评论Vo
                HashMap<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user",userService.selectById(comment.getUserId()));
                // 评论的点赞
                long commentLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getEntityId());
                commentVo.put("commentLikeCount", commentLikeCount);
                // 点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getEntityId());
                commentVo.put("likeStatus", likeStatus);
                // 回复列表
                List<Comment> replyList = commentService.selectCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(),
                        0, Integer.MAX_VALUE);
                // 回复Vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null) {
                    for(Comment reply : replyList) {
                        // 回复Vo
                        HashMap<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 回复的点赞
                        long replyLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getEntityId());
                        replyVo.put("replyLikeCount", replyLikeCount);
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getEntityId());
                        replyVo.put("likeStatus", likeStatus);
                        // 作者
                        replyVo.put("user", userService.selectById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == null ? null : userService.selectById(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                // 回复数量
                int replyCount = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getEntityId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);


        return "/site/discuss-detail";
    }


    /**--------------------------------------------------------------------------------------------------------------**/


    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param discussPost 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<DiscussPost> page, DiscussPost discussPost) {
        return success(this.discussPostService.page(page, new QueryWrapper<>(discussPost)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.discussPostService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param discussPost 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody DiscussPost discussPost) {
        return success(this.discussPostService.save(discussPost));
    }

    /**
     * 修改数据
     *
     * @param discussPost 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody DiscussPost discussPost) {
        return success(this.discussPostService.updateById(discussPost));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.discussPostService.removeByIds(idList));
    }
}

