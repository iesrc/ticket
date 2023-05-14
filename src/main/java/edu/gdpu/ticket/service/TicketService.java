package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.Ticket;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.gdpu.ticket.entity.TicketVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【ticket】的数据库操作Service
* @createDate 2023-03-28 18:38:08
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spri
public interface TicketService extends IService<Ticket> {

    List<TicketVO> listByUserId(Integer userId, String departureInput, String destinationInput, Date departureTimeInput);

    List<TicketVO> findAllTickets(String departureInput, String destinationInput, Date departureTimeInput);

    boolean updateTicketById(Integer ticketId);
}
