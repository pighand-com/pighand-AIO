package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsFavouriteDomain;
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

import static com.pighand.aio.domain.CMS.table.AssetsDocTableDef.ASSETS_DOC;
import static com.pighand.aio.domain.CMS.table.AssetsFavouriteTableDef.ASSETS_FAVOURITE;
import static com.pighand.aio.domain.CMS.table.AssetsImageTableDef.ASSETS_IMAGE;
import static com.pighand.aio.domain.CMS.table.AssetsVideoTableDef.ASSETS_VIDEO;

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

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ASSETS_FAVOURITE.ID.eq(id));

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
     * 分页或列表查询
     *
     * @param assetsFavouriteDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<AssetsFavouriteVO> query(AssetsFavouriteDomain assetsFavouriteDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(assetsFavouriteDomain.getJoinTables(), queryWrapper);

        PageOrList<AssetsFavouriteVO> result =
            this.page(assetsFavouriteDomain, finalQueryWrapper, AssetsFavouriteVO.class);
        this.relationMany(assetsFavouriteDomain.getJoinTables(), result.getRecords());

        return result;
    }

    /**
     * 查询收藏列表，包含关联的素材详细信息
     *
     * @param assetsFavouriteDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<AssetsFavouriteVO> queryWithAssetDetails(AssetsFavouriteDomain assetsFavouriteDomain,
        QueryWrapper queryWrapper) {
        
        // 获取素材类型过滤条件
        Integer assetsType = assetsFavouriteDomain.getAssetsType();
        
        // 构建图片素材查询
        QueryWrapper imageQuery = QueryWrapper.create()
            .select(ASSETS_FAVOURITE.ID, ASSETS_FAVOURITE.ASSETS_TYPE, ASSETS_FAVOURITE.ASSETS_ID,
                ASSETS_FAVOURITE.CREATED_BY, ASSETS_FAVOURITE.CREATED_AT, ASSETS_IMAGE.TITLE, ASSETS_IMAGE.DESCRIPTION,
                ASSETS_IMAGE.URL, ASSETS_IMAGE.URL.as("coverUrl"), // 图片素材没有单独的coverUrl，使用url
                ASSETS_IMAGE.FILE_FORMAT, ASSETS_IMAGE.FILE_SIZE, ASSETS_IMAGE.VIEW_COUNT, ASSETS_IMAGE.DOWNLOAD_COUNT,
                ASSETS_IMAGE.HANDPICK, ASSETS_IMAGE.STATUS).from(ASSETS_FAVOURITE).join(ASSETS_IMAGE)
            .on(ASSETS_FAVOURITE.ASSETS_ID.eq(ASSETS_IMAGE.ID)).where(ASSETS_FAVOURITE.ASSETS_TYPE.eq(10));

        // 构建视频素材查询
        QueryWrapper videoQuery = QueryWrapper.create()
            .select(ASSETS_FAVOURITE.ID, ASSETS_FAVOURITE.ASSETS_TYPE, ASSETS_FAVOURITE.ASSETS_ID,
                ASSETS_FAVOURITE.CREATED_BY, ASSETS_FAVOURITE.CREATED_AT, ASSETS_VIDEO.TITLE, ASSETS_VIDEO.DESCRIPTION,
                ASSETS_VIDEO.URL, ASSETS_VIDEO.COVER_URL, ASSETS_VIDEO.FILE_FORMAT, ASSETS_VIDEO.FILE_SIZE,
                ASSETS_VIDEO.VIEW_COUNT, ASSETS_VIDEO.DOWNLOAD_COUNT, ASSETS_VIDEO.HANDPICK, ASSETS_VIDEO.STATUS)
            .from(ASSETS_FAVOURITE).join(ASSETS_VIDEO).on(ASSETS_FAVOURITE.ASSETS_ID.eq(ASSETS_VIDEO.ID))
            .where(ASSETS_FAVOURITE.ASSETS_TYPE.eq(20));

        // 构建文档素材查询
        QueryWrapper docQuery = QueryWrapper.create()
            .select(ASSETS_FAVOURITE.ID, ASSETS_FAVOURITE.ASSETS_TYPE, ASSETS_FAVOURITE.ASSETS_ID,
                ASSETS_FAVOURITE.CREATED_BY, ASSETS_FAVOURITE.CREATED_AT, ASSETS_DOC.TITLE, ASSETS_DOC.DESCRIPTION,
                ASSETS_DOC.URL, ASSETS_DOC.COVER_URL, ASSETS_DOC.FILE_FORMAT, ASSETS_DOC.FILE_SIZE,
                ASSETS_DOC.VIEW_COUNT, ASSETS_DOC.DOWNLOAD_COUNT, ASSETS_DOC.HANDPICK, ASSETS_DOC.STATUS)
            .from(ASSETS_FAVOURITE).join(ASSETS_DOC).on(ASSETS_FAVOURITE.ASSETS_ID.eq(ASSETS_DOC.ID))
            .where(ASSETS_FAVOURITE.ASSETS_TYPE.eq(30));

        // 应用额外的查询条件到每个子查询
        if (assetsFavouriteDomain.getCreatedBy() != null) {
            imageQuery.and(ASSETS_FAVOURITE.CREATED_BY.eq(assetsFavouriteDomain.getCreatedBy()));
            videoQuery.and(ASSETS_FAVOURITE.CREATED_BY.eq(assetsFavouriteDomain.getCreatedBy()));
            docQuery.and(ASSETS_FAVOURITE.CREATED_BY.eq(assetsFavouriteDomain.getCreatedBy()));
        }

        // 根据assetsType参数构建联合查询
        QueryWrapper unionQuery;
        if (assetsType != null) {
            // 如果指定了素材类型，只查询对应类型的素材
            switch (assetsType) {
                case 10: // 图片
                    unionQuery = imageQuery;
                    break;
                case 20: // 视频
                    unionQuery = videoQuery;
                    break;
                case 30: // 文档
                    unionQuery = docQuery;
                    break;
                default:
                    // 如果是未知类型，返回空结果
                    unionQuery = QueryWrapper.create().where("1 = 0");
                    break;
            }
        } else {
            // 如果没有指定素材类型，查询所有类型的素材
            unionQuery = imageQuery.union(videoQuery).union(docQuery);
        }
        
        unionQuery.orderBy(ASSETS_FAVOURITE.CREATED_AT.desc());

        return this.page(assetsFavouriteDomain, unionQuery, AssetsFavouriteVO.class);
    }
}
