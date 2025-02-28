package com.example.order.service.impl;

import com.example.order.domain.UndoLog;
import com.example.order.mapper.UndoLogMapper;
import com.example.order.service.IUndoLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * AT transaction mode undo table 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Service
public class UndoLogServiceImpl extends ServiceImpl<UndoLogMapper, UndoLog> implements IUndoLogService {

}
