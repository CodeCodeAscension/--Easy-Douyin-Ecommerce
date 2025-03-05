package com.example.common.domain.message;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PayFailMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String orderId;
    private List<ProductQuantity> products;
    private List<Long> addProductIds;      // 需要恢复库存的ID
}
