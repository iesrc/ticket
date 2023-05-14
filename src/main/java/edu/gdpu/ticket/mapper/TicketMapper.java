package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.Ticket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.gdpu.ticket.entity.TicketVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【ticket】的数据库操作Mapper
* @createDate 2023-03-28 18:38:08
* @Entity edu.gdpu.ticket.entity.Ticket
*/
@Component
public interface TicketMapper extends BaseMapper<Ticket> {

    @Select("SELECT t.*, r.departure, r.destination, s.departure_time, s.bus_number, p.name," +
            "s.price,s.duration " +
            "FROM ticket t " +
            "INNER JOIN t_order o ON t.order_id = o.order_id " +
            "INNER JOIN passenger p ON t.passenger_id = p.passenger_id " +
            "LEFT JOIN schedule s ON o.schedule_id = s.schedule_id " +
            "LEFT JOIN route r ON s.route_id = r.route_id " +
            "WHERE o.user_id = #{userId} AND t.is_deleted = 0 " +
            "AND r.departure LIKE CONCAT('%', #{departureInput}, '%') " +
            "AND r.destination LIKE CONCAT('%', #{destinationInput}, '%') " +
            "AND (#{departureTimeInput} IS NULL OR DATE(s.departure_time) = DATE(#{departureTimeInput})) order by t.ticket_id desc\n"
    )
    @Results({// 其他属性的映射
            @Result(column = "schedule_id", property = "schedule.scheduleId"),
            @Result(column = "price", property = "schedule.price"),
            @Result(column = "duration", property = "schedule.duration"),
            @Result(column = "bus_number", property = "schedule.busNumber"),
            @Result(column = "departure_time", property = "schedule.departureTime"),
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination"),
            @Result(column = "passenger_id", property = "passenger.passengerId"),
            @Result(column = "name", property = "passenger.name")
    })
    List<TicketVO> listByUserId(@Param("userId") Integer userId,@Param("departureInput") String departureInput,
                                @Param("destinationInput") String destinationInput,@Param("departureTimeInput") Date departureTimeInput);

    @Select("SELECT t.*, r.departure, r.destination, s.departure_time, s.bus_number, p.name,\n" +
            "s.price, s.duration, u.username\n" +
            "FROM ticket t\n" +
            "INNER JOIN t_order o ON t.order_id = o.order_id\n" +
            "INNER JOIN passenger p ON t.passenger_id = p.passenger_id\n" +
            "LEFT JOIN schedule s ON o.schedule_id = s.schedule_id\n" +
            "LEFT JOIN route r ON s.route_id = r.route_id\n" +
            "LEFT JOIN user u ON o.user_id = u.user_id\n" +
            "WHERE t.is_deleted = 0\n" +
            "AND p.name LIKE CONCAT('%', #{departureInput}, '%')\n" +
            "AND u.username LIKE CONCAT('%', #{destinationInput}, '%')\n" +
            "AND (#{departureTimeInput} IS NULL OR DATE(s.departure_time) = DATE(#{departureTimeInput}))\n order by t.ticket_id desc\n"
    )
    @Results({// 其他属性的映射
            @Result(column = "schedule_id", property = "schedule.scheduleId"),
            @Result(column = "price", property = "schedule.price"),
            @Result(column = "duration", property = "schedule.duration"),
            @Result(column = "bus_number", property = "schedule.busNumber"),
            @Result(column = "departure_time", property = "schedule.departureTime"),
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination"),
            @Result(column = "passenger_id", property = "passenger.passengerId"),
            @Result(column = "name", property = "passenger.name"),
            @Result(column = "username", property = "user.username")
    })
    List<TicketVO> findAllTickets(@Param("departureInput") String departureInput,
                                  @Param("destinationInput") String destinationInput,@Param("departureTimeInput") Date departureTimeInput);


    @Update("UPDATE ticket SET is_deleted = 1 WHERE ticket_id = #{ticketId}")
    boolean updateTicketById(@Param("ticketId") Integer ticketId);
}




