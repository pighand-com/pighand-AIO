package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ProjectPlatformPayDomain;
import com.pighand.aio.vo.base.ProjectPlatformPayVO;
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

import static com.pighand.aio.domain.base.table.ProjectPlatformPayTableDef.PROJECT_PLATFORM_PAY;
import static com.pighand.aio.domain.base.table.ProjectTableDef.PROJECT;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ProjectPlatformPayMapper extends BaseMapper<ProjectPlatformPayDomain> {

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

        // PROJECT
        if (joinTables.contains(PROJECT.getTableName())) {
            queryWrapper.leftJoin(PROJECT).on(PROJECT.ID.eq(PROJECT_PLATFORM_PAY.ID));

            joinTables.remove(PROJECT.getTableName());
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

        List<Function<ProjectPlatformPayVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ProjectPlatformPayVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((ProjectPlatformPayVO)result, mainIdGetters, subTableQueriesSingle,
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
    default ProjectPlatformPayVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(PROJECT_PLATFORM_PAY.ID.eq(id));

        ProjectPlatformPayVO result = this.selectOneByQueryAs(queryWrapper, ProjectPlatformPayVO.class);
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
    default ProjectPlatformPayVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ProjectPlatformPayVO result = this.selectOneByQueryAs(finalQueryWrapper, ProjectPlatformPayVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param projectPlatformPayDomain
     * @return
     */
    default PageOrList<ProjectPlatformPayVO> query(ProjectPlatformPayDomain projectPlatformPayDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(projectPlatformPayDomain.getJoinTables(), queryWrapper);

        PageOrList<ProjectPlatformPayVO> result =
            this.page(projectPlatformPayDomain, finalQueryWrapper, ProjectPlatformPayVO.class);
        this.relationMany(projectPlatformPayDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
