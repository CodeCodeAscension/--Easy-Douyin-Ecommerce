package com.example.ai.service.impl;

import com.example.ai.tool.DateTimeTool;
import com.example.ai.prompt.OrderPrompt;
import com.example.ai.domain.AiOrderQueryDto;
import com.example.ai.service.ChatService;
import com.example.ai.prompt.ProcessPrompt;
import com.example.ai.tool.OrderTool;
import com.example.api.client.OrderClient;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ZhiPuAiChatModel chatModel;
    private final OrderClient orderClient;
    private final OrderPrompt promptTemplate;
    private final ObjectMapper objectMapper;
    private final ProcessPrompt processPrompt;

    private final DateTimeTool dateTimeTool;
    private final OrderTool orderTool;

    /**
     * 去掉Markdown标记
     * @param input 输入文本
     * @return 处理后的文本
     */
    private static String removeMarkdownCodeBlock(String input) {
        Pattern pattern = Pattern.compile(
                "^\\s*```[\\w]*\\n?(.*?)\\n?```\\s*$",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return matcher.group(1).trim();
        }
        return input;
    }

    @Override
    public List<OrderInfoVo> processOrderQuery(@NotNull AiOrderQueryDto userQuery) throws UserException, SystemException {
        // 调用AI模型,prompt是模型调用的输入
        Prompt prompt = promptTemplate.createPrompt(userQuery.getQueryContent());
//        ChatResponse response = chatModel.call(prompt);
//        String content = response.getResult().getOutput().getText();
        String content = ChatClient.create(chatModel)
                .prompt(prompt)
                .tools(dateTimeTool)
                .tools(orderTool)
                .call()
                .content();
        if (content == null) {
            log.info("AI模型未能返回数据");
            throw new BadRequestException("模型未能正确理解用户要求");
        }
        content = removeMarkdownCodeBlock(content);
        SearchOrderDto searchOrderDto;
        try {
            searchOrderDto = objectMapper.readValue(content, SearchOrderDto.class);
        } catch (Exception e) {
            log.error("转换为SearchOrderDto出错： {}", e.getMessage());
            throw new BadRequestException("模型未能正确理解用户要求");
        }

        //调用订单服务查询订单信息
        ResponseResult<List<OrderInfoVo>> listResponseResult = orderClient.searchOrders(searchOrderDto);
        if(listResponseResult.getCode() != ResultCode.SUCCESS || listResponseResult.getData() == null) {
            throw new SystemException(listResponseResult.getMsg());
        }

        Integer maxResultCount = userQuery.getMaxResultCount();
        if (maxResultCount != null && maxResultCount > 0) {
            //返回要求的最大结果数量
            if(maxResultCount < listResponseResult.getData().size()) {
                return listResponseResult.getData().subList(0, maxResultCount);
            }
        }
        return listResponseResult.getData();
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public List<OrderInfoVo> processOrderAuto(AiOrderQueryDto userQuery) throws UserException, SystemException {
        // 调用ai模型查询订单信息
        List<OrderInfoVo> orderInfoVos = processOrderQuery(userQuery);

        //如果不需要确认下单，则调用订单服务进行下单,返回下单的订单信息
        if (!userQuery.getNeedConfirm()) {

            //调用订单服务进行下单,返回下单的订单信息
            for (OrderInfoVo orderInfoVo : orderInfoVos) {
                //vo转dto
                PlaceOrderDto placeOrderDto = new PlaceOrderDto();
                placeOrderDto.setUserCurrency(orderInfoVo.getUserCurrency());
                placeOrderDto.setAddressId(orderInfoVo.getAddress().getId());
                placeOrderDto.setEmail(orderInfoVo.getEmail());
                placeOrderDto.setCartItems(orderInfoVo.getCartItems());

                //调用订单服务进行下单
                ResponseResult<PlaceOrderVo> placeOrderVoResponseResult = orderClient.placeOrder(placeOrderDto);

                //如果下单失败，则抛出异常回滚事务
                if (placeOrderVoResponseResult.getCode() != ResultCode.SUCCESS) {
                    throw new SystemException("订单服务调用失败");
                }
            }
        }
        return orderInfoVos;
    }

    @Override
    public String processChatAssistant(String userQuery) throws SystemException {
        Prompt prompt = processPrompt.createPrompt(userQuery);
        try {
            // 实现与ai大模型聊天对话功能
            // 返回的response对象转换为一个String
            return ChatClient.create(chatModel)
                    .prompt(prompt)
                    .tools(dateTimeTool)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
