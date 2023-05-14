package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
* @author 24100
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-03-01 09:56:09
* @Entity edu.gdpu.ticket.entity.User
*/
@Component
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名和密码查询用户信息
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    @Select("select * from user where username = #{username} and password = #{password}")
    User selectUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 查询是否已存在该手机号
     * @param phone 手机号码
     * @return
     */
    @Select("SELECT count(*) FROM user WHERE phone = #{phone}")
    int countByPhone(@Param("phone") String phone);

    /**
     * 查询是否已存在该邮箱
     * @param email 邮箱
     * @return
     */
    @Select("SELECT count(*) FROM user WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    /**
     * 查询是否已存在该用户名
     * @param username 用户名
     * @return
     */
    @Select("SELECT count(*) FROM user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
}




