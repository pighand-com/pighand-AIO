package com.pighand.aio.vo.common;

import com.pighand.aio.common.enums.QrcodeType;
import lombok.Data;

/**
 * 二维码信息
 */
@Data
public class QrcodeInfo {
    // 二维码类型
    private QrcodeType type;

    // 验证码操作数据的id
    private Long id;

    // 用户id
    private Long uId;

    // 过期时间
    private Long expire;

}
