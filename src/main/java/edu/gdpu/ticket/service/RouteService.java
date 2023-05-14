package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.Route;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 24100
* @description 针对表【route】的数据库操作Service
* @createDate 2023-03-11 15:33:23
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spring的事务
public interface RouteService extends IService<Route> {

    //  根据出发地和目的地查询路线
    List<Route> findAllRoutes(String departureInput, String destinationInput);

    //  根据出发地和目的地查询路线
    Route findRouteByDepartureAndDestination(String departure, String destination);
}
