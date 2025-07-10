package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableRef(SessionTemplateDomain.class)
@EqualsAndHashCode(callSuper = false)
public class SessionTemplateVO extends SessionTemplateDomain {
}
