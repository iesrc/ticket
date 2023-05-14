package edu.gdpu.ticket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName schedule
 */
@TableName(value ="schedule")
@Data
public class Schedule1 implements Serializable {
    /**
     * 班次ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer scheduleId;

    /**
     * 路线ID，关联route表
     */
    private Integer routeId;

    /**
     * 出发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date departureTime;

    /**
     * 行驶时间
     */
    private String duration;

    /**
     * 车牌号码
     */
    private String busNumber;

    /**
     * 票价
     */
    private Double price;

    /**
     * 剩余座位
     */
    private Integer seatsLeft;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}