package edu.gdpu.ticket.controller;

import edu.gdpu.ticket.entity.News;
import edu.gdpu.ticket.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/3/6 18:32
 */
@Controller
public class TestController {

    @Autowired
    private NewsService newsService;

    @RequestMapping("/error1")
    public String error(){
        return "templates/error/404";
    }

    @RequestMapping("/test")
    public String test(){
        return "temp/breadTest";
    }

    @RequestMapping("/notice")
    public ModelAndView notice(HttpSession session){
        ModelAndView mv = new ModelAndView();
        List<News> newsList = newsService.list(null);
        session.setAttribute("notices",newsList);
        mv.setViewName("temp/test2");
        return mv;
    }
}
