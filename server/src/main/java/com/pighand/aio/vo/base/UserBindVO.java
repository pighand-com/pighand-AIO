package com.pighand.aio.vo.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.UserBindDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
@TableRef(UserBindDomain.class)
@EqualsAndHashCode(callSuper = false)
public class UserBindVO extends UserBindDomain {
    private UserVO user;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bindUserId;
}
