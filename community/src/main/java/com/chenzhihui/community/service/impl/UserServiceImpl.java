package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.entity.LoginTicket;
import com.chenzhihui.community.mapper.LoginTicketMapper;
import com.chenzhihui.community.mapper.UserMapper;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.service.UserService;
import com.chenzhihui.community.util.CommunityConstant;
import com.chenzhihui.community.util.CommunityUtil;
import com.chenzhihui.community.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-19 23:43:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, CommunityConstant {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private LoginTicketMapper loginTicketMapper;


    @Autowired
    private MailUtil mailUtil;

    @Value("${community.path.domain}")
    private String domain;

    // 实现注册逻辑的具体实现累
    @Override
    public Map<String, Object> register(User user) throws MessagingException {

        HashMap<String, Object> map = new HashMap<>();
        // 1、检查user是否为空，为空抛出异常
        if(user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        // 2、判断user的username是否为空
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","username用户名不能为空");
            return map;
        }
        // 3、判断user的password是否为空
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","password密码不能为空");
            return map;
        }
        // 4、判断user的email是否为空
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","email邮箱不能为空");
            return map;
        }
        // 5、验证账号username是否已经存在
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg", "改用户名已被使用，请重新输入其他用户名");
            return map;
        }

        // 6、验证账号email是否已经注册
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg", "改邮箱已被注册，请重新选择");
            return map;
        }
        // 7、注册用户（利用user现在的信息，补充上需要填补的信息）-> 到这里意味这用户是可以注册的
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        System.out.println("先增的userid = " + user.getId());
        // 8、激活邮件逻辑处理 -> 注册成功之后，需要发送邮件给用户，叫用户去激活
        // 模版需要：xxxx 您好
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // 点击链接完成验证 :
        // todo：url这样拼接的作用是什么？
        String url = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailUtil.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    @Override
    public int activation(Integer id, String code) {
        User user = userMapper.selectById(id);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)){
            // todo: 实现通过id查找用户，修改用户激活状态
            userMapper.updateStatus(id, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }


    // 用户登录判断
    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {

        HashMap<String, Object> map = new HashMap<>();


        // 判断用户名和密码是否为空
        if(StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名为空！");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码为空！");
            return map;
        }

        User user = userMapper.selectByName(username);

        // 检查用户是否存在
        if(user == null){
            map.put("usernameMsg", "该账号不存在！");
            return map;
        }

        // 验证状态
        if(user.getStatus() == 0){
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        // 验证密码
        // 1、通过md5加密后，与用户本身存储在数据库的密码进行判断
        password  = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码错误！");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insert(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket,1);
    }

    @Override
    public int updateHeader(int id, String headerUrl) {
        return userMapper.updateHeader(id, headerUrl);
    }


    /**--------------------------------------------------------------------------------------------------------------**/

    @Override
    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }

    @Override
    public User selectByEmail(String email) {
        return null;
    }

    @Override
    public int insertUser(User user) {
        return 0;
    }

    @Override
    public int updateStatus(int id, int status) {
        return 0;
    }


    @Override
    public int updatePassword(int id, String password) {
        return 0;
    }
}

