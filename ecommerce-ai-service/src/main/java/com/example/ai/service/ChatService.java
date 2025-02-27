package com.example.ai.service;

import com.example.ai.domain.aiOrderQueryDto;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ChatService {

     List<OrderInfoVo> processOrderQuery(aiOrderQueryDto userQuery) throws JsonProcessingException;

    List<OrderInfoVo> processOrderAuto(aiOrderQueryDto userQuery) throws JsonProcessingException;

    String processChatAssistant(String userQuery);
}

