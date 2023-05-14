package edu.gdpu.ticket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName order
 */
@TableName(value ="t_order")
@Data
public class OrderVO implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer orderId;

    /**
     * 所属用户ID，关联user表
     */
    private Integer userId;
    private User user;

    /**
     * 班次ID，关联schedule表
     */
    private Integer scheduleId;
    private Schedule schedule;

    private Route route;

    /**
     * 订单状态（已支付、已取消...）
     */
    private String status;

    /**
     * 支付时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 购买票数
     */
    private Integer count;

    /**
     * 订单金额
     */
    private Double money;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}