package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.vo.base.StoreVO;
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

import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;
import static com.pighand.aio.domain.base.table.StoreTableDef.STORE;
import static com.pighand.aio.domain.base.table.TenantTableDef.TENANT;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Mapper
public interface StoreMapper extends BaseMapper<StoreDomain> {

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

        if (joinTables.contains(USER_EXTENSION.getName())) {
            queryWrapper.select(USER_EXTENSION.ID, USER_EXTENSION.NAME);
            queryWrapper.leftJoin(USER_EXTENSION).on(STORE.CREATED_BY.eq(USER_EXTENSION.ID));

            joinTables.remove(USER_EXTENSION.getName());
        }

        if (joinTables.contains(APPLICATION.getName())) {
            queryWrapper.select(APPLICATION.ID, APPLICATION.NAME);
            queryWrapper.leftJoin(APPLICATION).on(APPLICATION.ID.eq(STORE.APPLICATION_ID));

            joinTables.remove(APPLICATION.getName());
        }

        if (joinTables.contains(TENANT.getName())) {
            queryWrapper.select(TENANT.ID, TENANT.NAME);
            queryWrapper.leftJoin(TENANT).on(TENANT.ID.eq(STORE.TENANT_ID));

            joinTables.remove(TENANT.getName());
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

        List<Function<StoreVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<StoreVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((StoreVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default StoreVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(STORE.ID.eq(id));

        StoreVO result = this.selectOneByQueryAs(queryWrapper, StoreVO.class);
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
    default StoreVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        StoreVO result = this.selectOneByQueryAs(finalQueryWrapper, StoreVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param baseStoreDomain
     * @return
     */
    default PageOrList<StoreVO> query(StoreDomain baseStoreDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(baseStoreDomain.getJoinTables(), queryWrapper);

        PageOrList<StoreVO> result = this.page(baseStoreDomain, finalQueryWrapper, StoreVO.class);
        this.relationMany(baseStoreDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
