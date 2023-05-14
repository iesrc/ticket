package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Ticket;
import edu.gdpu.ticket.entity.TicketVO;
import edu.gdpu.ticket.service.TicketService;
import edu.gdpu.ticket.mapper.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【ticket】的数据库操作Service实现
* @createDate 2023-03-28 18:38:08
*/
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket>
    implements TicketService{

    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public List<TicketVO> listByUserId(Integer userId, String departureInput, String destinationInput, Date departureTimeInput) {
        return ticketMapper.listByUserId(userId,departureInput,destinationInput,departureTimeInput);
    }

    @Override
    public List<TicketVO> findAllTickets(String departureInput, String destinationInput, Date departureTimeInput) {
        return ticketMapper.findAllTickets(departureInput,destinationInput,departureTimeInput);
    }

    @Override
    public boolean updateTicketById(Integer ticketId) {
        return ticketMapper.updateTicketById(ticketId);
    }
}




