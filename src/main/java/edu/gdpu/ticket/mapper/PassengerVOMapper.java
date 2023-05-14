package edu.gdpu.ticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.gdpu.ticket.entity.PassengerVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PassengerVOMapper extends BaseMapper<PassengerVO> {
    //修改 PassengerMapper 中的 select 方法，
    // 使用左关联查询 passenger 表和 user 表，并通过 selectColumns 方法来选择需要的列：
    @Select("SELECT a.*, b.username FROM passenger a \n" +
            "LEFT JOIN user b ON a.user_id = b.user_id  \n" +
            "WHERE a.is_deleted = 0 AND b.is_deleted = 0 \n" +
            "AND (b.username LIKE CONCAT('%', #{username}, '%') and a.name LIKE CONCAT('%', #{name}, '%'))\n")
    @Results({
            @Result(column = "user_id", property = "user.userId"),
            // 使用 @TableField 关联实体类 Passenger 中的 user 字段和数据库中的表名 user
            // 这里指定 User 实体类中的 username 列和 passenger 表中的 userName 列对应
            @Result(column = "username", property = "user.username")
    })
    List<PassengerVO> selectWithUsername(String username, String name);
}
