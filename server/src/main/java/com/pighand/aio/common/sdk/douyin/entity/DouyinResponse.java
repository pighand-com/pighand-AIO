package com.pighand.aio.common.sdk.douyin.entity;

import com.pighand.aio.common.sdk.ResponseEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 错误信息
 *
 * @author wangshuli
 */
@Data
@NoArgsConstructor
public class DouyinResponse extends ResponseEntity {

    /**
     * 错误码 0-success
     */
    private Integer errcode;

    private String errmsg;

    private String openid;
    private String unionid;

    public DouyinResponse(String tip) {
        super("0", tip);
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;

        super.checkError(this.errcode, this.errmsg);
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;

        super.checkError(this.errcode, this.errmsg);
    }
}
