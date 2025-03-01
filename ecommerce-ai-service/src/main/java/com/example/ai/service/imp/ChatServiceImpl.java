package com.example.ai.service.imp;

import com.example.ai.aiUtil.DateTimeTools;
import com.example.ai.aiUtil.OrderPromptTemplate;
import com.example.ai.domain.aiOrderQueryDto;
import com.example.ai.service.ChatService;
import com.example.api.client.OrderClient;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.common.domain.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ZhiPuAiChatModel chatModel;
    private final OrderClient orderClient;
    private final OrderPromptTemplate promptTemplate;
    private final ObjectMapper objectMapper;
    private final com.example.ai.aiUtil.processPrompt processPrompt;

    @Override
    public List<OrderInfoVo> processOrderQuery(@NotNull aiOrderQueryDto userQuery) throws JsonProcessingException {

        // 调用AI模型,prompt是模型调用的输入
        Prompt prompt = promptTemplate.createPrompt(userQuery.getQueryContent());

        // 这里是在模型调用之后，返回的response对象
        ChatResponse response = chatModel.call(prompt);

        //我现在需要把返回的response对象转换为一个SearchOrderDto,然后调用订单服务查询订单信息
        String content = response.getResult().getOutput().getText();
        SearchOrderDto searchOrderDto = objectMapper.readValue(content, SearchOrderDto.class);

        //调用订单服务查询订单信息
        ResponseResult<List<OrderInfoVo>> listResponseResult = orderClient.searchOrders(searchOrderDto);

            Integer maxResultCount = userQuery.getMaxResultCount();
            if (maxResultCount != null && maxResultCount > 0) {
                //返回要求的最大结果数量
                if(maxResultCount < listResponseResult.getData().size()) {
                    return listResponseResult.getData().subList(0, maxResultCount);
                }else{
                    return listResponseResult.getData();
                }
            }

        return null;

    }

    // ai自动下单
    //事务回滚
    @Transactional
    @Override
    public List<OrderInfoVo> processOrderAuto(aiOrderQueryDto userQuery) throws JsonProcessingException {
        // 调用ai模型查询订单信息
        List<OrderInfoVo> orderInfoVos = processOrderQuery(userQuery);

        //如果不需要确认下单，则调用订单服务进行下单,返回下单的订单信息
        if (!userQuery.getNeedConfirm()) {

            //调用订单服务进行下单,返回下单的订单信息
            for (OrderInfoVo orderInfoVo : orderInfoVos) {
                //vo转dto
                PlaceOrderDto placeOrderDto = new PlaceOrderDto();
                placeOrderDto.setUserCurrency(orderInfoVo.getUserCurrency());
                placeOrderDto.setAddress(orderInfoVo.getAddress());
                placeOrderDto.setEmail(orderInfoVo.getEmail());
                placeOrderDto.setCartItems(orderInfoVo.getCartItems());

                //调用订单服务进行下单
                ResponseResult<PlaceOrderVo> placeOrderVoResponseResult = orderClient.placeOrder(placeOrderDto);

                //如果下单失败，则抛出异常回滚事务
                if (placeOrderVoResponseResult.getCode() != 200){
                    throw new RuntimeException("订单服务调用失败");
                }

            }

        }

        return orderInfoVos;
    }

    // ai聊天
    @Override
    public String processChatAssistant(String userQuery) {
        Prompt prompt = processPrompt.createPrompt(userQuery);
        try {

           //实现与ai大模型聊天对话
            String response = ChatClient.create(chatModel)
                    .prompt(prompt)
                    .tools(new DateTimeTools())
                    .call()
                    .content();
           // 返回的response对象转换为一个String
            String content = response;
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
