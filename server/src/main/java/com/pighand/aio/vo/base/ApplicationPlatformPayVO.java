package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Data
@TableRef(ApplicationPlatformPayDomain.class)
@EqualsAndHashCode(callSuper = false)
public class ApplicationPlatformPayVO extends ApplicationPlatformPayDomain {

    // relation table: begin
    // relation table: end
}
