package edu.gdpu.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.gdpu.ticket.entity.PassengerVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spring的事务
public interface PassengerVOService extends IService<PassengerVO> {
    //根据用户名和乘客姓名查询乘客列表
    List<PassengerVO> listWithUsername(String username, String name);
}
