package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.gdpu.ticket.entity.OrderVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【order】的数据库操作Mapper
* @createDate 2023-03-29 13:25:42
* @Entity edu.gdpu.ticket.entity.Order
*/
@Component
public interface OrderMapper extends BaseMapper<Order> {

    //@Options(useGeneratedKeys = true, keyProperty = "orderId"):
    // 表示开启自动生成主键并将生成的主键值赋予实体类属性 orderId。
    @Insert("INSERT INTO t_order (user_id, schedule_id, status, pay_time, count, money, is_deleted) " +
            "VALUES (#{userId}, #{scheduleId}, #{status}, #{payTime}, #{count}, #{money}, #{isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    boolean insertOrder(Order order);

    @Select("select o.*, r.departure, r.destination, s.departure_time, s.bus_number " +
            "from t_order o left join schedule s on o.schedule_id = s.schedule_id " +
            "left join route r on s.route_id = r.route_id " +
            "where o.user_id = #{userId} and o.is_deleted = 0 " +
            "AND r.departure LIKE CONCAT('%', #{departureInput}, '%') \n" +
            "AND r.destination LIKE CONCAT('%', #{destinationInput}, '%')\n" +
            "AND (#{departureTimeInput} is null OR DATE(o.pay_time) = DATE(#{departureTimeInput})) ORDER BY\n" +
            "o.pay_time DESC")
    @Results({
            @Result(column = "schedule_id", property = "schedule.scheduleId"),
            @Result(column = "bus_number", property = "schedule.busNumber"),
            @Result(column = "departure_time", property = "schedule.departureTime"),
            // 其他属性的映射
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination")
    })
    @Options(useGeneratedKeys=true, keyColumn="route_id, schedule_id", keyProperty="routeId,scheduleId")
    List<OrderVO> listByUserId(@Param("userId") Integer userId, @Param("departureInput") String departureInput,
                               @Param("destinationInput") String destinationInput, @Param("departureTimeInput") Date departureTimeInput);

    @Select("select o.*, r.departure, r.destination, s.departure_time, s.bus_number,u.username,u.phone " +
            "from t_order o left join schedule s on o.schedule_id = s.schedule_id " +
            "left join route r on s.route_id = r.route_id " +
            "LEFT JOIN user u ON o.user_id = u.user_id\n" +
            "where o.is_deleted = 0 " +
            "AND u.username LIKE CONCAT('%', #{usernameInput}, '%') \n" +
            "AND (#{payTimeInput} is null OR DATE(o.pay_time) = DATE(#{payTimeInput})) ORDER BY\n" +
            "o.pay_time DESC"
    )
    @Results({
            @Result(column = "username", property = "user.username"),
            @Result(column = "phone", property = "user.phone"),
            @Result(column = "schedule_id", property = "schedule.scheduleId"),
            @Result(column = "bus_number", property = "schedule.busNumber"),
            @Result(column = "departure_time", property = "schedule.departureTime"),
            // 其他属性的映射
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination")
    })
    List<OrderVO> findAllOrders(@Param("usernameInput") String usernameInput,@Param("payTimeInput") Date payTimeInput);
}




