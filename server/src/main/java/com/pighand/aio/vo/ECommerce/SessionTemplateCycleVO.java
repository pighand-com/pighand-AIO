package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.SessionTemplateCycleDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Data
@TableRef(SessionTemplateCycleDomain.class)
@EqualsAndHashCode(callSuper = false)
public class SessionTemplateCycleVO extends SessionTemplateCycleDomain {

    // relation table: begin
    // relation table: end
}
