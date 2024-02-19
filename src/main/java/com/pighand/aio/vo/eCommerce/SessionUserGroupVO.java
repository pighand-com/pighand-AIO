package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.SessionDomain;
import com.pighand.aio.domain.eCommerce.SessionTemplateGourpDomain;
import com.pighand.aio.domain.eCommerce.SessionUserGroupDomain;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SessionUserGroupVO extends SessionUserGroupDomain {

    private SessionDomain session;

    private SessionTemplateGourpDomain group;

    private String phone;

    private String profile;

    private BigDecimal amount;
}
