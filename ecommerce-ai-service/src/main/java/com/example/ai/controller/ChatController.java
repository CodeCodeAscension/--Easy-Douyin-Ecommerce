package com.example.ai.controller;

import com.example.ai.aiUtil.OrderPromptTemplate;
import com.example.ai.domain.aiOrderQueryDto;
import com.example.ai.service.ChatService;
import com.example.api.client.OrderClient;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.common.domain.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="ai服务接口",description = "用于ai查询订单信息,自动下单,智能助手")
public class ChatController {

   private final ChatService chatService;
   //private final OrderClient orderClient;


    @Operation(summary = "ai查询订单信息")
    @PostMapping("/api/v1/ai/orders/query")
    public ResponseResult<List<OrderInfoVo>> processOrderQuery(@RequestBody aiOrderQueryDto userQuery) throws JsonProcessingException {

        List<OrderInfoVo> orderInfoVos = chatService.processOrderQuery(userQuery);
        if (orderInfoVos != null) {
            return ResponseResult.success(orderInfoVos);
        }

            return ResponseResult.error(500, "查询失败");
    }



    @Operation(summary = "ai自动下单")
    @PostMapping("/api/v1/ai/orders/auto-place")
    public ResponseResult<List<OrderInfoVo>> processOrderAuto(@RequestBody aiOrderQueryDto userQuery) throws JsonProcessingException {

        List<OrderInfoVo> orderInfoVos = chatService.processOrderAuto(userQuery);
        if (orderInfoVos == null) {
            return ResponseResult.error(500, "查询失败");
        }

        return ResponseResult.success(orderInfoVos);

    }


    @Operation(summary = "ai智能助手")
    @GetMapping("/api/v1/ai/chat/assistant")
    public ResponseResult<String> processChatAssistant(@RequestParam("userQuery") String userQuery)  {

        String response = chatService.processChatAssistant(userQuery);
        if (response == null) {
            return ResponseResult.error(500, "回复失败");
        }
        return ResponseResult.success(response);
    }

}