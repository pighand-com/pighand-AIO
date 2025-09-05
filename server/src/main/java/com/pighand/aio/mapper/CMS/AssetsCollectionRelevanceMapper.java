package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsCollectionRelevanceDomain;
import com.pighand.aio.table.CmsAssetsCollectionRelevanceTableDef.CMS_ASSETS_COLLECTION_RELEVANCE;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
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

/**
 * CMS - 素材 - 专辑关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper
public interface AssetsCollectionRelevanceMapper extends BaseMapper<AssetsCollectionRelevanceDomain> {

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

        List<Function<AssetsCollectionRelevanceVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<AssetsCollectionRelevanceVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((AssetsCollectionRelevanceVO)result, mainIdGetters, subTableQueriesSingle,
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
    default AssetsCollectionRelevanceVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper =
            this.relationOne(joinTableSet, null).where(CMS_ASSETS_COLLECTION_RELEVANCE.ID.eq(id));

        AssetsCollectionRelevanceVO result = this.selectOneByQueryAs(queryWrapper, AssetsCollectionRelevanceVO.class);
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
    default AssetsCollectionRelevanceVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        AssetsCollectionRelevanceVO result =
            this.selectOneByQueryAs(finalQueryWrapper, AssetsCollectionRelevanceVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsCollectionRelevanceDomain
     * @return
     */
    default PageOrList<AssetsCollectionRelevanceVO> query(
        AssetsCollectionRelevanceDomain cmsAssetsCollectionRelevanceDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper =
            this.relationOne(cmsAssetsCollectionRelevanceDomain.getJoinTables(), queryWrapper);

        PageOrList<AssetsCollectionRelevanceVO> result =
            this.page(cmsAssetsCollectionRelevanceDomain, finalQueryWrapper, AssetsCollectionRelevanceVO.class);
        this.relationMany(cmsAssetsCollectionRelevanceDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
