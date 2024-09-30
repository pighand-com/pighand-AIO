package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.vo.base.UserExtensionVO;
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

import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 用户 - 扩展信息
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface UserExtensionMapper extends BaseMapper<UserExtensionDomain> {

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
            queryWrapper.leftJoin(USER).on(USER.ID.eq(USER_EXTENSION.ID));

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

        List<Function<UserExtensionVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<UserExtensionVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((UserExtensionVO)result, mainIdGetters, subTableQueriesSingle,
                subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default UserExtensionVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(USER_EXTENSION.ID.eq(id));

        UserExtensionVO result = this.selectOneByQueryAs(queryWrapper, UserExtensionVO.class);
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
    default UserExtensionVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        UserExtensionVO result = this.selectOneByQueryAs(finalQueryWrapper, UserExtensionVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param userExtensionDomain
     * @return
     */
    default PageOrList<UserExtensionVO> query(UserExtensionDomain userExtensionDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(userExtensionDomain.getJoinTables(), queryWrapper);

        PageOrList<UserExtensionVO> result = this.page(userExtensionDomain, finalQueryWrapper, UserExtensionVO.class);
        this.relationMany(userExtensionDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
