package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsDownloadDomain;
import com.pighand.aio.vo.CMS.AssetsDownloadVO;
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
import static com.pighand.aio.domain.CMS.table.AssetsDownloadTableDef.ASSETS_DOWNLOAD;
import static com.pighand.aio.domain.CMS.table.AssetsImageTableDef.ASSETS_IMAGE;
import static com.pighand.aio.domain.CMS.table.AssetsVideoTableDef.ASSETS_VIDEO;

/**
 * CMS - 素材 - 下载记录
 *
 * @author wangshuli
 * @createDate 2025-01-25 10:00:00
 */
@Mapper
public interface AssetsDownloadMapper extends BaseMapper<AssetsDownloadDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        // 这里可以添加关联查询逻辑，如果需要的话
        // 目前下载记录表相对简单，暂不需要复杂关联

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @param joinTables
     * @param result
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<Function<AssetsDownloadVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<AssetsDownloadVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((AssetsDownloadVO)result, mainIdGetters, subTableQueriesSingle,
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
    default AssetsDownloadVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ASSETS_DOWNLOAD.ID.eq(id));

        AssetsDownloadVO result = this.selectOneByQueryAs(queryWrapper, AssetsDownloadVO.class);
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
    default AssetsDownloadVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        AssetsDownloadVO result = this.selectOneByQueryAs(finalQueryWrapper, AssetsDownloadVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表查询
     *
     * @param assetsDownloadDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<AssetsDownloadVO> query(AssetsDownloadDomain assetsDownloadDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(assetsDownloadDomain.getJoinTables(), queryWrapper);

        PageOrList<AssetsDownloadVO> result =
            this.page(assetsDownloadDomain, finalQueryWrapper, AssetsDownloadVO.class);
        this.relationMany(assetsDownloadDomain.getJoinTables(), result.getRecords());

        return result;
    }

    /**
     * 查询下载列表，包含关联的素材详细信息
     *
     * @param assetsDownloadDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<AssetsDownloadVO> queryWithAssetDetails(AssetsDownloadDomain assetsDownloadDomain,
        QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }


        
        // 构建图片素材查询
        QueryWrapper imageQuery = QueryWrapper.create()
            .select(ASSETS_DOWNLOAD.ID, ASSETS_DOWNLOAD.ASSETS_TYPE, ASSETS_DOWNLOAD.ASSETS_ID,
                ASSETS_DOWNLOAD.CREATED_BY, ASSETS_DOWNLOAD.CREATED_AT, ASSETS_DOWNLOAD.UPDATED_AT, ASSETS_IMAGE.TITLE,
                ASSETS_IMAGE.DESCRIPTION, ASSETS_IMAGE.URL, ASSETS_IMAGE.URL.as("coverUrl"), // 图片素材没有单独的coverUrl，使用url
                ASSETS_IMAGE.FILE_FORMAT, ASSETS_IMAGE.FILE_SIZE, ASSETS_IMAGE.VIEW_COUNT, ASSETS_IMAGE.DOWNLOAD_COUNT,
                ASSETS_IMAGE.HANDPICK, ASSETS_IMAGE.STATUS).from(ASSETS_DOWNLOAD).join(ASSETS_IMAGE)
            .on(ASSETS_DOWNLOAD.ASSETS_ID.eq(ASSETS_IMAGE.ID)).where(ASSETS_DOWNLOAD.ASSETS_TYPE.eq(10));

        // 构建视频素材查询
        QueryWrapper videoQuery = QueryWrapper.create()
            .select(ASSETS_DOWNLOAD.ID, ASSETS_DOWNLOAD.ASSETS_TYPE, ASSETS_DOWNLOAD.ASSETS_ID,
                ASSETS_DOWNLOAD.CREATED_BY, ASSETS_DOWNLOAD.CREATED_AT, ASSETS_DOWNLOAD.UPDATED_AT, ASSETS_VIDEO.TITLE,
                ASSETS_VIDEO.DESCRIPTION, ASSETS_VIDEO.URL, ASSETS_VIDEO.COVER_URL, ASSETS_VIDEO.FILE_FORMAT,
                ASSETS_VIDEO.FILE_SIZE, ASSETS_VIDEO.VIEW_COUNT, ASSETS_VIDEO.DOWNLOAD_COUNT, ASSETS_VIDEO.HANDPICK,
                ASSETS_VIDEO.STATUS).from(ASSETS_DOWNLOAD).join(ASSETS_VIDEO)
            .on(ASSETS_DOWNLOAD.ASSETS_ID.eq(ASSETS_VIDEO.ID)).where(ASSETS_DOWNLOAD.ASSETS_TYPE.eq(20));

        // 构建文档素材查询
        QueryWrapper docQuery = QueryWrapper.create()
            .select(ASSETS_DOWNLOAD.ID, ASSETS_DOWNLOAD.ASSETS_TYPE, ASSETS_DOWNLOAD.ASSETS_ID,
                ASSETS_DOWNLOAD.CREATED_BY, ASSETS_DOWNLOAD.CREATED_AT, ASSETS_DOWNLOAD.UPDATED_AT, ASSETS_DOC.TITLE,
                ASSETS_DOC.DESCRIPTION, ASSETS_DOC.URL, ASSETS_DOC.COVER_URL, ASSETS_DOC.FILE_FORMAT,
                ASSETS_DOC.FILE_SIZE, ASSETS_DOC.VIEW_COUNT, ASSETS_DOC.DOWNLOAD_COUNT, ASSETS_DOC.HANDPICK,
                ASSETS_DOC.STATUS).from(ASSETS_DOWNLOAD).join(ASSETS_DOC)
            .on(ASSETS_DOWNLOAD.ASSETS_ID.eq(ASSETS_DOC.ID)).where(ASSETS_DOWNLOAD.ASSETS_TYPE.eq(30));

        // 应用额外的查询条件到每个子查询
        if (assetsDownloadDomain.getCreatedBy() != null) {
            imageQuery.and(ASSETS_DOWNLOAD.CREATED_BY.eq(assetsDownloadDomain.getCreatedBy()));
            videoQuery.and(ASSETS_DOWNLOAD.CREATED_BY.eq(assetsDownloadDomain.getCreatedBy()));
            docQuery.and(ASSETS_DOWNLOAD.CREATED_BY.eq(assetsDownloadDomain.getCreatedBy()));
        }

        // 根据assetsType参数决定查询哪些类型的素材
        QueryWrapper unionQuery = null;
        if (assetsDownloadDomain.getAssetsType() != null) {
            // 如果指定了assetsType，只查询对应类型的素材
            switch (assetsDownloadDomain.getAssetsType()) {
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
                    // 未知类型，返回空结果
                    unionQuery = QueryWrapper.create().where("1 = 0");
                    break;
            }
        } else {
            // 如果没有指定assetsType，查询所有类型的素材
            unionQuery = imageQuery.union(videoQuery).union(docQuery);
        }
        
        unionQuery.orderBy(ASSETS_DOWNLOAD.UPDATED_AT.desc());

        return this.page(assetsDownloadDomain, unionQuery, AssetsDownloadVO.class);
    }
}