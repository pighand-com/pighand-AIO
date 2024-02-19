package com.pighand.aio.vo.user.tripartite;

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
    private Long dbId;
    /**
     * 系统用户id
     */
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
