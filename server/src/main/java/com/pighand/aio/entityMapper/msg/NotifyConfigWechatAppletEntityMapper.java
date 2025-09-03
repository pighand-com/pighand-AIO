package com.pighand.aio.entityMapper.msg;

import com.pighand.aio.domain.msg.NotifyConfigWechatAppletDomain;
import com.pighand.aio.vo.msg.NotifyConfigWechatAppletVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 消息 - 通知配置 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 21:40:13
 */
@Mapper(componentModel = "spring")
public interface NotifyConfigWechatAppletEntityMapper {

    NotifyConfigWechatAppletVO toVo(NotifyConfigWechatAppletDomain entity);

    NotifyConfigWechatAppletDomain toDomain(NotifyConfigWechatAppletVO vo);

    List<NotifyConfigWechatAppletVO> toVoList(List<NotifyConfigWechatAppletDomain> entity);

    List<NotifyConfigWechatAppletDomain> toDomainList(List<NotifyConfigWechatAppletVO> vo);
}
