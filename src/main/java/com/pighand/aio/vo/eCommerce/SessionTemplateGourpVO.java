package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import lombok.Data;

import java.util.List;

@Data
public class SessionTemplateGourpVO extends SessionTemplateGourpDomain {
    private List<SessionUserGroupVO> users;
}
