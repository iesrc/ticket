package edu.gdpu.ticket.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.Passenger;
import edu.gdpu.ticket.entity.PassengerVO;
import edu.gdpu.ticket.entity.Route;
import edu.gdpu.ticket.entity.Schedule;
import edu.gdpu.ticket.service.PassengerService;
import edu.gdpu.ticket.service.PassengerVOService;
import edu.gdpu.ticket.service.RouteService;
import edu.gdpu.ticket.service.ScheduleService;
import edu.gdpu.ticket.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/ticket/admin")
public class AdminController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private PassengerVOService passengerVOService;

   /* @GetMapping("/list")
    public String list(Model model) {
        List<Passenger> passengers = passengerService.listWithUsername();
        passengers.forEach(System.out::println);
        model.addAttribute("passengers", passengers);
        return "list";
    }*/

    @RequestMapping("/showPassenger")
    public ModelAndView showPassenger(HttpSession session,
                                      @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                                      @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
                                      @RequestParam(name = "nameInput", required = false, defaultValue = "") String nameInput,
                                      @RequestParam(name = "usernameInput", required = false, defaultValue = "") String usernameInput) {

        System.out.println("passengerName" + nameInput);
        System.out.println("usernameInput" + usernameInput);
        System.out.println("pageNo" + pageNo);
        System.out.println("pageSize" + pageSize);
        ModelAndView mv = new ModelAndView();//视图模型层
        PageHelper.startPage(pageNo, pageSize);//开启分页
        List<PassengerVO> passengerList = passengerVOService.listWithUsername(usernameInput, nameInput);
        System.out.println("获取的乘客列表" + passengerList);
        PageInfo<PassengerVO> pageInfo = new PageInfo<>(passengerList);
        mv.addObject("passengerNum", passengerList.size());
        session.setAttribute("nameInput", nameInput);
        session.setAttribute("usernameInput", usernameInput);
        session.setAttribute("pageInfo", pageInfo);
        mv.addObject("passengerList", passengerList);
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageSize", pageSize);
        mv.setViewName("admin_passenger");
        return mv;
    }

    @RequestMapping("/showSchedule")
    public ModelAndView showSchedule(HttpSession session,
                                     @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                                     @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
                                     @RequestParam(name = "departureInput", required = false, defaultValue = "") String departureInput,
                                     @RequestParam(name = "destinationInput", required = false, defaultValue = "") String destinationInput,
                                     @RequestParam(name = "departureTimeInput", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureTimeInput
    ) {
        System.out.println("departureInput" + departureInput);
        System.out.println("destinationInput" + destinationInput);
        System.out.println("departureTimeInput" + departureTimeInput);
        System.out.println("pageNo" + pageNo);
        System.out.println("pageSize" + pageSize);
        ModelAndView mv = new ModelAndView();//视图模型层
        PageHelper.startPage(pageNo, pageSize);//开启分页
        List<Schedule> scheduleList = scheduleService.selectWithRoute(departureInput, destinationInput, departureTimeInput);
        List<Route> routeList = routeService.list();
        session.setAttribute("routeList", routeList);
        /*System.out.println("routeList" + routeList);
        System.out.println("获取的车次列表" + scheduleList);*/
        PageInfo<Schedule> pageInfo = new PageInfo<>(scheduleList);
        mv.addObject("scheduleNum", scheduleList.size());
        session.setAttribute("departureInput", departureInput);
        session.setAttribute("destinationInput", destinationInput);
        if (departureTimeInput != null) {
            // 假设date是Mar 17 00:00:00 CST 2023
//        Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(departureTimeInput); // formattedDate的值为"2023-03-17"
            mv.addObject("departureTimeInput", formattedDate);
        }
       /* int randomNum = (int) (Math.random() * 100);
        mv.addObject("randomNum", randomNum);*/
        session.setAttribute("pageInfo", pageInfo);
        mv.addObject("scheduleList", scheduleList);
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageSize", pageSize);
        mv.setViewName("admin_schedule");
        return mv;
    }

    @RequestMapping("/addSchedule")
    public String addSchedule(HttpSession session) {
        List<Route> routeList = routeService.list();
//        System.out.println("routeList" + routeList);
        session.setAttribute("routeList", routeList);
        return "admin_addSchedule";
    }

    @ResponseBody
    @RequestMapping("/doAddSchedule")
    public String addSchedule(@RequestParam(name = "routeId", required = false, defaultValue = "") Integer routeId,
                              @RequestParam(name = "duration", required = false, defaultValue = "") String duration,
                              @RequestParam(name = "busNumber", required = false, defaultValue = "") String busNumber,
                              @RequestParam(name = "price", required = false, defaultValue = "") Double price,
                              @RequestParam(name = "seatsLeft", required = false, defaultValue = "") Integer seatsLeft
            , @RequestParam(name = "departureTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date departureTime) {
        // 解析日期字符串为日期类型
        System.out.println("departureTime" + departureTime);

        Schedule schedule = new Schedule();
        schedule.setRouteId(routeId);
        schedule.setDuration(duration);
        schedule.setBusNumber(busNumber);
        schedule.setSeatsLeft(seatsLeft);
        schedule.setDepartureTime(departureTime);
        schedule.setPrice(price);
        System.out.println("schedule" + schedule);
        //添加车次
        boolean result = scheduleService.addSchedule(schedule);
        if (result) {
            return "SUCCESS";
        } else {
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/deleteScheduleById")
    public String deleteScheduleById(@RequestParam(value = "scheduleId", required = false, defaultValue = "0") Integer scheduleId) {
        System.out.println("scheduleId" + scheduleId);
        boolean flag = scheduleService.removeById(scheduleId);
        if (flag) {
            return "SUCCESS";
        } else {
            return "fail";
        }
    }

    @RequestMapping("/updateSchedule")
    public ModelAndView updateSchedule(HttpSession session,
                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
                                       @RequestParam(value = "scheduleId", required = false, defaultValue = "") int scheduleId,
                                       @RequestParam(value = "routeId", required = false, defaultValue = "") int routeId) {
        ModelAndView mv = new ModelAndView();
        Schedule schedule = scheduleService.selectWithScheduleIdAndRouteId(scheduleId, routeId);
        System.out.println("schedule" + schedule);
       /* List<Route> routeList = routeService.list();
        session.setAttribute("routeList",routeList);*/
        session.setAttribute("routeId", routeId);
        System.out.println("routeId" + routeId);
        mv.setViewName("updateSchedule");
        mv.addObject("schedule", schedule);
        session.setAttribute("pageNo", pageNo);
        return mv;

    }

    /**
     * 修改车次
     * @param session
     * @param pageNo
     * @param routeId
     * @param scheduleId
     * @param duration
     * @param busNumber
     * @param seatsLeft
     * @param departureTime
     * @return
     */
    @ResponseBody
    @RequestMapping("/doUpdateSchedule")
    public String doUpdateSchedule(HttpSession session,
                                   @RequestParam(name = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "routeId", required = false, defaultValue = "") Integer routeId,
                                   @RequestParam(name = "scheduleId", required = false, defaultValue = "") Integer scheduleId,
                                   @RequestParam(name = "duration", required = false, defaultValue = "") String duration,
                                   @RequestParam(name = "busNumber", required = false, defaultValue = "") String busNumber,
                                   @RequestParam(name = "price", required = false, defaultValue = "") Double price,
                                   @RequestParam(name = "seatsLeft", required = false, defaultValue = "") Integer seatsLeft
            , @RequestParam(name = "departureTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date departureTime) {
        // 解析日期字符串为日期类型
        System.out.println("departureTime" + departureTime);
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setRouteId(routeId);
        schedule.setDuration(duration);
        schedule.setBusNumber(busNumber);
        schedule.setSeatsLeft(seatsLeft);
        schedule.setPrice(price);
        schedule.setDepartureTime(departureTime);
        System.out.println("更新后的schedule" + schedule);
        System.out.println("更新后的routeId" + routeId);
        System.out.println("更新的scheduleId" + scheduleId);
        session.setAttribute("pageNo", pageNo);
        Schedule oldSchedule = scheduleService.getByScheduleId(scheduleId);
        System.out.println("oldSchedule" + oldSchedule);
        System.out.println("获取的旧RouteId:"+oldSchedule.getRouteId());
        System.out.println("获取的旧RouteId:"+oldSchedule.getRoute().getRouteId());
        //如果传过来的路线、出发时间、车次号与原来的不一样，则判断表中是否已有相同的路线、出发时间、车次号，有的话就不能更新
        //因为oldSchedule.getDepartureTime()获取的时间格式与departureTime不一样，所以这个分支条件一定会执行
        if (oldSchedule.getRoute().getRouteId() != routeId ||!oldSchedule.getDepartureTime().equals(departureTime)
                || !oldSchedule.getBusNumber().equals(busNumber)) {
            boolean result = scheduleService.updateSchedule(schedule);
            if (result) {
                return "SUCCESS";
            } else {
                return "fail";
            }
        }
        //没有做关键的修改，直接更新
        scheduleService.updateById(schedule);
        return "SUCCESS";
    }

}
