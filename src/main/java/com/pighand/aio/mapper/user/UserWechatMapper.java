package com.pighand.aio.mapper.user;

import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.aio.domain.user.UserWechatDomain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户微信信息表
 * 同一个用户，不同来源，可能存在多个身份，使用unionid关联。所以本表与用户多对一
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface UserWechatMapper extends BaseMapper<UserWechatDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param userWechatVO
     * @return
     */
}
