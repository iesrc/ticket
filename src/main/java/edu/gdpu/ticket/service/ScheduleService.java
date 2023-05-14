package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【schedule】的数据库操作Service
* @createDate 2023-03-17 19:34:11
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spri
public interface ScheduleService extends IService<Schedule> {


     int updateSeatsById(Integer scheduleId, Integer newSeatNum);

    //查询车次
    List<Schedule> selectWithRoute(String departure, String destination, Date departureTime);

    //添加车次
    boolean addSchedule(Schedule schedule);

    //
    Schedule selectWithScheduleIdAndRouteId(int scheduleId, int routeId);

    //
    boolean updateSchedule(Schedule schedule);

    Schedule getByScheduleId(Integer scheduleId);

    Integer getRemainingSeats(Integer scheduleId);

    List<Schedule> getScheduleListByDateAndTime(String departureInput,String destinationInput, Date searchDate, Date currentTime);


}
