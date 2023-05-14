package edu.gdpu.ticket.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.Route;
import edu.gdpu.ticket.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author iesrc
 * @since 2023-02-27
 */
@Controller
@RequestMapping("/ticket/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @RequestMapping("/findAllRoutes")
    public ModelAndView findAllRoutes(HttpSession session, @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                                      @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                                      @RequestParam(name = "departureInput",required = false,defaultValue = "") String departureInput,
                                        @RequestParam(name = "destinationInput",required = false,defaultValue = "") String destinationInput,
                                      @RequestParam(name = "pageTotal",required = false,defaultValue = "0") int pageTotal){
        System.out.println("pageNo" + pageNo);
        System.out.println("pageSize" + pageSize);
        System.out.println("departureInput" + departureInput);
        System.out.println("destinationInput" + destinationInput);
        System.out.println("pageTotal" + pageTotal);
        ModelAndView mv = new ModelAndView();
        PageHelper.startPage(pageNo, pageSize);//开启分页
        List<Route> routeList = routeService.findAllRoutes(departureInput,destinationInput);
        PageInfo<Route> pageInfo = new PageInfo<>(routeList);
        session.setAttribute("pageInfo",pageInfo);
        session.setAttribute("departureInput",departureInput);
        session.setAttribute("destinationInput",destinationInput);
        session.setAttribute("pageTotal",pageTotal);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        mv.addObject("routeList",routeList);
        mv.addObject("routeNum",routeList.size());
        mv.setViewName("route");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/add")
    public Map<String,Object> addRoute(Route route){
        Map<String, Object> map = new HashMap<>();
        System.out.println("route" + route);
        // 先从数据库中查询是否已经存在该路线记录
        Route oldRoute = routeService.findRouteByDepartureAndDestination(route.getDeparture(),route.getDestination());
        if(oldRoute != null){
            map.put("success",false);
            map.put("msg","该路线已存在,请勿重复添加");
            return map;
        }
        // 不存在则添加
        boolean flag = routeService.save(route);
        if(flag){
            map.put("success",true);
            map.put("msg","添加成功");
        }else{
            map.put("success",false);
            map.put("msg","添加失败");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/deleteRouteById")
    public String deleteRouteById(@RequestParam(value = "routeId",required = false,defaultValue = "0") Integer routeId) {
        System.out.println("routeId" + routeId);
        boolean flag = routeService.removeById(routeId);
        if (flag) {
            return "SUCCESS";
        } else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/update")
    public Map<String,Object> updateRoute(Route route){
        Map<String, Object> map = new HashMap<>();
        System.out.println("route" + route);
        Route oldRoute = routeService.getById(route.getRouteId());
        if(!route.getDeparture().equals(oldRoute.getDeparture()) && !route.getDestination().equals(oldRoute.getDestination())){
            // 修改了出发地和目的地
            Route newRoute = routeService.findRouteByDepartureAndDestination(route.getDeparture(),route.getDestination());
            if(newRoute != null){
                map.put("success",false);
                map.put("msg","该路线已存在,请勿重复添加");
                return map;
            }
        }
        boolean flag = routeService.updateById(route);
        if(flag){
            map.put("success",true);
            map.put("msg","修改成功");
        }else{
            map.put("success",false);
            map.put("msg","修改失败");
        }
        return map;
    }
}
