package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationAuthorizationDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Authorization配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableRef(ApplicationAuthorizationDomain.class)
public class ApplicationAuthorizationVO extends ApplicationAuthorizationDomain {
}
