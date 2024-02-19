package com.pighand.aio.service.common.impl;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.service.common.QrcodeService;
import com.pighand.aio.vo.common.QrcodeInfo;

public class QrcodeServiceImpl implements QrcodeService {

    /**
     * 二维码过期时间
     */
    private final int expire = 1000 * 60 * 5;

    public void encode(QrcodeInfo qrcodeInfo) {
        qrcodeInfo.setUId(Context.getLoginUser().getId());
        qrcodeInfo.setExpire(System.currentTimeMillis() + expire);
    }
}
