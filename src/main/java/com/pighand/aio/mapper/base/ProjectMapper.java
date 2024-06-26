package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ProjectDomain;
import com.pighand.aio.domain.base.ProjectPlatformKeyDomain;
import com.pighand.aio.vo.base.ProjectPlatformKeyVO;
import com.pighand.aio.vo.base.ProjectVO;
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

import static com.pighand.aio.domain.base.table.ProjectAuthorizationTableDef.PROJECT_AUTHORIZATION;
import static com.pighand.aio.domain.base.table.ProjectDefaultTableDef.PROJECT_DEFAULT;
import static com.pighand.aio.domain.base.table.ProjectPlatformKeyTableDef.PROJECT_PLATFORM_KEY;
import static com.pighand.aio.domain.base.table.ProjectPlatformPayTableDef.PROJECT_PLATFORM_PAY;
import static com.pighand.aio.domain.base.table.ProjectTableDef.PROJECT;

/**
 * 项目 - 基本信息
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectDomain> {

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

        // PROJECT_DEFAULT
        if (joinTables.contains(PROJECT_DEFAULT.getTableName())) {
            queryWrapper.leftJoin(PROJECT_DEFAULT).on(PROJECT_DEFAULT.ID.eq(PROJECT.ID));

            joinTables.remove(PROJECT_DEFAULT.getTableName());
        }

        // PROJECT_AUTHORIZATION
        if (joinTables.contains(PROJECT_AUTHORIZATION.getTableName())) {
            queryWrapper.leftJoin(PROJECT_AUTHORIZATION).on(PROJECT_AUTHORIZATION.ID.eq(PROJECT.ID));

            joinTables.remove(PROJECT_AUTHORIZATION.getTableName());
        }

        // PROJECT_PLATFORM_PAY
        if (joinTables.contains(PROJECT_PLATFORM_PAY.getTableName())) {
            queryWrapper.leftJoin(PROJECT_PLATFORM_PAY).on(PROJECT_PLATFORM_PAY.ID.eq(PROJECT.ID));

            joinTables.remove(PROJECT_PLATFORM_PAY.getTableName());
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

        List<Function<ProjectVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ProjectVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // PROJECT_PLATFORM_KEY
        if (joinTables.contains(PROJECT_PLATFORM_KEY.getTableName())) {
            mainIdGetters.add(ProjectVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new ProjectPlatformKeyDomain().select(PROJECT_PLATFORM_KEY.DEFAULT_COLUMNS)
                        .where(PROJECT_PLATFORM_KEY.PROJECT_ID.in(ids)).listAs(ProjectPlatformKeyVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new ProjectPlatformKeyDomain().select(PROJECT_PLATFORM_KEY.DEFAULT_COLUMNS)
                        .where(PROJECT_PLATFORM_KEY.PROJECT_ID.eq(id)).listAs(ProjectPlatformKeyVO.class));
            }

            subTableIdGetter.add(obj -> ((ProjectPlatformKeyVO)obj).getProjectId());
            subResultSetter.add((vo, list) -> vo.setProjectPlatformKey((List<ProjectPlatformKeyVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((ProjectVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default ProjectVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(PROJECT.ID.eq(id));

        ProjectVO result = this.selectOneByQueryAs(queryWrapper, ProjectVO.class);
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
    default ProjectVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ProjectVO result = this.selectOneByQueryAs(finalQueryWrapper, ProjectVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param projectDomain
     * @return
     */
    default PageOrList<ProjectVO> query(ProjectDomain projectDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(projectDomain.getJoinTables(), queryWrapper);

        PageOrList<ProjectVO> result = this.page(projectDomain, finalQueryWrapper, ProjectVO.class);
        this.relationMany(projectDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
