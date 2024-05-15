package com.pighand.aio.mapper.common;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.common.SimpleSchedulingDomain;
import com.pighand.aio.vo.common.SimpleSchedulingVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.common.table.SimpleSchedulingTableDef.SIMPLE_SCHEDULING;

/**
 * 简单定时器
 *
 * @author wangshuli
 * @createDate 2024-04-16 11:44:49
 */
@Mapper
public interface SimpleSchedulingMapper extends BaseMapper<SimpleSchedulingDomain> {

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
    default Consumer<FieldQueryBuilder<SimpleSchedulingVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<SimpleSchedulingVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default SimpleSchedulingVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(SIMPLE_SCHEDULING.ID.eq(id));
        Consumer<FieldQueryBuilder<SimpleSchedulingVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, SimpleSchedulingVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default SimpleSchedulingVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<SimpleSchedulingVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, SimpleSchedulingVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param simpleSchedulingDomain
     * @return
     */
    default PageOrList<SimpleSchedulingVO> query(SimpleSchedulingDomain simpleSchedulingDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(simpleSchedulingDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<SimpleSchedulingVO>>[] relationManyBuilders =
            this.relationMany(simpleSchedulingDomain.getJoinTables());

        return this.page(simpleSchedulingDomain, finalQueryWrapper, SimpleSchedulingVO.class, relationManyBuilders);
    }
}
