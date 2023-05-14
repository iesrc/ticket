package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.User;
import edu.gdpu.ticket.service.UserService;
import edu.gdpu.ticket.mapper.UserMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
* @author 24100
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-03-01 09:56:09
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;



    /**
     * 通过用户名和密码查询用户是否存在并返回用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户
     */
    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 查询是否已存在该手机号
     * @param phone 手机号码
     * @return
     */
    public boolean isPhoneExist(String phone) {
        return userMapper.countByPhone(phone) > 0;
    }

    /**
     * 查询是否已存在该邮箱
     * @param email 邮箱
     * @return
     */
    public boolean isEmailExist(String email){
        return userMapper.countByEmail(email) > 0;
    }

    /**
     * 查询是否已存在该用户名
     * @param username 用户名
     * @return
     */
    public boolean isUsernameExist(String username){
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public void export(ServletOutputStream outputStream) {
        // 查询所有用户数据
        List<User> userList = list();

        // 创建 Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet("用户表");
        // 设置单元格格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        // 创建单元格并设置表头
        HSSFRow header = sheet.createRow(0);
        String[] fields = {"ID", "用户名", "密码", "手机号码", "邮箱"};
        for (int i = 0; i < fields.length; i++) {
            HSSFCell cell = header.createCell(i);
            cell.setCellValue(fields[i]);
            cell.setCellStyle(style);
        }
        // 写入数据
        for (int i = 0; i < userList.size(); i++) {
            HSSFRow row = sheet.createRow(i + 1);
            User user = userList.get(i);
            row.createCell(0).setCellValue(user.getUserId());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getPassword());
            row.createCell(3).setCellValue(user.getPhone());
            row.createCell(4).setCellValue(user.getEmail());
        }

        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据搜索框内容查询用户
     * @param nameInput
     * @return  用户列表
     */
    @Override
    public List<User> findAllUsers(String nameInput) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", nameInput);
        return userMapper.selectList(queryWrapper);
    }

}




