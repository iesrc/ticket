package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Schedule;
import edu.gdpu.ticket.service.ScheduleService;
import edu.gdpu.ticket.mapper.ScheduleMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
* @author 24100
* @description 针对表【schedule】的数据库操作Service实现
* @createDate 2023-03-17 19:34:11
*/
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
    implements ScheduleService{

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public int updateSeatsById(Integer scheduleId, Integer newSeatNum) {
        return scheduleMapper.updateSeatsById(scheduleId, newSeatNum);
    }

    @Override
    public List<Schedule> selectWithRoute(String departure, String destination, Date departureTime) {
        return getBaseMapper().selectWithRoute(departure, destination, departureTime);
    }


    /**
     * 添加车次
     * @param schedule
     * @return true：添加成功，false：添加失败
     */
    public boolean addSchedule(Schedule schedule) {
        // 查询该路线该发车时间是否已经有相同车牌号码的车次存在
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("route_id", schedule.getRouteId())
                .eq("departure_time", schedule.getDepartureTime())
                .eq("bus_number", schedule.getBusNumber());
        Long count = scheduleMapper.selectCount(queryWrapper);
        if (count > 0) {
            // 存在相同车牌号码的车次，不允许添加
            return false;
        } else {
            // 不存在相同车牌号码的车次，可以添加
            scheduleMapper.insert(schedule);
            return true;
        }
    }

    @Override
    public Schedule selectWithScheduleIdAndRouteId(int scheduleId, int routeId) {
        return scheduleMapper.selectWithScheduleIdAndRouteId(scheduleId,routeId);
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {
        // 查询该路线该发车时间是否已经有相同车牌号码的车次存在
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("route_id", schedule.getRouteId())
                .eq("departure_time", schedule.getDepartureTime())
                .eq("bus_number", schedule.getBusNumber());
        Long count = scheduleMapper.selectCount(queryWrapper);
        if (count > 0) {
            // 存在相同车牌号码的车次，不允许添加
            return false;
        } else {
            // 不存在相同车牌号码的车次，可以添加
            scheduleMapper.updateById(schedule);
            return true;
        }
    }

    @Override
    public Schedule getByScheduleId(Integer scheduleId) {
        return scheduleMapper.getByScheduleId(scheduleId);
    }

    @Override
    public Integer getRemainingSeats(Integer scheduleId) {
        return scheduleMapper.getRemainingSeats(scheduleId);

    }

    @Override
    public List<Schedule> getScheduleListByDateAndTime(String departureInput,String destinationInput,Date date, Date time) {
      return scheduleMapper.getScheduleListByDateAndTime(departureInput,destinationInput,date,time);
    }


}




