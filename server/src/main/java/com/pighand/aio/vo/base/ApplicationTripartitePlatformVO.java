package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationTripartitePlatformDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目三方平台配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@TableRef(ApplicationTripartitePlatformDomain.class)
@EqualsAndHashCode(callSuper = false)
public class ApplicationTripartitePlatformVO extends ApplicationTripartitePlatformDomain {
}
