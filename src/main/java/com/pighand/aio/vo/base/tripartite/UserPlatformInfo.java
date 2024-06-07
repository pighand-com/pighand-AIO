package com.pighand.aio.vo.base.tripartite;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户平台信息
 *
 * <p>根据code解析的信息，判断用户是否存在
 *
 * @author wangshuli
 */
@Data
@AllArgsConstructor
public class UserPlatformInfo {

    /**
     * 用户平台信息id。不存在需要创建
     */
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dbId;
    /**
     * 系统用户id
     */
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 三方平台信息
     */
    private String dbOpenid;
    private String dbAnonymousOpenid;
    private String dbUnionid;

    public UserPlatformInfo(Long dbId) {
        this.dbId = dbId;
    }
}
