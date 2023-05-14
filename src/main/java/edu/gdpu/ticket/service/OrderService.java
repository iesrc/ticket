package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.gdpu.ticket.entity.OrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【order】的数据库操作Service
* @createDate 2023-03-29 13:25:42
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spri
public interface OrderService extends IService<Order> {

    boolean insertOrder(Order order);

    List<OrderVO> listByUserId(Integer userId, String departureInput, String destinationInput, Date departureTimeInput);

    List<OrderVO> findAllOrders(String usernameInput, Date payTimeInput);
}
