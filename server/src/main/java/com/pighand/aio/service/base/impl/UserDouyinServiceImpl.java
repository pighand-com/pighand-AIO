package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.UserDouyinDomain;
import com.pighand.aio.mapper.base.UserDouyinMapper;
import com.pighand.aio.service.base.UserDouyinService;
import com.pighand.aio.vo.base.UserDouyinVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.UserDouyinTableDef.USER_DOUYIN;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 用户 - 抖音
 *
 * @author wangshuli
 * @createDate 2024-06-05 23:58:27
 */
@Service
public class UserDouyinServiceImpl extends BaseServiceImpl<UserDouyinMapper, UserDouyinDomain>
    implements UserDouyinService {

    /**
     * 创建
     *
     * @param userDouyinVO
     * @return
     */
    @Override
    public UserDouyinVO create(UserDouyinVO userDouyinVO) {
        super.mapper.insert(userDouyinVO);

        return userDouyinVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public UserDouyinDomain find(Long id) {
        return super.mapper.find(id, USER.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param userDouyinVO
     * @return PageOrList<UserDouyinVO>
     */
    @Override
    public PageOrList<UserDouyinVO> query(UserDouyinVO userDouyinVO) {
        userDouyinVO.setJoinTables(USER.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(USER_DOUYIN.OPENID.like(userDouyinVO.getOpenid(), VerifyUtils::isNotEmpty));
        queryWrapper.and(USER_DOUYIN.UNIONID.like(userDouyinVO.getUnionid(), VerifyUtils::isNotEmpty));
        queryWrapper.and(USER_DOUYIN.ANONYMOUS_OPENID.like(userDouyinVO.getAnonymousOpenid(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(USER_DOUYIN.APPLICATION_ID.eq(userDouyinVO.getApplicationId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(USER_DOUYIN.USER_ID.eq(userDouyinVO.getUserId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(USER_DOUYIN.SOURCE_PLATFORM.eq(userDouyinVO.getSourcePlatform(), VerifyUtils::isNotEmpty));
        queryWrapper.and(USER_DOUYIN.STATUS.eq(userDouyinVO.getStatus(), VerifyUtils::isNotEmpty));

        return super.mapper.query(userDouyinVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param userDouyinVO
     */
    @Override
    public void update(UserDouyinVO userDouyinVO) {
        UpdateChain updateChain = this.updateChain().where(USER_DOUYIN.ID.eq(userDouyinVO.getId()));
        boolean needUpdate = false;

        if (VerifyUtils.isNotEmpty(userDouyinVO.getId())) {
            needUpdate = true;
            updateChain.set(USER_DOUYIN.ID, userDouyinVO.getId());
        }

        if (needUpdate) {
            updateChain.update();
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
