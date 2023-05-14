package edu.gdpu.ticket.mapper;

import edu.gdpu.ticket.entity.Passenger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 24100
* @description 针对表【passenger】的数据库操作Mapper
* @createDate 2023-03-07 14:04:48
* @Entity edu.gdpu.ticket.entity.Passenger
*/
@Component
public interface PassengerMapper extends BaseMapper<Passenger> {

    //通过用户id和身份证号查询是否已存在该乘客
    Passenger getByUserIdAndIdentityPassenger(@Param("userId") Integer userId, @Param("identity") String identity);


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
    List<Passenger> selectWithUsername(String username, String name);

    @Select("select * from passenger where is_deleted = 0 and user_id = #{userId}")
    List<Passenger> selectByUserId(@Param("userId") Integer userId);

    /**
     * 根据多个id查询乘客列表
     * @param passengerIds 乘客id列表
     * @return 乘客列表
     */
    List<Passenger> selectPassengersByIds(@Param("passengerIds") List<Integer> passengerIds);

}




