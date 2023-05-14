package edu.gdpu.ticket.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 路线表
 * @TableName route
 */
@TableName(value ="route")
@Data
public class Route implements Serializable {
    /**
     * 路线ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer routeId;

    /**
     * 出发地
     */
    private String departure;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}