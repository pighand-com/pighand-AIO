package com.pighand.aio.common.CAPTCHA;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 验证码数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"code"})
public class CodeData {
    private String captchaId;

    private String code;

    private String base64;
}
