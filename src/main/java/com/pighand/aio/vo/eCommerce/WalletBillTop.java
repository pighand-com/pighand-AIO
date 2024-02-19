package com.pighand.aio.vo.eCommerce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBillTop {
    @Schema(description = "金额")
    private BigDecimal amount;

    private Integer userCount;
    private Long sessionGroupId;
    private String sessionGroupName;

    private Long userId;
    private String userName;
}
