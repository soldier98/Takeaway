package com.soldier.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soldier.common.Result;
import com.soldier.domain.User;
import com.soldier.service.IUserService;
import com.soldier.utils.SMSUtils;
import com.soldier.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author soldier
 * @since 2022-07-18
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @param user
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //生成验证码并调用阿里云发送短信
     * @Date 2022/8/8
     **/
    @PostMapping("/sendMsg")
//    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
    public Result<String> sendMsg(@RequestBody User user) {
        String phone = user.getPhone();
        //判断手机号是否为空
        if (StringUtils.isNotEmpty(phone)) {
            Integer code00 =ValidateCodeUtils.generateValidateCode(4);
            String code=String.valueOf(code00);
            log.info("验证码" + code);
            //调用阿里云api发送验证码
//            SMSUtils.sendSMS("阿里云短信测试", "SMS_154950909", phone, code);
            //保存在session域，用作登录验证
//            session.setAttribute(phone, code);
            //将验证码保存在redis,并设置过期时间
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
            return new Result<>(1, "短信验证码发送成功", null);
        } else return new Result<>(0, "短信验证码发送失败", null);
    }

    /**
     * @param map
     * @return com.soldier.common.Result<java.lang.String>
     * @Author soldier
     * @Description //用户登录验证
     * 用Map封装前端传来的phone和code
     * @Date 2022/8/8
     **/
    @PostMapping("/login")
/**
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        //比对验证码
        if (session != null && code.equals(session.getAttribute(phone))) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User one = userService.getOne(userLambdaQueryWrapper);
            //如果手机号不在数据库中，则注册
            if (one == null) {
                User user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                one = userService.getOne(userLambdaQueryWrapper);
            }
            //将用户id存进session
            session.setAttribute("userId",one.getId());
            return new Result<User>(1,"",one);
        }else return new Result<>(0,"验证码错误",null);
    }
 **/
    public Result<User> login(@RequestBody Map map,HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        //比对验证码
        if (redisTemplate.opsForValue().get(phone) != null && code.equals(redisTemplate.opsForValue().get(phone))) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User one = userService.getOne(userLambdaQueryWrapper);
            //如果手机号不在数据库中，则注册
            if (one == null) {
                User user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                one = userService.getOne(userLambdaQueryWrapper);
            }
            //将用户id存进session
            session.setAttribute("userId",one.getId());

            //登录成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);
            return new Result<User>(1,"",one);
        }else return new Result<>(0,"验证码错误",null);
    }
}