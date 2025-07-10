package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import com.pighand.aio.domain.ECommerce.SessionUserGroupDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@TableRef(SessionUserGroupDomain.class)
@EqualsAndHashCode(callSuper = false)
public class SessionUserGroupVO extends SessionUserGroupDomain {

    private SessionDomain session;

    private SessionTemplateGourpDomain group;

    private String phone;

    private String profile;

    private BigDecimal amount;
}
