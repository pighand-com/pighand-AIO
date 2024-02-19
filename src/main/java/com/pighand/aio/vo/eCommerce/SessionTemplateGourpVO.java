package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.SessionTemplateGourpDomain;
import lombok.Data;

import java.util.List;

@Data
public class SessionTemplateGourpVO extends SessionTemplateGourpDomain {
    private List<SessionUserGroupVO> users;
}
