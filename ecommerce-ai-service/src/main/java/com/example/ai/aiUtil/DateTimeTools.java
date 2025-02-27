package com.example.ai.aiUtil;

import java.time.LocalDateTime;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateTimeTools {

    @Tool(description = "这个方法可以获取当前时间,以用于分析当前是几点和今天是星期几这样的问题")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

}