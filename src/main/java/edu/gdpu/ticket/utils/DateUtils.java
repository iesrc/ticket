package edu.gdpu.ticket.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/3/29 23:21
 */
public class DateUtils {

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        // 使用默认时区获取Instant对象
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        // 转换为Date类型
        return Date.from(instant);
    }
}