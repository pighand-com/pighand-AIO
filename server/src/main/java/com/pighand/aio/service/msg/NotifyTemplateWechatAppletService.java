package com.pighand.aio.service.msg;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.msg.NotifyTemplateWechatAppletDomain;
import com.pighand.aio.mapper.msg.NotifyTemplateWechatAppletMapper;
import com.pighand.aio.vo.msg.NotifyTemplateWechatAppletVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.msg.table.NotifyTemplateWechatAppletTableDef.NOTIFY_TEMPLATE_WECHAT_APPLET;

/**
 * 消息 - 通知模板 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 18:35:39
 */
@Service
public class NotifyTemplateWechatAppletService
    extends BaseServiceImpl<NotifyTemplateWechatAppletMapper, NotifyTemplateWechatAppletDomain> {

    /**
     * 创建
     *
     * @param notifyTemplateWechatAppletVO
     * @return
     */
    public NotifyTemplateWechatAppletVO create(NotifyTemplateWechatAppletVO notifyTemplateWechatAppletVO) {
        super.mapper.insert(notifyTemplateWechatAppletVO);

        return notifyTemplateWechatAppletVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public NotifyTemplateWechatAppletDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param notifyTemplateWechatAppletVO
     * @return PageOrList<NotifyTemplateWechatAppletVO>
     */
    public PageOrList<NotifyTemplateWechatAppletVO> query(NotifyTemplateWechatAppletVO notifyTemplateWechatAppletVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(NOTIFY_TEMPLATE_WECHAT_APPLET.TEMPLATE_ID.like(notifyTemplateWechatAppletVO.getTemplateId()))
            .and(NOTIFY_TEMPLATE_WECHAT_APPLET.URL.like(notifyTemplateWechatAppletVO.getUrl()))

            // equal
            .and(NOTIFY_TEMPLATE_WECHAT_APPLET.APPLICATION_ID.eq(notifyTemplateWechatAppletVO.getApplicationId()))
            .and(NOTIFY_TEMPLATE_WECHAT_APPLET.TYPE.eq(notifyTemplateWechatAppletVO.getType()));

        return super.mapper.query(notifyTemplateWechatAppletVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param notifyTemplateWechatAppletVO
     */
    public void update(NotifyTemplateWechatAppletVO notifyTemplateWechatAppletVO) {
        UpdateChain updateChain =
            this.updateChain().where(NOTIFY_TEMPLATE_WECHAT_APPLET.ID.eq(notifyTemplateWechatAppletVO.getId()));

        updateChain.set(NOTIFY_TEMPLATE_WECHAT_APPLET.ID, notifyTemplateWechatAppletVO.getId(),
            VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
