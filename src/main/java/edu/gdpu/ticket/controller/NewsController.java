package edu.gdpu.ticket.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.gdpu.ticket.entity.News;
import edu.gdpu.ticket.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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
@RequestMapping("/ticket/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @ResponseBody
    @RequestMapping("/findAllNews")
    public ModelAndView findAllNews(HttpSession session, @RequestParam(name = "pageNo",required = false,defaultValue = "1") int pageNo,
                                    @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize,
                                    @RequestParam(name = "nameInput",required = false,defaultValue = "") String nameInput,
                                    @RequestParam(name = "pageTotal",required = false,defaultValue = "0") int pageTotal){
        ModelAndView mv = new ModelAndView();
        PageHelper.startPage(pageNo,pageSize);
        List<News> newsList = newsService.findAllNews(nameInput);
        mv.addObject("newsList",newsList);
        mv.addObject("newsNum",newsList.size());
        PageInfo<News> pageInfo = new PageInfo<>(newsList);
        session.setAttribute("pageInfo",pageInfo);
        mv.setViewName("news");
        session.setAttribute("pageNo",pageNo);
        session.setAttribute("pageSize",pageSize);
        session.setAttribute("nameInput",nameInput);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/add")
    public Map<String,Object> addNews(News news){
        Map<String,Object> map = new HashMap<>();
        System.out.println("news" + news);
        Date date = new Date();
        System.out.println("date" + date);
        news.setSaveTime(date);
        boolean save = newsService.save(news);
        if (save){
            map.put("success",true);
            map.put("msg","添加成功");
        }else {
            map.put("success",false);
            map.put("msg","添加失败");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/deleteNewsById")
    public String deleteNewsById(@RequestParam("id") Integer id){

        boolean result = newsService.removeById(id);
        if (result){
            return "SUCCESS";
        }else {
            return "fail";
        }

    }

    @ResponseBody
    @RequestMapping("/update")
    public Map<String,Object> updateNews(News news){
        Map<String,Object> map = new HashMap<>();
        System.out.println("news" + news);
        Date date = new Date();
        System.out.println("date" + date);
        news.setSaveTime(date);
        boolean save = newsService.updateById(news);
        if (save){
            map.put("success",true);
            map.put("msg","修改成功");
        }else {
            map.put("success",false);
            map.put("msg","修改失败");
        }
        return map;
    }

}
