package edu.gdpu.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.gdpu.ticket.entity.News;
import edu.gdpu.ticket.service.NewsService;
import edu.gdpu.ticket.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 24100
* @description 针对表【news】的数据库操作Service实现
* @createDate 2023-03-11 20:55:07
*/
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
    implements NewsService{

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public List<News> findAllNews(String nameInput) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",nameInput);
        return newsMapper.selectList(queryWrapper);
    }
}




