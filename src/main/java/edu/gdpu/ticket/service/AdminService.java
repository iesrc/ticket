package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 24100
* @description 针对表【admin】的数据库操作Service
* @createDate 2023-03-01 12:17:00
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spring的事务
public interface AdminService extends IService<Admin> {


    Admin getAdminByUsernameAndPassword(String username, String password);
}
