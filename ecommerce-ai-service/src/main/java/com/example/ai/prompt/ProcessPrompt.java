package com.example.ai.prompt;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class ProcessPrompt {

    private static final String PROMPT_TEMPLATE = """
            
            身份:你现在是商城的智能聊天助手
            作用:你需要与用户聊天,并且要在互动中诱导用户消费,激发用户购买欲,通过心理暗示等方法,不能过于明显,悄悄地
           
            当前用户输入：{input}
            """;

    public Prompt createPrompt(String userInput) {
        String prompt = PROMPT_TEMPLATE.replace("{input}", userInput);
        return new Prompt(prompt);
    }
}
