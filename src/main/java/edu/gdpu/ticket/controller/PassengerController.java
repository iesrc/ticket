package edu.gdpu.ticket.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.config.ResultVo;
import edu.gdpu.ticket.entity.Passenger;
import edu.gdpu.ticket.entity.User;
import edu.gdpu.ticket.service.PassengerService;
import edu.gdpu.ticket.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
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
@RequestMapping("/ticket/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 根据用户id查询所有乘客
     * @param session
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPassenger")
    public ModelAndView findPassenger(HttpSession session,
                                      @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                                      @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                                      @RequestParam(name = "nameInput",required = false,defaultValue = "") String nameInput){


//        int i = 10 / 0;
        System.out.println("passengerName" + nameInput);
        System.out.println("pageNo" + pageNo);
        System.out.println("pageSize" + pageSize);
        ModelAndView mv = new ModelAndView();//视图模型层
        PageHelper.startPage(pageNo, pageSize);//开启分页
        User user = (User) session.getAttribute("user");
        System.out.println("获取的用户" + user);
        List<Passenger> passengerList = passengerService.findByUserIdAndNameInput(user.getUserId(),nameInput);
//        System.out.println("获取的乘客列表" + passengerList);
//        System.out.println("获取的乘客列表个数" + passengerList.size());
        //用来判断是否有乘客以此选择展示不同的表格
        mv.addObject("passengerNum",passengerList.size());
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);

        session.setAttribute("nameInput",nameInput);
        session.setAttribute("pageInfo",pageInfo);
        mv.addObject("passengerList",passengerList);
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        mv.setViewName("passenger");
        return mv;
    }

    /**
     * 跳转到添加乘客页面
     * @return
     */
    @RequestMapping("/addPassenger")
    public ModelAndView addPassenger(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("addPassenger");
        return mv;
    }

    /**
     * 添加乘客
     * @param passenger
     * @param session
     * @return  添加成功返回true，否则返回false
     */
    @ResponseBody
    @RequestMapping("/doAddPassenger")
    public Map<String,Object> doAddPassenger(Passenger passenger, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        System.out.println("添加的乘客信息" + passenger);
        User user = (User) session.getAttribute("user");
        passenger.setUserId(user.getUserId());
        //根据用户id判断乘客表中是否已有该乘客身份证号
        if(passengerService.isIdentityExist(user.getUserId(),passenger.getIdentity())){
            map.put("success",false);
            map.put("msg","该身份证号您的账户下已存在，请勿重复添加");
            return map;
        }
        passengerService.save(passenger);
        map.put("success",true);
        map.put("msg","添加乘客成功");
        return map;
    }

    @ResponseBody
    @RequestMapping("/deletePassengerById")
    public String deletePassengerById(@RequestParam(name = "passengerId",required = false,defaultValue = "0") Integer passengerId){
        System.out.println("获取的passengerId：" + passengerId);

        if(passengerId != 0){
            passengerService.removeById(passengerId);
            return "SUCCESS";
        }
        return "fail";

    }

    /**
     * 根据乘客id和所属用户id修改乘客信息
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public Map<String,Object> update(Passenger passenger,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        User user = (User) session.getAttribute("user");
        System.out.println("修改的乘客信息" + passenger);
        //根据passengerId和userId查询出更新之前数据库的乘客信息
        Passenger oldPassenger = passengerService.findByUserIdAndPassengerId(user.getUserId(),passenger.getPassengerId());
        if(!passenger.getIdentity().equals(oldPassenger.getIdentity())){
            //如果修改的乘客身份证号与原来的不同，则判断该身份证号是否已存在
            if(passengerService.isIdentityExist(user.getUserId(),passenger.getIdentity())){
                map.put("success",false);
                map.put("msg","该身份证号您的账户下已存在，请勿重复添加");
                return map;
            }
        }
            passenger.setUserId(user.getUserId());
            passengerService.updateById(passenger);
            map.put("success",true);
            map.put("msg","修改乘客成功");
            return map;
    }



    @RequestMapping(value = "/getPassengerList", method = RequestMethod.POST)
    @ResponseBody
    public List<Passenger> getPassengerList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return passengerService.findByUserId(user.getUserId());
    }




}