package com.pighand.aio.common.listener;

import com.pighand.aio.common.base.ApplicationIdAware;
import com.pighand.aio.common.interceptor.Context;

/**
 * 插入监听
 * 设置所属应用id
 */
public class InsertListener implements com.mybatisflex.annotation.InsertListener {

    @Override
    public void onInsert(Object entity) {
        Long applicationId = Context.applicationId();

        if (entity instanceof ApplicationIdAware domain) {
            domain.setApplicationId(applicationId);
        }

    }
}
