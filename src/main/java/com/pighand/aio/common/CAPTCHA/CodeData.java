package com.pighand.aio.common.CAPTCHA;

import lombok.Data;

/**
 * 验证码数据
 */
@Data
public class CodeData {
    private String code;
    private byte[] byteArray;
}
