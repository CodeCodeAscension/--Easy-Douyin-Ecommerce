package com.example.ai.prompt;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class OrderPrompt {

    private static final String PROMPT_TEMPLATE = """
            请从用户问题中提取订单查询参数，严格按以下要求返回JSON，不要返回Markdown格式，返回严格的JSON字符串：
            字段说明：
            - status (选填): 订单状态枚举[0(待确认)/1(待支付)/2(已支付)/3(支付失败)/4(已取消)]
            - createDateUpperBound (选填): 订单创建开始时间，格式yyyy-MM-dd
            - createDateLowerBound (选填): 必须与createTimeStart成对出现，且晚于开始时间
            
            校验规则：
            1. 时间范围非法时忽略该条件
            2. 无法识别的状态值视为无效
            3. 未提及的字段留空或设为null
            
            示例：
            用户输入：帮我查下5月1日到今天的已支付手机订单
            响应：
            {
              "status": 2,
              "createTimeStart": "2024-05-01",
              "createTimeEnd": "2025-02-20"
            }
            
            当前用户输入：{input}
            """;

    public Prompt createPrompt(String userInput) {
        return new Prompt(PROMPT_TEMPLATE.replace("{input}", userInput));
    }
}
