package com.pighand.aio.common.sdk;

import com.pighand.framework.spring.exception.ThrowException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.util.StringUtils;

/**
 * 统一处理SDK返回值异常
 *
 * @author wangshuli
 */
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity {

    private String successCode;
    private String tip;

    protected void checkError(Integer code, String message) {
        if (null != code) {
            this.checkError(code.toString(), message);
        }
    }

    protected void checkError(String code, String message) {
        if (!successCode.equals(code) && StringUtils.hasText(message)) {
            throw new ThrowException(
                String.format("%s%s: %s", StringUtils.hasText(tip) ? tip + " - " : "", code, message));
        }
    }
}
