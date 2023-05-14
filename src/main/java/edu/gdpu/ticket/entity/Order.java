package edu.gdpu.ticket.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName order
 */
@TableName(value ="t_order")
@Data
public class Order implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer orderId;

    /**
     * 所属用户ID，关联user表
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 班次ID，关联schedule表
     */
    @TableField("schedule_id")
    private Integer scheduleId;

    /**
     * 改签后给用户的退款金额
     */
//    private Double refund;

    /**
     * 订单状态（已支付、已取消...）
     */
    private String status;

    /**
     * 支付时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("pay_time")
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
    @TableField("is_deleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}