package com.example.ai.service;

import com.example.ai.domain.AiOrderQueryDto;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;

import java.util.List;

public interface ChatService {

    /**
     * 查询商品信息
     * @param userQuery 用户指令
     * @return 查询结果
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    List<OrderInfoVo> processOrderQuery(AiOrderQueryDto userQuery) throws UserException, SystemException;

    /**
     * 模拟自动下单
     * @param userQuery 用户指令
     * @return 下单结果
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    List<OrderInfoVo> processOrderAuto(AiOrderQueryDto userQuery) throws UserException, SystemException;

    /**
     * AI智能聊天助手
     * @param userQuery 用户输入
     * @throws SystemException 系统异常
     * @return AI响应
     */
    String processChatAssistant(String userQuery) throws SystemException;
}

