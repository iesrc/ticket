package edu.gdpu.ticket.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.*;
import edu.gdpu.ticket.service.OrderService;
import edu.gdpu.ticket.service.RouteService;
import edu.gdpu.ticket.service.ScheduleService;
import edu.gdpu.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author iesrc
 * @since 2023-02-27
 */
@Controller
@RequestMapping("/ticket/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RouteService routeService;

    @RequestMapping("/list")
    public ModelAndView list(HttpSession session,
                             @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                             @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                             @RequestParam(name = "departureInput",required = false,defaultValue = "") String departureInput,
                             @RequestParam(name = "destinationInput",required = false,defaultValue = "") String destinationInput
            , @RequestParam(name = "departureTimeInput", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureTimeInput
                             ){
        ModelAndView mv = new ModelAndView();
        PageHelper.startPage(pageNo, pageSize);//开启分页
        User user = (User) session.getAttribute("user");
        List<TicketVO> ticketList = ticketService.listByUserId(user.getUserId(),departureInput,destinationInput,departureTimeInput);
        System.out.println("获取的ticketList = " + ticketList);
        // 判断每个车票是否过期
        for (TicketVO ticket : ticketList) {
            // 如果当前时间大于车票的出发时间，则车票过期
            if (ticket.getSchedule().getDepartureTime().before(new Date())) {
                ticket.setIsExpired(true);
            }else {
                ticket.setIsExpired(false);
            }
        }
        mv.addObject("ticketList",ticketList);
        mv.addObject("ticketNum",ticketList.size());
        PageInfo<TicketVO> pageInfo = new PageInfo<>(ticketList);
        session.setAttribute("pageInfo",pageInfo);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        session.setAttribute("departureInput",departureInput);
        session.setAttribute("destinationInput",destinationInput);
        if (departureTimeInput != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(departureTimeInput); // formattedDate的值为"2023-03-17"
            mv.addObject("departureTimeInput", formattedDate);
        }
        mv.setViewName("ticket");
        return mv;
    }

    @RequestMapping("/findAllTickets")
    public ModelAndView findAllTickets(HttpSession session,
                             @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                             @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                             @RequestParam(name = "passengerInput",required = false,defaultValue = "") String passengerInput,
                             @RequestParam(name = "destinationInput",required = false,defaultValue = "") String destinationInput
            , @RequestParam(name = "departureTimeInput", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureTimeInput
    ){
        ModelAndView mv = new ModelAndView();
        PageHelper.startPage(pageNo, pageSize);//开启分页
        List<TicketVO> ticketList = ticketService.findAllTickets(passengerInput,destinationInput,departureTimeInput);
        mv.addObject("ticketList",ticketList);
        session.setAttribute("ticketNum",ticketList.size());
        PageInfo<TicketVO> pageInfo = new PageInfo<>(ticketList);
        session.setAttribute("pageInfo",pageInfo);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        session.setAttribute("passengerInput",passengerInput);
        session.setAttribute("destinationInput",destinationInput);
        if (departureTimeInput != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(departureTimeInput); // formattedDate的值为"2023-03-17"
            mv.addObject("departureTimeInput", formattedDate);
        }
        mv.setViewName("admin_ticket");
        return mv;
    }

    @PostMapping("/refundTicket")
    @ResponseBody
    public Map<String, Object> refundTicket(@RequestParam("ticketId") Integer ticketId) {
        Map<String, Object> resultMap = new HashMap<>();

        // Get ticket information from database
        Ticket ticket = ticketService.getById(ticketId);
        System.out.println("ticket" + ticket);
        if (ticket == null || ticket.getIsDeleted() == 1) {
            resultMap.put("success", false);
            resultMap.put("msg", "退票失败：该票不存在或已被删除");
            return resultMap;
        }

        // Get order information from database
        Order order = orderService.getById(ticket.getOrderId());
        if (order == null || order.getIsDeleted() == 1) {
            resultMap.put("success", false);
            resultMap.put("msg", "退票失败：该订单不存在或已被删除");
            return resultMap;
        }

        // Update database

        boolean update = ticketService.removeById(ticketId);
        Order OrderById = orderService.getById(ticket.getOrderId());
        System.out.println("OrderById = " + OrderById);
        Schedule schedule = scheduleService.getByScheduleId(OrderById.getScheduleId());
        System.out.println("schedule = " + schedule);
        System.out.println("schedule.getSeatLeaf = " + schedule.getSeatsLeft());
        int seats = scheduleService.updateSeatsById(schedule.getScheduleId(), schedule.getSeatsLeft() + 1);
        System.out.println("schedule.getSeatsLeft() + 1 " + schedule.getSeatsLeft() );
        if (update && seats > 0) {
            resultMap.put("success", true);
            resultMap.put("msg", "退票成功");
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "退票失败：数据库更新失败");
        }

        return resultMap;
    }

    @RequestMapping("/showChangeSchedule")
    public ModelAndView showChangeSchedule(HttpSession session,
                                     @RequestParam(name = "ticketId",required = false,defaultValue = " ") int ticketId,
                                           @RequestParam(name = "price",required = false,defaultValue = " ") Double price,
                                           @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
                                     @RequestParam(name = "departure", required = false, defaultValue = "") String departureInput,
                                     @RequestParam(name = "destination", required = false, defaultValue = "") String destinationInput){
        ModelAndView mv = new ModelAndView();
        session.setAttribute("departureInput",departureInput);
        session.setAttribute("destinationInput",destinationInput);
        session.setAttribute("ticketId",ticketId);
        session.setAttribute("price",price);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        PageHelper.startPage(pageNo, pageSize);//开启分页
        PageInfo<Object> pageInfo = new PageInfo<>();
        session.setAttribute("pageInfo",pageInfo);
        mv.addObject("scheduleNum",0);
        mv.setViewName("searchResult1");
        return mv;
    }

    @RequestMapping("/showSchedule1")
    public ModelAndView showSchedule1(HttpSession session,
                                     @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                                     @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
                                     @RequestParam(name = "departureInput", required = false, defaultValue = "") String departureInput,
                                     @RequestParam(name = "destinationInput", required = false, defaultValue = "") String destinationInput,
                                     @RequestParam(name = "departureTimeInput", required = false, defaultValue = "") String departureTimeInput
    ) throws ParseException {

        ModelAndView mv = new ModelAndView();//视图模型层
        PageHelper.startPage(pageNo, pageSize);//开启分页
        System.out.println("保存在session中的ticketId: " + session.getAttribute("ticketId"));
        // 获取当前日期时间
        Date currentDateTime = new Date();
        System.out.println("当前日期" + currentDateTime);

        // 将用户输入的日期字符串解析为Date对象
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date searchDate = dateFormat.parse(departureTimeInput);
        System.out.println("用户输入的日期" + searchDate);

        // 比较日期是否相同，忽略时间部分
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(searchDate);
        cal2.setTime(currentDateTime);
        boolean isSameDate = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE);
        // 判断用户输入的日期是否为当天日期
        if (isSameDate) {
            // 如果是当天日期，则获取当前时间
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Date currentTime = timeFormat.parse(timeFormat.format(new Date()));
            System.out.println("当前时间" + currentTime);
            // 使用当前日期和时间进行查询
            List<Schedule> scheduleList = scheduleService.getScheduleListByDateAndTime(departureInput, destinationInput, searchDate, currentTime);
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
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat1.format(searchDate); // formattedDate的值为"2023-03-17"
                mv.addObject("departureTimeInput", formattedDate);
            }
           /* int randomNum = (int) (Math.random() * 100);
            mv.addObject("randomNum", randomNum);*/
            session.setAttribute("pageInfo", pageInfo);
            mv.addObject("scheduleList", scheduleList);
            session.setAttribute("pageNo", pageNo);
            session.setAttribute("pageSize", pageSize);
            mv.setViewName("searchResult");
            return mv;
        } else {
            // 如果不是当天日期，则查询该日期当天的所有车次
            List<Schedule> scheduleList = scheduleService.selectWithRoute(departureInput, destinationInput, searchDate);
            List<Route> routeList = routeService.list();
            session.setAttribute("routeList", routeList);
            PageInfo<Schedule> pageInfo = new PageInfo<>(scheduleList);
            mv.addObject("scheduleNum", scheduleList.size());
            session.setAttribute("departureInput", departureInput);
            session.setAttribute("destinationInput", destinationInput);
            if (departureTimeInput != null) {
                // 假设date是Mar 17 00:00:00 CST 2023
                //Date date = new Date();
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat1.format(searchDate); // formattedDate的值为"2023-03-17"
                mv.addObject("departureTimeInput", formattedDate);
            }
            session.setAttribute("pageInfo", pageInfo);
            mv.addObject("scheduleList", scheduleList);
            session.setAttribute("pageNo", pageNo);
            session.setAttribute("pageSize", pageSize);
            mv.setViewName("searchResult1");
            return mv;
        }
    }

    @ResponseBody
    @RequestMapping("/deleteTicketById")
    public String deleteTicketById(@RequestParam(value = "ticketId",required = false,defaultValue = "0") Integer ticketId) {
        System.out.println("ticketId" + ticketId);
        boolean flag = ticketService.removeById(ticketId);
        if (flag) {
            return "SUCCESS";
        } else {
            return "fail";
        }
    }

}
