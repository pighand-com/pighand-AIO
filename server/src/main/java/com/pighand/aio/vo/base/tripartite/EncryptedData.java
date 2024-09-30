package com.pighand.aio.vo.base.tripartite;

import lombok.Data;

/**
 * 加密信息
 * 用于解密手机号等三方平台的用户加密信息
 */
@Data
public class EncryptedData {
    private String code;
}
