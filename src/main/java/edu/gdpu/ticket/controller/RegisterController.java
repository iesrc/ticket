package edu.gdpu.ticket.controller;

import edu.gdpu.ticket.entity.User;
import edu.gdpu.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/3/2 14:36
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @RequestMapping("/register1")
    public String showRegisterPage1() {
        return "register1";
    }

    /**
     * 用户注册方法
     * @param user
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/doRegister")
    public Map<String, Object> doRegister(User user, Model model){
        System.out.println("获取的user:" + user);
        // 验证username是否已存在
        if(userService.isUsernameExist(user.getUsername()) || user.getUsername() == ""){
            Map<String, Object> result = new HashMap<>();
            result.put("success",false);
            result.put("msg","用户名已存在");
            return result;

        }
        // 验证phone是否已存在
        if(userService.isPhoneExist(user.getPhone()) || user.getPhone() == ""){
            Map<String, Object> result = new HashMap<>();
            result.put("success",false);
            result.put("msg","手机号已存在");
            return result;
        }
        // 验证email是否已存在
        if(userService.isEmailExist(user.getEmail()) || user.getEmail() == ""){
            Map<String, Object> result = new HashMap<>();
            result.put("success",false);
            result.put("msg","邮箱已存在");
            return result;
        }

        // 插入新用户
        userService.addUser(user);

        Map<String, Object> result = new HashMap<>();
        result.put("success",true);
        result.put("msg","注册成功");
        return result;
    }
}
