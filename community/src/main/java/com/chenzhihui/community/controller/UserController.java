package com.chenzhihui.community.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenzhihui.community.annotation.LoginRequired;
import com.chenzhihui.community.entity.DiscussPost;
import com.chenzhihui.community.entity.Pages;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.entity.resp.ReplyPostResult;
import com.chenzhihui.community.service.*;
import com.chenzhihui.community.util.CommunityUtil;
import com.chenzhihui.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chenzhihui.community.constant.CommunityConstant.ENTITY_TYPE_POST;
import static com.chenzhihui.community.constant.CommunityConstant.ENTITY_TYPE_USER;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2023-05-19 23:42:59
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController extends ApiController {

    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @Resource
    private LikeService likeService;

    @Resource
    private FollowService followService;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private CommentService commentService;

    /**
     * 通过id查找用户
     *
     * @param id int 查询实体
     * @return User信息
     */
    @GetMapping("selectById/{id}")
    public User selectById(@PathVariable("id") int id) {
        System.out.println("id = " + id);
        return userService.selectById(id);
    }

    @LoginRequired
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getUserSettingPage(){
        System.out.println("有请求发送到：个人主页设置界面！");
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) throws IOException {
        // 1、判断上传的图片是否为空
        if (headerImage == null){
            model.addAttribute("error", "您还没有选择图片！");
            return "/site/setting";
        }
        // 2、得到文件名 -> 因为可能很多人存的文件是一样的，所以我们需要将该文件以一定的命名规则重新存储
        String filename = headerImage.getOriginalFilename();
        // 3、得到文件的尾部，文件格式
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error", "您上传的文件格式有误，请重新上传！");
            return "/site/setting";
        }
        // 4、组装成新的文件名
        filename = CommunityUtil.generateUUID() + suffix;
        // 5、确定文件存放的路径
        File file = new File(uploadPath + "/" + filename);
        // 6、存储文件
        headerImage.transferTo(file);

        // 7、存储完文件，就是要用户的头像 -> 前提：通过hostHolder获取当前用户
        // 格式：http://localhost:8080/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerurl = domain + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerurl);
        return "redirect:/index";
    }

    // 网站获取本地头像
    @RequestMapping(value = "/header/{filename}", method = RequestMethod.GET)
    public void getHeaderImage(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
        // 先拿到服务器的存放地址(现在是本地)
        filename = uploadPath + "/" + filename;
        // 文件后缀
        String suffix = filename.substring(filename.indexOf("."));
        // 响应图片: 现在响应的是图片，后缀名为suffix
        response.setContentType("image/" + suffix);
        // 指定我们要读取的内容
        FileInputStream fis = new FileInputStream(filename);
        // 获取响应对象的输出流
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int b = 0;
        while ( (b = fis.read(buffer)) != -1){
            os.write(buffer, 0, b);
        }
        fis.close();
        os.close();
    }

    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

    // 用户发布的帖子
    @RequestMapping(path = "/post/{userId}", method = RequestMethod.GET)
    public String getUserPosts(@PathVariable("userId") int userId, Model model, Pages page) {
        // 添加用户信息
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        // 帖子总数
        List<DiscussPost> discussPostList = discussPostService.selectAllDiscussPosts(userId);
        int postCount = discussPostList.size();
        model.addAttribute("postCount", postCount);

        // 分页相关参数
        page.setRows(postCount);
        page.setPath("/user/post/" + userId);

        // 主语显示：是我的帖子还是他的帖子
        String subject = "我";
        user = hostHolder.getUser();
        if (user == null || userId != user.getId()) {
            subject = "Ta";
        }

        // 小标题显示信息
        model.addAttribute("subject", subject);

        // 帖子信息
        List<DiscussPost> list = discussPostService.selectDiscussPosts(userId, page.getFrom(), page.getLimit(), 0);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("likeCount", post.getLikeCount());
                discussPosts.add(map);
            }
        }
        // 帖子相关信息
        model.addAttribute("discussPosts", discussPosts);

        return "/site/my-post";
    }

    // 用户回复的帖子
    @RequestMapping(path = "/reply/{userId}", method = RequestMethod.GET)
    public String getUserReply(@PathVariable("userId") int userId, Model model, Pages page) {
        // 添加用户信息
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        // 回复的帖子总数
        int postCount = commentService.findPostCommentCountByUserId(userId, ENTITY_TYPE_POST);
        model.addAttribute("postCount", postCount);

        // 分页相关参数
        page.setRows(postCount);
        page.setPath("/user/reply/" + userId);

        // 帖子及回复相关信息
        List<ReplyPostResult> list = discussPostService.findReplyDiscussPosts(userId, page.getFrom(), page.getLimit());
        model.addAttribute("replyPost", list);

        return "/site/my-reply";
    }



    //----------------------------------------------------------------------------------------------------------------//


    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<User> page, User user) {
        return success(this.userService.page(page, new QueryWrapper<>(user)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.userService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody User user) {
        return success(this.userService.save(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody User user) {
        return success(this.userService.updateById(user));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.userService.removeByIds(idList));
    }
}

