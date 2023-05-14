package edu.gdpu.ticket;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.gdpu.ticket.entity.Admin;
import edu.gdpu.ticket.entity.Passenger;
import edu.gdpu.ticket.entity.Schedule;
import edu.gdpu.ticket.entity.User;
import edu.gdpu.ticket.mapper.PassengerMapper;
import edu.gdpu.ticket.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/2/27 22:20
 */
@SpringBootTest
public class Test01 {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TicketService ticketService;

    @Test
    public void test01(){
        long count = userService.count(null);
        System.out.println(count);
    }

    @Test
    public void test02(){
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username","admin");
        adminQueryWrapper.eq("password","12345");
        Admin admin = adminService.getOne(adminQueryWrapper);
        System.out.println(admin);
    }

    @Test
    public void test03(){
        Passenger passenger = passengerMapper.getByUserIdAndIdentityPassenger(2, "123456789012345678");
        System.out.println(passenger);
    }

    /**
     * 测试查询所有用户
     */
    @Test
    public void test04(){
        List<User> allUsers = userService.findAllUsers("");
        allUsers.forEach(System.out::println);
    }

   /* @Test
    public void test05(){
        List<Schedule> schedules = scheduleService.selectWithRoute("", "",new Date(2023, 3, 15));
        schedules.forEach(System.out::println);
    }*/

    /**
     * 测试根据用户id查询所有乘客
     */
    @Test
    public void test06(){
        passengerMapper.selectByUserId(2)
                .forEach(System.out::println);
    }

    @Test
    public void test07(){
        Integer remainingSeats = scheduleService.getRemainingSeats(1);
        System.out.println("remainingSeats = " + remainingSeats);
    }

    /*@Test
    public void test08(){
        Schedule byId = scheduleService.getById(1);
        System.out.println(byId);
    }*/

    @Test
    public void test09(){
       passengerMapper.selectPassengersByIds(new ArrayList<Integer>(){{
           add(1);
           add(2);
       }}).forEach(System.out::println);
    }

    @Test
    public void test10(){
        Schedule schedule = new Schedule();
        schedule.setScheduleId(1);
        schedule.setSeatsLeft(10);
        scheduleService.updateById(schedule);
    }

    @Test
    public void test11(){
        boolean b = ticketService.removeById(20);
        System.out.println(b);
    }

}
