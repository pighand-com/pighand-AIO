package com.pighand.aio.vo.ECommerce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 电商 - 钱包 - 排行榜
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WalletTopVO {

    @Schema(description = "查询的top数")
    private Integer top;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "tokens")
    private BigDecimal tokens;
}
