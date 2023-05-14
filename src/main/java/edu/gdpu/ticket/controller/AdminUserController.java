package edu.gdpu.ticket.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.gdpu.ticket.entity.User;
import edu.gdpu.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/3/10 13:52
 */
@Controller
@RequestMapping("/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize, Model model) {

        // 分页查询用户数据
        Page<User> page = new Page<>(pageNum, pageSize);
        userService.page(page);

        model.addAttribute("users", page.getRecords());
        model.addAttribute("total", page.getTotal());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        return "user";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public void add(User user) {
        userService.save(user);
    }

    @PostMapping("/update")
    @ResponseBody
    public void update(User user) {
        userService.updateById(user);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        userService.removeById(id);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        // 设置响应头信息，使浏览器以下载的方式打开文件
        response.setHeader("Content-Disposition", "attachment;filename=user.xls");
        response.setContentType("application/vnd.ms-excel");

        userService.export(response.getOutputStream());
    }

}
