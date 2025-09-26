package com.pighand.aio.service.base.impl;

import com.pighand.aio.domain.base.ApplicationDefaultDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.mapper.base.UserExtensionMapper;
import com.pighand.aio.service.base.ApplicationDefaultService;
import com.pighand.aio.service.base.UserExtensionService;
import com.pighand.aio.service.base.UserWechatService;
import com.pighand.aio.vo.base.ApplicationDefaultVO;
import com.pighand.aio.vo.base.UserExtensionVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户扩展信息。id与user表相同
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
public class UserExtensionServiceImpl extends BaseServiceImpl<UserExtensionMapper, UserExtensionDomain>
    implements UserExtensionService {

    @Autowired
    private ApplicationDefaultService projectDefaultService;

    @Autowired
    private UserWechatService userWechatService;

    /**
     * 对多关联查询
     *
     * @param userExtensions
     * @param joinTables
     */
    private void joinMany(List<UserExtensionVO> userExtensions, Set<String> joinTables) {
        //        List<Long> ids = userExtensions.stream().map(UserExtensionVO::getId).toList();
        //
        //        if (joinTables.contains("wechatUser")) {
        //            LambdaQueryWrapper<UserWechatDomain> query = new LambdaQueryWrapper<>();
        //            query.in(UserWechatDomain::getUserId, ids);
        //            List<UserWechatDomain> list = userWechatService.list(query);
        //
        //            BeanUtil.appendChildren(
        //                    userExtensions,
        //                    list,
        //                    UserExtensionVO::getId,
        //                    UserWechatDomain::getUserId,
        //                    (userExtension, userWechats) -> {
        //                        if (userWechats != null) {
        //                            userExtension.setUserWechats(userWechats);
        //                        }
        //                    });
        //        }
    }

    /**
     * 对多关联查询
     *
     * @param userExtension
     * @param joinTables
     */
    private void joinMany(UserExtensionVO userExtension, Set<String> joinTables) {
        joinMany(List.of(userExtension), joinTables);
    }

    /**
     * 创建
     *
     * <p>优先传入参数，如果没有使用默认信息({@link ApplicationDefaultDomain})
     *
     * @param userExtension
     * @return
     */
    @Override
    public UserExtensionDomain create(UserExtensionDomain userExtension) {
        if (userExtension.getRegisterAt() == null) {
            userExtension.setRegisterAt(new Date());
        }

        if (userExtension.getProfile() == null || userExtension.getNickname() == null) {
            ApplicationDefaultVO projectDefault = projectDefaultService.find(userExtension.getApplicationId());

            userExtension.setProfile(projectDefault.getRandomProfile());
            userExtension.setNickname(projectDefault.getDefaultNickname());
        }

        super.mapper.insert(userExtension);

        return userExtension;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public UserExtensionDomain find(Long id) {
        UserExtensionDomain userExtensionDomain = super.mapper.find(id);
        return userExtensionDomain;
    }

    /**
     * 分页或列表
     *
     * @param userExtensionVO
     */
    @Override
    public PageOrList<UserExtensionVO> query(UserExtensionVO userExtensionVO) {
        //        PageOrList pageInfo = userExtensionVO.pageParamOrInit(PageType.NEXT_TOKEN);
        //        userExtensionVO.setJoinTables(List.of("user", "wechatUser"));
        //        var list = super.mapper.join(pageInfo, userExtensionVO);
        //
        //        joinMany(list.getRecords(), userExtensionVO.getJoinTables());
        //
        //        return list;
        //        return super.mapper.query(pageInfo, userExtensionVO);
        return null;
    }

    /**
     * 修改
     *
     * @param userExtensionVO
     */
    @Override
    public void update(UserExtensionVO userExtensionVO) {
        //        UserExtensionDomain userExtensionDomain = super.mapper.selectById(userExtensionVO.getId());
        //
        //        if (userExtensionDomain == null) {
        //            throw new RuntimeException("用户不存在");
        //        }
        //
        //        if (!userExtensionDomain.equals(userExtensionVO)) {
        //            super.mapper.updateById(userExtensionVO);
        //        }
    }

    /**
     * 修改三方平台信息
     *
     * @param userExtensionVO
     */
    @Override
    public void updateTripartitePlatformInfo(UserExtensionVO userExtensionVO) {
        //        UserExtensionDomain userExtensionDomain = super.mapper.selectById(userExtensionVO.getId());
        //
        //        if (userExtensionDomain == null) {
        //            throw new RuntimeException("用户不存在");
        //        }
        //
        //        boolean isUpdateProfile = StringUtils.hasText(userExtensionVO.getProfile()) && !userExtensionDomain.getProfile()
        //            .equals(userExtensionVO.getProfile());
        //        boolean isUpdateNickname =
        //            StringUtils.hasText(userExtensionVO.getNickname()) && !userExtensionDomain.getNickname()
        //                .equals(userExtensionVO.getNickname());
        //        boolean isUpdateGender =
        //            userExtensionVO.getGender() != null && !userExtensionDomain.getGender().equals(userExtensionVO.getGender());
        //
        //        boolean needUpdate =
        //            !userExtensionDomain.getInitialInfo() && (isUpdateProfile || isUpdateNickname || isUpdateGender);
        //        if (needUpdate) {
        //            super.mapper.updateById(userExtensionVO);
        //        }
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
