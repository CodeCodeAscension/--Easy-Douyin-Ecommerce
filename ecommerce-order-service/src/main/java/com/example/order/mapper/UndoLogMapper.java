package com.example.order.mapper;

import com.example.order.domain.UndoLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * AT transaction mode undo table Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Mapper
public interface UndoLogMapper extends BaseMapper<UndoLog> {

}
