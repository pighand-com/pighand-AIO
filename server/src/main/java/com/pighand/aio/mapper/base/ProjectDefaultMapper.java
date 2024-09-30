package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ProjectDefaultDomain;
import com.pighand.aio.vo.base.ProjectDefaultVO;
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

import static com.pighand.aio.domain.base.table.ProjectDefaultTableDef.PROJECT_DEFAULT;
import static com.pighand.aio.domain.base.table.ProjectTableDef.PROJECT;

/**
 * 项目 - 默认设置
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ProjectDefaultMapper extends BaseMapper<ProjectDefaultDomain> {

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
            queryWrapper.leftJoin(PROJECT).on(PROJECT.ID.eq(PROJECT_DEFAULT.ID));

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

        List<Function<ProjectDefaultVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ProjectDefaultVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((ProjectDefaultVO)result, mainIdGetters, subTableQueriesSingle,
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
    default ProjectDefaultVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(PROJECT_DEFAULT.ID.eq(id));

        ProjectDefaultVO result = this.selectOneByQueryAs(queryWrapper, ProjectDefaultVO.class);
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
    default ProjectDefaultVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ProjectDefaultVO result = this.selectOneByQueryAs(finalQueryWrapper, ProjectDefaultVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param projectDefaultDomain
     * @return
     */
    default PageOrList<ProjectDefaultVO> query(ProjectDefaultDomain projectDefaultDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(projectDefaultDomain.getJoinTables(), queryWrapper);

        PageOrList<ProjectDefaultVO> result =
            this.page(projectDefaultDomain, finalQueryWrapper, ProjectDefaultVO.class);
        this.relationMany(projectDefaultDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
