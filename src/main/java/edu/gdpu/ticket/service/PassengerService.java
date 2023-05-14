package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.Passenger;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 24100
* @description 针对表【passenger】的数据库操作Service
* @createDate 2023-03-07 14:04:48
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spring的事务
public interface PassengerService extends IService<Passenger> {
    //根据用户id和输入的姓名查询所有乘客
    List<Passenger> findByUserIdAndNameInput(Integer userId,String nameInput);

    //根据用户id和乘客身份证号码查询乘客是否已经存在
    boolean isIdentityExist(Integer userId,String identity);

    //根据用户id和乘客id查询乘客
    Passenger findByUserIdAndPassengerId(Integer userId, Integer passengerId);

    //根据用户名和乘客姓名查询乘客列表
    List<Passenger> listWithUsername(String username, String name);

    //根据用户id查询所有乘客
    List<Passenger> findByUserId(Integer userId);

    //根据乘客id数组查询乘客
    List<Passenger> selectPassengersByIds(List<Integer> passengerIds);
}
