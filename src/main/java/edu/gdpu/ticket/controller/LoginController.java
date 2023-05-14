package edu.gdpu.ticket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.*;
import edu.gdpu.ticket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/2/28 17:09
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RouteService routeService;

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }


    /**
     * 校验登录用户是否合法
     *
     * @param identity
     * @param username
     * @param password
     * @param keyCord
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doLogin")
    public String doLogin(@RequestParam("identity") int identity, @RequestParam("username") String username,
                          @RequestParam("password") String password, @RequestParam("keyCord") String keyCord,
                          HttpSession session, Model model) {

        String lowerCase = keyCord.toLowerCase();
        String verifyCode = (String) session.getAttribute("verifyCode");
//        System.out.println(lowerCase);
//        System.out.println(((String) session.getAttribute("verifyCode")).toLowerCase());
//        System.out.println( "username" +username);
//        System.out.println("password" + password);
        if ("".equals(lowerCase) || !lowerCase.equals(verifyCode.toLowerCase())) {
            model.addAttribute("errorMsg", "验证码输入错误");
            return "login";
        }
        if (identity == 1) {//判断管理员是否合法
            Admin admin = adminService.getAdminByUsernameAndPassword(username, password);
//            System.out.println(admin);
            if (admin == null) {
                model.addAttribute("errorMsg", "用户名或密码错误！");
            } else {
                session.setAttribute("admin", admin);
                return "admin_index";
            }
        } else if (identity == 0) {//判断用户是否合法
            User user = userService.getUserByUsernameAndPassword(username, password);
//            System.out.println("查询的用户：" + user);
            if (user == null) {
                model.addAttribute("errorMsg", "用户名或密码错误！");
            } else {
                session.setAttribute("user", user);
                return "user_index";
            }
        } else {
            model.addAttribute("errorMsg", "用户类型错误！");
        }
        return "login";//重定向，防止表单重复提交
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * 进入用户主页
     * @return
     */
    @RequestMapping("/index")
    public String userIndex(){
        return "user_index";
    }

    /**
     * 进入管理员主页
     * @return
     */
    @RequestMapping("/adminIndex")
    public ModelAndView adminIndex(HttpSession session){
        ModelAndView mv = new ModelAndView();
        session.setAttribute("notices",newsService.list(null));
        mv.setViewName("admin_index");
        return mv;
    }

    @RequestMapping("/adminMain")
    public String adminMain(HttpSession session){
        List<News> newsList = newsService.list(null);
        session.setAttribute("notices",newsList);
        return "adminMain";
    }

    @RequestMapping("/userMain")
    public String userMain(HttpSession session,
                           @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
                           @RequestParam(name = "departureInput", required = false, defaultValue = "") String departureInput,
                           @RequestParam(name = "destinationInput", required = false, defaultValue = "") String destinationInput,
                           @RequestParam(name = "departureTimeInput", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureTimeInput){
        session.setAttribute("departureInput", departureInput);
        session.setAttribute("destinationInput", destinationInput);
        if (departureTimeInput != null) {
            // 假设date是Mar 17 00:00:00 CST 2023
//        Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(departureTimeInput); // formattedDate的值为"2023-03-17"
            session.setAttribute("departureTimeInput", formattedDate);
        }
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageSize", pageSize);
        return "userMain";
    }

    /*@RequestMapping("/showSchedule")
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
        *//*System.out.println("routeList" + routeList);
        System.out.println("获取的车次列表" + scheduleList);*//*
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
       *//* int randomNum = (int) (Math.random() * 100);
        mv.addObject("randomNum", randomNum);*//*
        session.setAttribute("pageInfo", pageInfo);
        mv.addObject("scheduleList", scheduleList);
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageSize", pageSize);
        mv.setViewName("searchResult");
        return mv;
    }*/

    @RequestMapping("/showSchedule")
    public ModelAndView showSchedule(HttpSession session,
                                     @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                                     @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
                                     @RequestParam(name = "departureInput", required = false, defaultValue = "") String departureInput,
                                     @RequestParam(name = "destinationInput", required = false, defaultValue = "") String destinationInput,
                                     @RequestParam(name = "departureTimeInput", required = false, defaultValue = "") String departureTimeInput
    ) throws ParseException {

        ModelAndView mv = new ModelAndView();//视图模型层
        PageHelper.startPage(pageNo, pageSize);//开启分页

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
            mv.setViewName("searchResult");
            return mv;
        }
    }

}

