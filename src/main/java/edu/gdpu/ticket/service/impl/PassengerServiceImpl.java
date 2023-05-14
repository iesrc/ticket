package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Passenger;
import edu.gdpu.ticket.service.PassengerService;
import edu.gdpu.ticket.mapper.PassengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 24100
* @description 针对表【passenger】的数据库操作Service实现
* @createDate 2023-03-07 14:04:48
*/
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
    implements PassengerService{

    @Autowired
    private PassengerMapper passengerMapper;

    @Override
    public List<Passenger> findByUserIdAndNameInput(Integer userId,String nameInput) {
        QueryWrapper<Passenger> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .like("name", nameInput);
        return passengerMapper.selectList(queryWrapper);
    }

    /**
     * 根据用户id和乘客身份证号码查询乘客是否已经存在
     * @param userId
     * @param identity
     * @return true:存在 false:不存在
     */
    @Override
    public boolean isIdentityExist(Integer userId, String identity) {
        Passenger passenger = passengerMapper.getByUserIdAndIdentityPassenger(userId, identity);
        return passenger != null;
    }

    @Override
    public Passenger findByUserIdAndPassengerId(Integer userId, Integer passengerId) {
        QueryWrapper<Passenger> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("passenger_id", passengerId);
        return passengerMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Passenger> listWithUsername(String username, String name) {
        return getBaseMapper().selectWithUsername(username, name);
    }

    @Override
    public List<Passenger> findByUserId(Integer userId) {
        return getBaseMapper().selectByUserId(userId);
    }

    /**
     * 根据多个id查询乘客列表
     * @param passengerIds 乘客id列表
     * @return 乘客列表
     */
    @Override
    public List<Passenger> selectPassengersByIds(List<Integer> passengerIds) {
        return passengerMapper.selectPassengersByIds(passengerIds);
    }


}




