package com.pighand.aio.mapper.project;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.project.ProjectPlatformPayDomain;
import com.pighand.aio.vo.project.ProjectPlatformPayVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.project.table.ProjectPlatformPayTableDef.PROJECT_PLATFORM_PAY;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper
public interface ProjectPlatformPayMapper extends BaseMapper<ProjectPlatformPayDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(List<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null) {
            return queryWrapper;
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<ProjectPlatformPayVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<ProjectPlatformPayVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default ProjectPlatformPayVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(PROJECT_PLATFORM_PAY.ID.eq(id));
        Consumer<FieldQueryBuilder<ProjectPlatformPayVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, ProjectPlatformPayVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default ProjectPlatformPayVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<ProjectPlatformPayVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, ProjectPlatformPayVO.class, relationManyBuilders);
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
        Consumer<FieldQueryBuilder<ProjectPlatformPayVO>>[] relationManyBuilders =
            this.relationMany(projectPlatformPayDomain.getJoinTables());

        return this.page(projectPlatformPayDomain, finalQueryWrapper, ProjectPlatformPayVO.class, relationManyBuilders);
    }
}
