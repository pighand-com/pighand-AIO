package com.pighand.aio.vo.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.aio.domain.ECommerce.WalletBillDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class WalletBillVO extends WalletBillDomain {
    private WalletTransferVO walletTransfer;

    @Schema(description = "查询的top数")
    private Integer top;

    @Schema(description = "按日期查询top，day：天，week：周，month：月，year：年")
    private String topByDateType;

    @Schema(description = "场次查询top")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long topBySessionId;

    @Schema(description = "总数统计类型 10：总金额，20：当前场次总金额")
    private Integer TotalType;
}
