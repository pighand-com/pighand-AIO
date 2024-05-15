package com.pighand.aio.vo.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.aio.common.enums.QrcodeType;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;

/**
 * 二维码信息
 */
@Data
public class QrcodeInfo {
    // 二维码类型
    private QrcodeType type;

    // 验证码操作数据的id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    // 用户id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uId;

    // 过期时间
    private Long expire;

}
