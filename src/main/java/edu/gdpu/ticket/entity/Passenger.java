package edu.gdpu.ticket.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName passenger
 * 乘客和用户的关系是一对多的关系，也可以说是多个乘客对应一个用户。
 */
@TableName(value ="passenger")
@Data
public class Passenger implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer passengerId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String identity;

    /**
     * 性别
     */
    private String gender;

    /**
     * 逻辑删除字段

     */
    @TableLogic
    private Integer isDeleted;

//    private User user; // 增加了一个关联对象 user，表示用户信息

  /*  public String getUserName() {
        return user.getUsername();
    }
    public void setUserName(String userName) { // 此处为了保持接口的兼容性，实际不需要实现
        // do nothing
    }*/

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}