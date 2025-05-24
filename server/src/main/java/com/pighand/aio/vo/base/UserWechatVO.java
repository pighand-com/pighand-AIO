package com.pighand.aio.vo.base;

import com.pighand.aio.domain.base.UserWechatDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户微信信息表 同一个用户，不同来源，可能存在多个身份，使用unionid关联。所以本表与用户多对一
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserWechatVO extends UserWechatDomain {
}
