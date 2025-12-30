package com.pighand.aio.service.common;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.vo.common.QrcodeInfo;

public class QrcodeService  {

    /**
     * 二维码过期时间
     */
    private final int expire = 1000 * 60 * 5;

    public void encode(QrcodeInfo qrcodeInfo) {
        qrcodeInfo.setUId(Context.loginUser().getId());
        qrcodeInfo.setExpire(System.currentTimeMillis() + expire);
    }
}
