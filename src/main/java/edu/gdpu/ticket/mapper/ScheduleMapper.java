package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【schedule】的数据库操作Mapper
* @createDate 2023-03-17 19:34:11
* @Entity edu.gdpu.ticket.entity.Schedule
*/
@Component
public interface ScheduleMapper extends BaseMapper<Schedule> {
    //将departureTime改为可选参数，并在查询条件中增加对是否传入departureTime的判断，
    // 如果没传就不加入departureTime的查询条件。
    //DATE(s.departure_time) 表示只取 s.departure_time 的日期部分。通过 DATE(#{departureTime})
    // 可以将 departureTime 的日期部分与 s.departure_time 的日期部分比较。
    @Select("SELECT s.*, r.departure, r.destination \n" +
            "FROM schedule s \n" +
            "LEFT JOIN route r ON s.route_id = r.route_id \n" +
            "WHERE s.is_deleted = 0 AND r.is_deleted = 0 \n" +
            "AND r.departure LIKE CONCAT('%', #{departure}, '%') \n" +
            "AND r.destination LIKE CONCAT('%', #{destination}, '%')\n" +
            "AND (#{departureTime} is null OR DATE(s.departure_time) = DATE(#{departureTime}))")
    @Results({
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination")
    })
    List<Schedule> selectWithRoute(@Param("departure") String departure,
                                   @Param("destination") String destination,
                                   @Param("departureTime") Date departureTime);

    @Select("SELECT s.*, r.departure, r.destination \n" +
            "FROM schedule s \n" +
            "LEFT JOIN route r ON s.route_id = r.route_id \n" +
            "WHERE s.is_deleted = 0 AND r.is_deleted = 0 \n"+
            "and s.schedule_id=#{scheduleId}")
    @Results({
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination")
    })
    Schedule selectWithScheduleIdAndRouteId(@Param("scheduleId") int scheduleId,
                                            @Param("routeId") int routeId);

    @Select("SELECT s.*, r.departure, r.destination \n" +
            "FROM schedule s \n" +
            "LEFT JOIN route r ON s.route_id = r.route_id \n" +
            "WHERE s.is_deleted = 0 AND r.is_deleted = 0 \n"+
            "and s.schedule_id=#{scheduleId}")
    @Results({
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination")
    })
    Schedule getByScheduleId(@Param("scheduleId") Integer scheduleId);

    @Select("SELECT s.seats_left\n" +
            "FROM schedule s \n" +
            "WHERE s.is_deleted = 0 \n"+
            "and s.schedule_id=#{scheduleId}")
    Integer getRemainingSeats(@Param("scheduleId")Integer scheduleId);

    @Select("SELECT s.*, r.departure, r.destination \n" +
            "FROM schedule s \n" +
            "LEFT JOIN route r ON s.route_id = r.route_id \n" +
            "WHERE s.is_deleted = 0 AND r.is_deleted = 0 \n" +
            "AND r.departure LIKE CONCAT('%', #{departureInput}, '%') \n" +
            "AND r.destination LIKE CONCAT('%', #{destinationInput}, '%')\n" +
            "AND DATE(s.departure_time) = DATE(#{date}) \n" +
            "AND TIME(s.departure_time) >= TIME(#{time})")
    @Results({
            @Result(column = "route_id", property = "route.routeId"),
            @Result(column = "departure", property = "route.departure"),
            @Result(column = "destination", property = "route.destination")
    })
    List<Schedule> getScheduleListByDateAndTime(@Param("departureInput") String departureInput,
                                                @Param("destinationInput") String destinationInput,
                                                @Param("date") Date date,
                                                @Param("time") Date time);



    @Update("UPDATE schedule SET seats_left = #{newSeatNum} WHERE " +
            "schedule_id = #{scheduleId}")
    int updateSeatsById(Integer scheduleId, Integer newSeatNum);
}




