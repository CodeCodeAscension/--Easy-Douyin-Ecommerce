package com.example.order.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;

import com.example.order.enums.logStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * AT transaction mode undo table
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("undo_log")
@Schema(description = "AT事务模式回滚日志表")
public class UndoLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分支事务ID")
    @TableId(value = "branch_id", type = IdType.ASSIGN_ID)
    private Long branchId;

    @Schema(description = "全局事务ID")
    private String xid;

    @Schema(description = "回滚日志上下文，例如序列化信息")
    private String context;

    @Schema(description = "回滚信息")
    private Blob rollbackInfo;

    @Schema(description = "日志状态（0:正常状态，1:防御状态）")
    private logStatusEnum logStatus;

    @Schema(description = "创建时间")
    private LocalDateTime logCreated;

    @Schema(description = "修改时间")
    private LocalDateTime logModified;

}
