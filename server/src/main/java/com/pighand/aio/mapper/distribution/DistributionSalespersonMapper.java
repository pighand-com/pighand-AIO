package com.pighand.aio.mapper.distribution;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
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
import static com.pighand.aio.domain.distribution.table.DistributionSalespersonTableDef.DISTRIBUTION_SALESPERSON;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Mapper
public interface DistributionSalespersonMapper extends BaseMapper<DistributionSalespersonDomain> {

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

        if (joinTables.contains(USER.getName())) {
            queryWrapper.select(USER.PHONE).leftJoin(USER).on(USER.ID.eq(DISTRIBUTION_SALESPERSON.USER_ID));

            joinTables.remove(USER.getName());
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

        List<Function<DistributionSalespersonVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<DistributionSalespersonVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((DistributionSalespersonVO)result, mainIdGetters, subTableQueriesSingle,
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
    default DistributionSalespersonVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(DISTRIBUTION_SALESPERSON.ID.eq(id));

        DistributionSalespersonVO result = this.selectOneByQueryAs(queryWrapper, DistributionSalespersonVO.class);
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
    default DistributionSalespersonVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        DistributionSalespersonVO result = this.selectOneByQueryAs(finalQueryWrapper, DistributionSalespersonVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param distDistributionSalespersonDomain
     * @return
     */
    default PageOrList<DistributionSalespersonVO> query(DistributionSalespersonDomain distDistributionSalespersonDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper =
            this.relationOne(distDistributionSalespersonDomain.getJoinTables(), queryWrapper);

        PageOrList<DistributionSalespersonVO> result =
            this.page(distDistributionSalespersonDomain, finalQueryWrapper, DistributionSalespersonVO.class);
        this.relationMany(distDistributionSalespersonDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
