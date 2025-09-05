package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsFavouriteDomain;
import com.pighand.aio.table.CmsAssetsFavouriteTableDef.CMS_ASSETS_FAVOURITE;
import com.pighand.aio.vo.CMS.AssetsFavouriteVO;
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
 * CMS - 素材 - 收藏
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper
public interface AssetsFavouriteMapper extends BaseMapper<AssetsFavouriteDomain> {

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

        List<Function<AssetsFavouriteVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<AssetsFavouriteVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((AssetsFavouriteVO)result, mainIdGetters, subTableQueriesSingle,
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
    default AssetsFavouriteVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(CMS_ASSETS_FAVOURITE.ID.eq(id));

        AssetsFavouriteVO result = this.selectOneByQueryAs(queryWrapper, AssetsFavouriteVO.class);
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
    default AssetsFavouriteVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        AssetsFavouriteVO result = this.selectOneByQueryAs(finalQueryWrapper, AssetsFavouriteVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsFavouriteDomain
     * @return
     */
    default PageOrList<AssetsFavouriteVO> query(AssetsFavouriteDomain cmsAssetsFavouriteDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(cmsAssetsFavouriteDomain.getJoinTables(), queryWrapper);

        PageOrList<AssetsFavouriteVO> result =
            this.page(cmsAssetsFavouriteDomain, finalQueryWrapper, AssetsFavouriteVO.class);
        this.relationMany(cmsAssetsFavouriteDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
