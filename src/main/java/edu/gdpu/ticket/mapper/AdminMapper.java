package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
* @author 24100
* @description 针对表【admin】的数据库操作Mapper
* @createDate 2023-03-01 12:17:00
* @Entity edu.gdpu.ticket.entity.Admin
*/
@Component
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据用户名和密码查询是否存在该 Admin
     *
     * @param username 用户名
     * @param password 密码
     * @return 查询到的 Admin 对象，如果不存在则返回 null
     */
    @Select("select * from admin where username = #{username} and password = #{password}")
    Admin selectAdminByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}




