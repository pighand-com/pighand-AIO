package com.pighand.aio.vo.user.tripartite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 三方平台用户信息
 *
 * @author wangshuli
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripartitePlatformUserInfo {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String profile;

    /**
     * 性别
     */
    private Integer gender;
}
