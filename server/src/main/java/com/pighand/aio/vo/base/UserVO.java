package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import lombok.Data;

import java.util.List;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@TableRef(UserDomain.class)
@Data
public class UserVO extends UserDomain {

    // relation table: begin
    private UserExtensionDomain extension;

    private ApplicationDomain application;
    // relation table: end

    private String token;

    private Integer bindCount;

    private List<ApplicationDomain> relevanceApplications;
}
