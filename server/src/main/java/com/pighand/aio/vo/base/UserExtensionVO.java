package com.pighand.aio.vo.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.domain.base.UserWechatDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户扩展信息。id与user表相同
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@TableRef(UserExtensionDomain.class)
@EqualsAndHashCode(callSuper = false)
public class UserExtensionVO extends UserExtensionDomain {
    @JsonIgnoreProperties(value = {"id"})
    private UserVO user;

    @JsonIgnoreProperties(value = {"id"})
    private List<UserWechatDomain> userWechats;
}
