package com.pighand.aio.vo.ECommerce;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PayVO {
    private String timeStamp;

    private String nonceStr;

    private String signType;

    private String paySign;

    private String prepayId;

    // TODO 支持List<String>转List<Long>
    private List<String> orderIds;
}
