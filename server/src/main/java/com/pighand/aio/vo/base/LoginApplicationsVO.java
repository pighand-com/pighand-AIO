package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录用户可使用的应用
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@TableRef(ApplicationDomain.class)
@EqualsAndHashCode(callSuper = false)
public class LoginApplicationsVO extends ApplicationDomain {

    private String password;
}
