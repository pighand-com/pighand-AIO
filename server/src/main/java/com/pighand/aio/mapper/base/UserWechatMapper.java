package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.UserWechatDomain;
import com.pighand.aio.vo.base.UserWechatVO;
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

import static com.pighand.aio.domain.base.table.UserTableDef.USER;
import static com.pighand.aio.domain.base.table.UserWechatTableDef.USER_WECHAT;

/**
 * 用户 - 微信
 * 同一个用户，不同来源，可能存在多个身份，使用unionid关联。所以本表与用户多对一
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface UserWechatMapper extends BaseMapper<UserWechatDomain> {

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

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(USER_WECHAT.USER_ID));

            joinTables.remove(USER.getTableName());
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

        List<Function<UserWechatVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<UserWechatVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((UserWechatVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default UserWechatVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(USER_WECHAT.ID.eq(id));

        UserWechatVO result = this.selectOneByQueryAs(queryWrapper, UserWechatVO.class);
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
    default UserWechatVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        UserWechatVO result = this.selectOneByQueryAs(finalQueryWrapper, UserWechatVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param userWechatDomain
     * @return
     */
    default PageOrList<UserWechatVO> query(UserWechatDomain userWechatDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(userWechatDomain.getJoinTables(), queryWrapper);

        PageOrList<UserWechatVO> result = this.page(userWechatDomain, finalQueryWrapper, UserWechatVO.class);
        this.relationMany(userWechatDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
