package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Admin;
import edu.gdpu.ticket.service.AdminService;
import edu.gdpu.ticket.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 24100
* @description 针对表【admin】的数据库操作Service实现
* @createDate 2023-03-01 12:17:00
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 根据用户名和密码查询是否存在该 Admin
     *
     * @param username 用户名
     * @param password 密码
     * @return 查询到的 Admin 对象，如果不存在则返回 null
     */
    public Admin getAdminByUsernameAndPassword(String username, String password) {
       /* return adminMapper.selectAdminByUsernameAndPassword(username, password);*/
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username",username);
        adminQueryWrapper.eq("password",password);
        return adminMapper.selectOne(adminQueryWrapper);
    }


}




