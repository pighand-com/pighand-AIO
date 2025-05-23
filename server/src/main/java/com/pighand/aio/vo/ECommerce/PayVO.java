package com.pighand.aio.vo.ECommerce;

import lombok.Data;

@Data
public class PayVO {
    private String timeStamp;

    private String nonceStr;

    private String signType;

    private String paySign;

    private String prepayId;
}
