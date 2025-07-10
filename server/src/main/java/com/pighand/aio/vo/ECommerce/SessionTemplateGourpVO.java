package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@TableRef(SessionTemplateGourpDomain.class)
@EqualsAndHashCode(callSuper = false)
public class SessionTemplateGourpVO extends SessionTemplateGourpDomain {
    private List<SessionUserGroupVO> users;
}
