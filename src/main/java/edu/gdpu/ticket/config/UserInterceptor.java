package edu.gdpu.ticket.config;

import edu.gdpu.ticket.entity.Admin;
import edu.gdpu.ticket.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author:iesrc
 * @Crate:2022/11/18 14:48
 */

public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        User user = (User) session.getAttribute("user");
        Admin admin = (Admin) session.getAttribute("admin");
        if(user!=null || admin != null){  //用户不为空
            return true;
        }else{
            //主要是用来防止session失效后，跳不出当前窗口，我希望它在顶级窗口打开
            //通常情况下，Session默认的有效时长是30分钟，即如果用户在30分钟内没有与服务器进行任何交互，
            // 那么该Session就会过期，需要重新登录才能重新创建一个新的Session。但是，这个时间也可以通过配置文件或者编程的方式进行修改。
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write("<script>window.parent.location.href='/login';</script>");
            return false;
        }
    }
}

