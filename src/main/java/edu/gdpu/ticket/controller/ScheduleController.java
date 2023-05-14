package edu.gdpu.ticket.controller;


import edu.gdpu.ticket.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author iesrc
 * @since 2023-02-27
 */
@Controller
@RequestMapping("/ticket/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;


    @ResponseBody
    @RequestMapping("/getRemainingSeats")
    public Integer getRemainingSeats(@RequestParam("scheduleId") Integer scheduleId) {
        System.out.println("scheduleId:" + scheduleId);
        System.out.println( " 余票："+ scheduleService.getRemainingSeats(scheduleId));
        return scheduleService.getRemainingSeats(scheduleId);
    }

}
