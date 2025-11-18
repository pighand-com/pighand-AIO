package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserWechatDomain;
import com.pighand.aio.vo.base.UserWechatVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 用户微信信息表 同一个用户，不同来源，可能存在多个身份，使用unionid关联。所以本表与用户多对一
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface UserWechatService extends BaseService<UserWechatDomain> {

    /**
     * 创建
     *
     * @param userWechatVO
     * @return
     */
    UserWechatVO create(UserWechatVO userWechatVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserWechatDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param userWechatVO
     * @return PageOrList<UserWechatVO>
     */
    PageOrList<UserWechatVO> query(UserWechatVO userWechatVO);

    /**
     * 修改
     *
     * @param userWechatVO
     */
    void update(UserWechatVO userWechatVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    void deleteByUserId(Long userId);
}
