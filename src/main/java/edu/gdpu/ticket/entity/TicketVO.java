package edu.gdpu.ticket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName ticket
 */
@TableName(value ="ticket")
@Data
public class TicketVO implements Serializable {
    /**
     * 车票ID ，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer ticketId;

    /**
     * 所属订单ID，关联order表
     */
    private Integer orderId;
    private Order order;

    private Schedule schedule;

    private Route route;

    private User user;

    /**
     * 乘客ID，关联passenger表
     */
    private Integer passengerId;
    private Passenger passenger;

    /**
     * 状态（已支付、已退票...）
     */
//    private String status;

    /**
     * 是否允许改签
     * 默认0能改签,不为0的就是改签之前那张车票的主键id
     */
    private Integer canModify;

    /**
     *是否过期
     */
    private Boolean isExpired; // 新增属性

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}