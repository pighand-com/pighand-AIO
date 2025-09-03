package com.pighand.aio.entityMapper.msg;

import com.pighand.aio.domain.msg.NotifyTemplateWechatAppletDomain;
import com.pighand.aio.vo.msg.NotifyTemplateWechatAppletVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 消息 - 通知模板 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 18:35:39
 */
@Mapper(componentModel = "spring")
public interface NotifyTemplateWechatAppletEntityMapper {

    NotifyTemplateWechatAppletVO toVo(NotifyTemplateWechatAppletDomain entity);

    NotifyTemplateWechatAppletDomain toDomain(NotifyTemplateWechatAppletVO vo);

    List<NotifyTemplateWechatAppletVO> toVoList(List<NotifyTemplateWechatAppletDomain> entity);

    List<NotifyTemplateWechatAppletDomain> toDomainList(List<NotifyTemplateWechatAppletVO> vo);
}
