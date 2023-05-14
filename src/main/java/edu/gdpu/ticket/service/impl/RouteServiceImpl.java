package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.Route;
import edu.gdpu.ticket.service.RouteService;
import edu.gdpu.ticket.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 24100
* @description 针对表【route】的数据库操作Service实现
* @createDate 2023-03-11 15:33:23
*/
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route>
    implements RouteService{

    @Autowired
    private RouteMapper routeMapper;

    @Override
    public List<Route> findAllRoutes(String departureInput, String destinationInput) {
        QueryWrapper<Route> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("departure",departureInput)
                .like("destination",destinationInput);
        List<Route> routeList = routeMapper.selectList(queryWrapper);
        return routeList;
    }

    @Override
    public Route findRouteByDepartureAndDestination(String departure, String destination) {
        QueryWrapper<Route> queryWrapper = new QueryWrapper<Route>();
        queryWrapper.eq("departure", departure)
                .eq("destination", destination);
        Route route = routeMapper.selectOne(queryWrapper);
        return route;
    }
}




