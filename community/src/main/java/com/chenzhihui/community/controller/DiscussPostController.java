package com.chenzhihui.community.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenzhihui.community.entity.DiscussPost;
import com.chenzhihui.community.entity.Pages;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.service.DiscussPostService;
import com.chenzhihui.community.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (DiscussPost)表控制层
 *
 * @author makejava
 * @since 2023-05-19 23:47:05
 */
@Controller
public class DiscussPostController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;


    // 首页展示
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String selectAllDiscussPost(Model model, Pages pages){
        // 方法调用栈、SpringMVC会自动实例化Model和Page，并将Page注入Model中
        // 所以，在thymeleaf中可以直接访问Page这中对象的数据
        List<DiscussPost> list = discussPostService.selectDiscussPosts(103, pages.getCurrent(), pages.getLimit());
        // 这里的rows需要查找所有的信息
        int all = discussPostService.selectAllDiscussPosts(103).size();
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
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }




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

