package com.pighand.aio.vo.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    // 用户id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}
