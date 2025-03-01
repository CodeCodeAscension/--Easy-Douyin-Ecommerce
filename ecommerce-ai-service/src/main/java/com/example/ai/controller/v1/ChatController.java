package com.example.ai.controller.v1;

import com.example.ai.domain.AiOrderQueryDto;
import com.example.ai.service.ChatService;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.exception.SystemException;
import com.example.common.util.UserContextUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
@Tag(name = "ai服务接口", description = "用于ai查询订单信息,自动下单,智能助手")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "ai查询订单信息")
    @PostMapping("/orders/query")
    public ResponseResult<List<OrderInfoVo>> processOrderQuery(@RequestBody @Validated AiOrderQueryDto userQuery) throws JsonProcessingException {
        UserContextUtil.getUserId();
        List<OrderInfoVo> orderInfoVos = chatService.processOrderQuery(userQuery);
        if (orderInfoVos != null) {
            return ResponseResult.success(orderInfoVos);
        }
        throw new SystemException("查询失败");
    }

    @Operation(summary = "ai自动下单")
    @PostMapping("/orders/auto-place")
    public ResponseResult<List<OrderInfoVo>> processOrderAuto(@RequestBody @Validated AiOrderQueryDto userQuery) throws JsonProcessingException {
        UserContextUtil.getUserId();
        List<OrderInfoVo> orderInfoVos = chatService.processOrderAuto(userQuery);
        if (orderInfoVos == null) {
            throw new SystemException("查询失败");
        }
        return ResponseResult.success(orderInfoVos);
    }

    @Operation(summary = "ai智能助手")
    @GetMapping("/chat/assistant")
    public ResponseResult<String> processChatAssistant(@RequestParam("userQuery") String userQuery)  {
        UserContextUtil.getUserId();
        String response = chatService.processChatAssistant(userQuery);
        if (response == null) {
            throw new SystemException("回复失败");
        }
        return ResponseResult.success(response);
    }
}