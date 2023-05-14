package edu.gdpu.ticket.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.*;
import edu.gdpu.ticket.mapper.ScheduleMapper;
import edu.gdpu.ticket.service.OrderService;
import edu.gdpu.ticket.service.PassengerService;
import edu.gdpu.ticket.service.ScheduleService;
import edu.gdpu.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author iesrc
 * @since 2023-02-27
 */
@Controller
@RequestMapping("/ticket/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @RequestMapping("/confirmOrder1")
    public ModelAndView createOrder1(HttpSession session,
                                    @RequestParam("passengerIds") List<Integer> passengerIds,
                                    @RequestParam("scheduleId") Integer scheduleId) {
        ModelAndView mv = new ModelAndView();
        System.out.println("passengerIds = " + passengerIds);
        System.out.println("scheduleId = " + scheduleId);
        System.out.println(passengerIds.size());
        mv.addObject("passengerTotal",passengerIds.size());
        Schedule schedule = scheduleService.getByScheduleId(scheduleId);
        System.out.println("schedule = " + schedule);
        mv.addObject("schedule",schedule);
        Double price = (Double) session.getAttribute("price");
        Double totalPrice = price - schedule.getPrice();
        mv.addObject("totalPrice",totalPrice);
        List<Passenger> passengerList = passengerService.listByIds(passengerIds);
        mv.addObject("passengerIds",passengerIds);
        mv.addObject("passengerList",passengerList);
        mv.setViewName("confirmOrder1");
        return mv;
    }

    @RequestMapping("/confirmOrder")
        public ModelAndView createOrder(@RequestParam("passengerIds") List<Integer> passengerIds,
                                        @RequestParam("scheduleId") Integer scheduleId) {
        ModelAndView mv = new ModelAndView();
        System.out.println("passengerIds = " + passengerIds);
        System.out.println("scheduleId = " + scheduleId);
        System.out.println(passengerIds.size());
        mv.addObject("passengerTotal",passengerIds.size());
        Schedule schedule = scheduleService.getByScheduleId(scheduleId);
        System.out.println("schedule = " + schedule);
        mv.addObject("schedule",schedule);
        Double totalPrice = schedule.getPrice() * passengerIds.size();
        mv.addObject("totalPrice",totalPrice);
        List<Passenger> passengerList = passengerService.listByIds(passengerIds);
        mv.addObject("passengerIds",passengerIds);
        mv.addObject("passengerList",passengerList);
        mv.setViewName("confirmOrder");
        return mv;
    }

    @RequestMapping("/pay")
    @ResponseBody
    public String pay(HttpSession session,
                      @RequestParam(value = "scheduleId",defaultValue = "",required = false) Integer scheduleId,
                      @RequestParam(value = "passengerIds",defaultValue = "",required = false) String passengerIdsJson,
                      @RequestParam(value = "totalPrice",defaultValue = "",required = false) String totalPrice) throws JsonProcessingException {
        Double totalPriceD = Double.parseDouble(totalPrice);
        // 将 passengerIds 的 JSON 字符串转换成 List<Integer>
        List<Integer> passengerIds = new ArrayList<>();
        passengerIds = new ObjectMapper().readValue(passengerIdsJson, new TypeReference<List<Integer>>() {});
        System.out.println("passengerIds的长度 = " + passengerIds.size());

        //创建订单
        User user = (User) session.getAttribute("user");
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setScheduleId(scheduleId);
        order.setStatus("已支付");
        order.setPayTime(new Date());
        order.setCount(passengerIds.size());
        order.setMoney(totalPriceD);
        order.setIsDeleted(0);
        boolean save = orderService.insertOrder(order);
        if (save){
            ArrayList<Ticket> ticketList = new ArrayList<>();
            //为每一个乘客创建车票
            for (Integer passengerId : passengerIds) {
                Ticket ticket = new Ticket();
                ticket.setOrderId(order.getOrderId());
                ticket.setPassengerId(passengerId);
                ticket.setCanModify(0);
                ticketList.add(ticket);
            }
            ticketService.saveBatch(ticketList);
            //获取相应车次，余票减去乘客数量
            Schedule schedule = scheduleService.getByScheduleId(scheduleId);
            Integer newSeatNum = schedule.getSeatsLeft() - passengerIds.size();
            //更新车次余票
            int i = scheduleService.updateSeatsById(scheduleId,newSeatNum);

            return "success";
        }else {
            return "fail";
        }
    }


    @RequestMapping("/pay1")
    @ResponseBody
    public String pay1(HttpSession session,
                      @RequestParam(value = "scheduleId",defaultValue = "",required = false) Integer scheduleId,
                      @RequestParam(value = "passengerIds",defaultValue = "",required = false) String passengerIdsJson,
                      @RequestParam(value = "totalPrice",defaultValue = "",required = false) String totalPrice) throws JsonProcessingException {
        Double totalPriceD = Double.parseDouble(totalPrice);
        // 将 passengerIds 的 JSON 字符串转换成 List<Integer>
        List<Integer> passengerIds = new ArrayList<>();
        passengerIds = new ObjectMapper().readValue(passengerIdsJson, new TypeReference<List<Integer>>() {});
        System.out.println("passengerIds的长度 = " + passengerIds.size());

        Integer ticketId = (Integer) session.getAttribute("ticketId");
        if (ticketId == null){
            return "fail";
        }



        //创建订单
        User user = (User) session.getAttribute("user");
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setScheduleId(scheduleId);
        order.setStatus("已支付");
        order.setPayTime(new Date());
        order.setCount(passengerIds.size());
        Schedule newSchedule = scheduleService.getByScheduleId(scheduleId);
        order.setMoney(newSchedule.getPrice());
        order.setIsDeleted(0);
        boolean save = orderService.insertOrder(order);
        if (save){
            ArrayList<Ticket> ticketList = new ArrayList<>();
            //为每一个乘客创建车票
            for (Integer passengerId : passengerIds) {
                Ticket ticket = new Ticket();
                ticket.setOrderId(order.getOrderId());
                ticket.setPassengerId(passengerId);
                ticket.setCanModify(ticketId);
                ticketList.add(ticket);
            }
            ticketService.saveBatch(ticketList);
            //获取相应车次，余票减去乘客数量
            Schedule schedule = scheduleService.getByScheduleId(scheduleId);
            Integer newSeatNum = schedule.getSeatsLeft() - passengerIds.size();
            //更新车次余票
            int i = scheduleService.updateSeatsById(scheduleId,newSeatNum);
            //将改签之前的车次余票加回去
            Ticket oldTicket = ticketService.getById(ticketId);
            Order oldOrder = orderService.getById(oldTicket.getOrderId());
            Schedule oldSchedule = scheduleService.getByScheduleId(oldOrder.getScheduleId());
            //更新改签之前的车次余票
//            Integer oldSeatNum = oldSchedule.getSeatsLeft() + oldOrder.getCount();
            Integer oldSeatNum = oldSchedule.getSeatsLeft() + 1;
            scheduleService.updateSeatsById(oldOrder.getScheduleId(),oldSeatNum);
            //删除改签之前的车票
            ticketService.removeById(ticketId);
            return "success";
        }else {
            return "fail";
        }
    }


    @RequestMapping("/list")
    public ModelAndView list(HttpSession session,
                             @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                             @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                             @RequestParam(name = "departureInput",required = false,defaultValue = "") String departureInput,
                             @RequestParam(name = "destinationInput",required = false,defaultValue = "") String destinationInput
            , @RequestParam(name = "departureTimeInput", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureTimeInput){
        ModelAndView mv = new ModelAndView();
        //演示500错误
//        int i = 10 / 0;
        PageHelper.startPage(pageNo, pageSize);//开启分页
        User user = (User) session.getAttribute("user");
        List<OrderVO> orderList = orderService.listByUserId(user.getUserId(),departureInput,destinationInput,departureTimeInput);
        mv.addObject("orderList",orderList);
        mv.addObject("orderListNum",orderList.size());
        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderList);
        System.out.println("pageInfo = " + pageInfo);
        System.out.println("pageNo = "+pageNo);
        System.out.println("pageSize = "+pageSize);
        session.setAttribute("pageInfo",pageInfo);
        session.setAttribute("departureInput",departureInput);
        session.setAttribute("destinationInput",destinationInput);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        if (departureTimeInput != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(departureTimeInput); // formattedDate的值为"2023-03-17"
            mv.addObject("departureTimeInput", formattedDate);
        }
        mv.setViewName("orderList");
        return mv;
    }

    @RequestMapping("/findAllOrders")
    public ModelAndView findAllOrders(HttpSession session,
                             @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                             @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                             @RequestParam(name = "usernameInput",required = false,defaultValue = "") String usernameInput
            , @RequestParam(name = "payTimeInput", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date payTimeInput){
        ModelAndView mv = new ModelAndView();
        PageHelper.startPage(pageNo, pageSize);//开启分页
        List<OrderVO> orderList = orderService.findAllOrders(usernameInput,payTimeInput);
        mv.addObject("orderList",orderList);
        mv.addObject("orderListNum",orderList.size());
        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderList);
        System.out.println("pageInfo = " + pageInfo);
        System.out.println("pageNo = "+pageNo);
        System.out.println("pageSize = "+pageSize);
        session.setAttribute("pageInfo",pageInfo);
        session.setAttribute("payTimeInput",payTimeInput);
        session.setAttribute("usernameInput",usernameInput);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        if (payTimeInput != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(payTimeInput); // formattedDate的值为"2023-03-17"
            mv.addObject("departureTimeInput", formattedDate);
        }
        mv.setViewName("admin_order");
        return mv;
    }

}
