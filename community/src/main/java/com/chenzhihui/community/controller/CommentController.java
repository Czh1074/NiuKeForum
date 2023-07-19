package com.chenzhihui.community.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenzhihui.community.annotation.LoginRequired;
import com.chenzhihui.community.entity.Comment;
import com.chenzhihui.community.service.CommentService;
import com.chenzhihui.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (Comment)表控制层
 * todo：删除评论 -> 如果是回复的话直接删除；如果是评论的话设置该评论已被删除，保留回复
 * @author makejava
 * @since 2023-05-19 23:46:44
 */
@Controller
@RequestMapping("comment")
public class CommentController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    @Resource
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(value = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + discussPostId;
    }










    /**--------------------------------------------------------------------------------------------------------------**/


    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param comment 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Comment> page, Comment comment) {
        return success(this.commentService.page(page, new QueryWrapper<>(comment)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.commentService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param comment 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody Comment comment) {
        return success(this.commentService.save(comment));
    }

    /**
     * 修改数据
     *
     * @param comment 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Comment comment) {
        return success(this.commentService.updateById(comment));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.commentService.removeByIds(idList));
    }
}

