package edu.gdpu.ticket.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.User;
import edu.gdpu.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author iesrc
 * @since 2023-02-27
 */
@Controller
@RequestMapping("/ticket/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/rePassword")
    public String rePassword(){
        return "user_rePassword";
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/doRePassword")
    public Map<String,Object> doRePassword(@RequestParam("oldPassword") String oldPassword,
                                           @RequestParam("newPassword") String newPassword,
                                           @RequestParam("confirmPassword") String confirmPassword, HttpSession session){
/*        System.out.println("oldPassword" + oldPassword);
        System.out.println("newPassword" + newPassword);
        System.out.println("confirmPassword" + confirmPassword);*/
        Map<String,Object> map = new HashMap<>();
        User loginUser = (User) session.getAttribute("user");
        //新旧输入密码一致
        if (newPassword.equals(confirmPassword)){
            //获取当前用户
            User user = userService.getById(loginUser.getUserId());
            //判断旧密码是否输入正确
            if (user.getPassword().equals(oldPassword)){
                //输入正确，修改密码
                user.setPassword(newPassword);
                userService.updateById(user);
                map.put("success",true);
                map.put("msg","修改成功,请重新登录");
            }else {//旧密码输入错误
                map.put("success",false);
                map.put("msg","原密码错误");
            }
        }else {//新密码两次密输入不一致
            map.put("success",false);
            map.put("msg","两次密码不一致");
        }
        return map;
    }

    @RequestMapping("/findAllUsers")
    public ModelAndView findAllUsers(HttpSession session,@RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                                     @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                                     @RequestParam(name = "nameInput",required = false,defaultValue = "") String nameInput,
                                     @RequestParam(name = "pageTotal",required = false,defaultValue = "0") int pageTotal){
        System.out.println("nameInput" + nameInput);
        System.out.println("pageNo" + pageNo);
        System.out.println("pageSize" + pageSize);
        System.out.println("pageTotal" + pageTotal);
        ModelAndView mv = new ModelAndView();
        PageHelper.startPage(pageNo, pageSize);//开启分页
        List<User> userList = userService.findAllUsers(nameInput);
        mv.addObject("userList",userList);
        mv.addObject("UserNum",userList.size());
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        session.setAttribute("pageInfo",pageInfo);
        System.out.println("数据个数"+pageInfo.getTotal());
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        session.setAttribute("nameInput",nameInput);
        mv.setViewName("users");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/deleteUserById")
    public String deleteUserById(@RequestParam(name = "userId",required = false,defaultValue = "0") Integer userId){
        System.out.println("获取的userId：" + userId);
        if(userId != 0){
            userService.removeById(userId);
            return "SUCCESS";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping("/update")
    public Map<String,Object> update(User user){
            Map<String, Object> map = new HashMap<>();
        System.out.println("获取的user" + user);
        User oldUser = userService.getById(user.getUserId());
        System.out.println("数据库的oldUser" + oldUser);
        if(!user.getUsername().equals(oldUser.getUsername())){
            boolean usernameExist = userService.isUsernameExist(user.getUsername());
            if(usernameExist){
                map.put("success",false);
                map.put("msg","用户名已存在");
                return map;
            }
        }
        if(!user.getEmail().equals(oldUser.getEmail())){
            boolean emailExist = userService.isEmailExist(user.getEmail());
            if(emailExist){
                map.put("success",false);
                map.put("msg","邮箱已存在");
                return map;
            }
        }
        if(!user.getPhone().equals(oldUser.getPhone())){
            boolean phoneExist = userService.isPhoneExist(user.getPhone());
            if(phoneExist){
                map.put("success",false);
                map.put("msg","手机号已存在");
                return map;
            }
        }
        userService.updateById(user);
        map.put("success",true);
        map.put("msg","修改用户成功");
        return map;

    }

    @ResponseBody
    @RequestMapping("/add")
    public Map<String,Object> add(User user){
        Map<String, Object> map = new HashMap<>();
        System.out.println("获取的user" + user);
        boolean usernameExist = userService.isUsernameExist(user.getUsername());
        if(usernameExist){
            map.put("success",false);
            map.put("msg","用户名已存在");
            return map;
        }
        boolean emailExist = userService.isEmailExist(user.getEmail());
        if(emailExist){
            map.put("success",false);
            map.put("msg","邮箱已存在");
            return map;
        }
        boolean phoneExist = userService.isPhoneExist(user.getPhone());
        if(phoneExist){
            map.put("success",false);
            map.put("msg","手机号已存在");
            return map;
        }
        userService.save(user);
        map.put("success",true);
        map.put("msg","添加用户成功");
        return map;
    }


}
