package com.pighand.aio.service.msg;

import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.msg.NotifyConfigWechatAppletDomain;
import com.pighand.aio.domain.msg.NotifyTemplateWechatAppletDomain;
import com.pighand.aio.mapper.msg.NotifyConfigWechatAppletMapper;
import com.pighand.aio.vo.msg.NotifyConfigWechatAppletVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.msg.table.NotifyConfigWechatAppletTableDef.NOTIFY_CONFIG_WECHAT_APPLET;
import static com.pighand.aio.domain.msg.table.NotifyTemplateWechatAppletTableDef.NOTIFY_TEMPLATE_WECHAT_APPLET;

/**
 * 消息 - 通知配置 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 21:40:13
 */
@Service
@RequiredArgsConstructor
public class NotifyConfigWechatAppletService
    extends BaseServiceImpl<NotifyConfigWechatAppletMapper, NotifyConfigWechatAppletDomain> {

    private final NotifyTemplateWechatAppletService notifyTemplateWechatAppletService;

    /**
     * 同步模板
     *
     * @param type
     * @param dataId
     */
    public void syncByTemplate(Integer type, Long dataId) {
        NotifyTemplateWechatAppletDomain notifyTemplateWechatAppletDomain =
            notifyTemplateWechatAppletService.queryChain()
                .where(NOTIFY_TEMPLATE_WECHAT_APPLET.APPLICATION_ID.eq(Context.applicationId()))
                .and(NOTIFY_TEMPLATE_WECHAT_APPLET.TYPE.eq(type)).one();

        if (notifyTemplateWechatAppletDomain == null) {
            throw new ThrowException("该业务未配置模板");
        }

        NotifyConfigWechatAppletDomain notifyConfigWechatAppletDomain = new NotifyConfigWechatAppletDomain();
        notifyConfigWechatAppletDomain.setNotifyTemplateId(notifyTemplateWechatAppletDomain.getId());
        notifyConfigWechatAppletDomain.setDataType(notifyTemplateWechatAppletDomain.getType());
        notifyConfigWechatAppletDomain.setDataId(dataId);
        notifyConfigWechatAppletDomain.setTemplateId(notifyTemplateWechatAppletDomain.getTemplateId());
        notifyConfigWechatAppletDomain.setUrl(notifyTemplateWechatAppletDomain.getUrl());
        notifyConfigWechatAppletDomain.setNotified(false);
        notifyConfigWechatAppletDomain.setData(notifyTemplateWechatAppletDomain.getData());

        super.mapper.insert(notifyConfigWechatAppletDomain);
    }

    /**
     * 详情
     *
     * @param dataId
     * @return
     */
    public NotifyConfigWechatAppletVO findByDataId(Long dataId, Integer type) {
        return queryChain().where(NOTIFY_CONFIG_WECHAT_APPLET.DATA_ID.eq(dataId))
            .and(NOTIFY_CONFIG_WECHAT_APPLET.DATA_TYPE.eq(type)).oneAs(NotifyConfigWechatAppletVO.class);
    }

    /**
     * 修改
     *
     * @param msgNotifyConfigWechatAppletVO
     */
    public void update(NotifyConfigWechatAppletVO msgNotifyConfigWechatAppletVO) {
        UpdateChain updateChain =
            this.updateChain().where(NOTIFY_CONFIG_WECHAT_APPLET.ID.eq(msgNotifyConfigWechatAppletVO.getId()));

        updateChain.set(NOTIFY_CONFIG_WECHAT_APPLET.ID, msgNotifyConfigWechatAppletVO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

}
