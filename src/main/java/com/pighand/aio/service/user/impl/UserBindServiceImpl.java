package com.pighand.aio.service.user.impl;

import com.pighand.aio.domain.user.UserBindDomain;
import com.pighand.aio.mapper.user.UserBindMapper;
import com.pighand.aio.service.user.UserBindService;
import com.pighand.aio.vo.user.UserBindVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pighand.aio.domain.user.table.UserTableDef.USER;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class UserBindServiceImpl extends BaseServiceImpl<UserBindMapper, UserBindDomain> implements UserBindService {

    /**
     * 创建
     *
     * @param userBindVO
     * @return
     */
    @Override
    public UserBindVO create(UserBindVO userBindVO) {
        super.mapper.insert(userBindVO);

        return userBindVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public UserBindDomain find(Long id) {
        List<String> joinTables = List.of("user", "user");

        return super.mapper.find(id, joinTables);
    }

    /**
     * 分页或列表
     *
     * @param userBindVO
     * @return PageOrList<UserBindVO>
     */
    @Override
    public PageOrList<UserBindVO> query(UserBindVO userBindVO) {
        userBindVO.setJoinTables(List.of(USER.getTableName()));

        return super.mapper.query(userBindVO);
    }

    /**
     * 修改
     *
     * @param userBindVO
     */
    @Override
    public void update(UserBindVO userBindVO) {
        super.mapper.update(userBindVO);
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
