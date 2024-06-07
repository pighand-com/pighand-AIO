package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pighand.aio.domain.ECommerce.table.WalletTableDef.WALLET;
import static com.pighand.aio.domain.base.table.UserDouyinTableDef.USER_DOUYIN;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;
import static com.pighand.aio.domain.base.table.UserWechatTableDef.USER_WECHAT;

/**
 * 用户 - 关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null || joinTables.isEmpty()) {
            return queryWrapper;
        }

        // USER_EXTENSION
        if (joinTables.contains(USER_EXTENSION.getTableName())) {
            queryWrapper.leftJoin(USER_EXTENSION).on(USER_EXTENSION.ID.eq(USER.ID));

            joinTables.remove(USER_EXTENSION.getTableName());
        }

        // USER_DOUYIN
        if (joinTables.contains(USER_DOUYIN.getTableName())) {
            queryWrapper.leftJoin(USER_DOUYIN).on(USER_DOUYIN.USER_ID.eq(USER.ID));

            joinTables.remove(USER_DOUYIN.getTableName());
        }

        // USER_WECHAT
        if (joinTables.contains(USER_WECHAT.getTableName())) {
            queryWrapper.leftJoin(USER_WECHAT).on(USER_WECHAT.USER_ID.eq(USER.ID));

            joinTables.remove(USER_WECHAT.getTableName());
        }

        // WALLET
        if (joinTables.contains(WALLET.getTableName())) {
            queryWrapper.leftJoin(WALLET).on(WALLET.USER_ID.eq(USER.ID));

            joinTables.remove(WALLET.getTableName());
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<Function<UserVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<UserVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((UserVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default UserVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(USER.ID.eq(id));

        UserVO result = this.selectOneByQueryAs(queryWrapper, UserVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default UserVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        UserVO result = this.selectOneByQueryAs(finalQueryWrapper, UserVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param userDomain
     * @return
     */
    default PageOrList<UserVO> query(UserDomain userDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(userDomain.getJoinTables(), queryWrapper);

        PageOrList<UserVO> result = this.page(userDomain, finalQueryWrapper, UserVO.class);
        this.relationMany(userDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
