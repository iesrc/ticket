package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
* @author 24100
* @description 针对表【news】的数据库操作Mapper
* @createDate 2023-03-11 20:55:07
* @Entity edu.gdpu.ticket.entity.News
*/
@Component
public interface NewsMapper extends BaseMapper<News> {

}




