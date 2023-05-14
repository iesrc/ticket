package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Passenger;
import edu.gdpu.ticket.entity.PassengerVO;
import edu.gdpu.ticket.mapper.PassengerMapper;
import edu.gdpu.ticket.mapper.PassengerVOMapper;
import edu.gdpu.ticket.service.PassengerService;
import edu.gdpu.ticket.service.PassengerVOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @author:薛梦婷
 * @date 2023/3/17 20:16
 */
@Service
public class PassengerVOServiceImpl extends ServiceImpl<PassengerVOMapper, PassengerVO>
        implements PassengerVOService {

    @Autowired
    private PassengerVOMapper passengerVOMapper;
    @Override
    public List<PassengerVO> listWithUsername(String username, String name) {
        return getBaseMapper().selectWithUsername(username, name);
    }
}
