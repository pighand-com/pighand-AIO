package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.SessionDomain;
import lombok.Data;

import java.util.List;

@Data
public class SessionVO extends SessionDomain {
    private Long sessionGroupId;

    private List<SessionTemplateGourpVO> groups;

    private SessionTemplateVO sessionTemplate;
}
