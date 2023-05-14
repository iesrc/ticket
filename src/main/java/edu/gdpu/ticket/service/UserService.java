package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
* @author 24100
* @description 针对表【user】的数据库操作Service
* @createDate 2023-03-01 09:56:09
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spring的事务
public interface UserService extends IService<User> {

    //通过用户名和密码查询用户是否存在并返回用户
    User getUserByUsernameAndPassword(String username, String password);

    //查询是否已存在该手机号
    boolean isPhoneExist(String phone);

    //查询是否已存在该邮箱
    boolean isEmailExist(String email);

    //查询是否已存在该用户名
    boolean isUsernameExist(String username);

    //新增用户
    int addUser(User user);


    void export(ServletOutputStream outputStream);

    //根据搜索框内容查询用户
    List<User> findAllUsers(String nameInput);
}
