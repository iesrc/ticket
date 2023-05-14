package edu.gdpu.ticket.service;

import edu.gdpu.ticket.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 24100
* @description 针对表【news】的数据库操作Service
* @createDate 2023-03-11 20:55:07
*/
@Transactional(rollbackFor = Exception.class)//事务回滚,开启Spring的事务
public interface NewsService extends IService<News> {

    //根据新闻标题模糊查询
    List<News> findAllNews(String nameInput);
}
