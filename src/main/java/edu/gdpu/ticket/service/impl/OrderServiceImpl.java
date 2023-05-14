package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Order;
import edu.gdpu.ticket.entity.OrderVO;
import edu.gdpu.ticket.service.OrderService;
import edu.gdpu.ticket.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-03-29 13:25:42
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean insertOrder(Order order) {
        return orderMapper.insertOrder(order) ;
    }

    @Override
    public List<OrderVO> listByUserId(Integer userId, String departureInput, String destinationInput, Date departureTimeInput) {
        return orderMapper.listByUserId(userId,departureInput,destinationInput,departureTimeInput);
    }

    @Override
    public List<OrderVO> findAllOrders(String usernameInput, Date payTimeInput) {
        return orderMapper.findAllOrders(usernameInput,payTimeInput);
    }
}




