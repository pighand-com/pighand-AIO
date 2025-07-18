package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@TableRef(ApplicationPlatformKeyDomain.class)
@EqualsAndHashCode(callSuper = false)
public class ApplicationPlatformKeyVO extends ApplicationPlatformKeyDomain {
}
