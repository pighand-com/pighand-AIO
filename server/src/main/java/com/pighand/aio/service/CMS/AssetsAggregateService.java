package com.pighand.aio.service.CMS;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.mapper.CMS.AssetsDocMapper;
import com.pighand.aio.mapper.CMS.AssetsImageMapper;
import com.pighand.aio.mapper.CMS.AssetsVideoMapper;
import com.pighand.aio.vo.CMS.AssetsAggregateVO;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.CMS.table.AssetsDocTableDef.ASSETS_DOC;
import static com.pighand.aio.domain.CMS.table.AssetsImageTableDef.ASSETS_IMAGE;
import static com.pighand.aio.domain.CMS.table.AssetsVideoTableDef.ASSETS_VIDEO;
import static com.pighand.aio.domain.CMS.table.AssetsCollectionRelevanceTableDef.ASSETS_COLLECTION_RELEVANCE;

/**
 * CMS - 素材聚合查询服务
 * 用于统一查询文档、图片、视频三种素材类型的聚合结果
 *
 * @author wangshuli
 * @createDate 2025-01-21
 */
@Service
@RequiredArgsConstructor
public class AssetsAggregateService {

    private final AssetsDocMapper assetsDocMapper;

    private final AssetsImageMapper assetsImageMapper;

    private final AssetsVideoMapper assetsVideoMapper;

    /**
     * 聚合查询素材（文档、图片、视频）
     * 使用UNION ALL查询优化性能，支持数据库级别分页
     *
     * @param assetsAggregateVO 查询条件（包含分页信息）
     * @return 聚合查询结果
     */
    public PageOrList<AssetsAggregateVO> queryAggregate(AssetsAggregateVO assetsAggregateVO) {
        // 处理特殊classificationId逻辑
        List<String> assetTypes = processSpecialClassificationId(assetsAggregateVO);

        // 获取分页参数，设置默认值
        Long pageSize = assetsAggregateVO.getPageSize();
        Long pageNumber = assetsAggregateVO.getPageNumber();

        if (pageSize == null || pageSize <= 0) {
            pageSize = 10L; // 默认每页10条
        }
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1L; // 默认第1页
        }

        // 构建基础查询条件（用于复用）
        QueryWrapper baseDocQuery = buildDocBaseQuery(assetsAggregateVO, assetTypes);
        QueryWrapper baseImageQuery = buildImageBaseQuery(assetsAggregateVO, assetTypes);
        QueryWrapper baseVideoQuery = buildVideoBaseQuery(assetsAggregateVO, assetTypes);

        // 获取总数
        long totalCount = getTotalCount(assetsAggregateVO, assetTypes);

        // 构建分页查询
        QueryWrapper pagedQuery = buildPagedQuery(baseDocQuery, baseImageQuery, baseVideoQuery, pageNumber, pageSize, assetsAggregateVO);

        // 执行分页查询
        List<AssetsAggregateVO> pagedResults = new ArrayList<>();
        if (pagedQuery != null) {
            pagedResults = assetsDocMapper.selectListByQueryAs(pagedQuery, AssetsAggregateVO.class);
        }

        // 构建分页结果
        Page page = new Page();
        page.setPageSize(pageSize);
        page.setTotalRow(totalCount);
        page.setPageNumber(pageNumber);
        page.setRecords(pagedResults);
        PageOrList<AssetsAggregateVO> result = new PageOrList<>(page);
        return result;
    }

    /**
     * 处理特殊classificationId逻辑
     * 1-图片，2-视频，3-文档，其他按表中的classificationId搜索
     *
     * @param assetsAggregateVO 查询条件
     * @return 处理后的资产类型列表
     */
    private List<String> processSpecialClassificationId(AssetsAggregateVO assetsAggregateVO) {
                Long classificationId = assetsAggregateVO.getClassificationId();
        
        // 如果classificationId是特殊值，则转换为对应的资产类型，并清空classificationId
        List<String> assetTypes = null;
        if (classificationId == 1L) {
            // 1-图片
            assetTypes = List.of("image");
            assetsAggregateVO.setClassificationId(null);
        } else if (classificationId == 2L) {
            // 2-视频
            assetTypes = List.of("video");
            assetsAggregateVO.setClassificationId(null);
        } else if (classificationId == 3L) {
            // 3-文档
            assetTypes = List.of("doc");
            assetsAggregateVO.setClassificationId(null);
        } else {
            // 其他值保持原有的classificationId，按表中的classificationId搜索
        }
        
        return assetTypes;
    }

    /**
     * 构建文档查询的基础条件
     *
     * @param assetsAggregateVO 查询条件
     * @param assetTypes        资产类型列表
     * @return 文档查询条件，如果不需要查询文档则返回null
     */
    private QueryWrapper buildDocBaseQuery(AssetsAggregateVO assetsAggregateVO, List<String> assetTypes) {
        if (assetTypes != null && !assetTypes.isEmpty() && !assetTypes.contains("doc")) {
            return null;
        }

        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(ASSETS_DOC.ID, ASSETS_DOC.APPLICATION_ID, ASSETS_DOC.CLASSIFICATION_ID, ASSETS_DOC.TITLE,
                ASSETS_DOC.DESCRIPTION, ASSETS_DOC.COVER_URL, ASSETS_DOC.URL, ASSETS_DOC.FILE_FORMAT,
                ASSETS_DOC.FILE_SIZE, ASSETS_DOC.VIEW_COUNT, ASSETS_DOC.DOWNLOAD_COUNT, ASSETS_DOC.HANDPICK,
                ASSETS_DOC.STATUS, ASSETS_DOC.CREATED_AT, ASSETS_DOC.UPDATED_AT, ASSETS_DOC.CREATED_BY)
            .select("'doc' as asset_type").from(ASSETS_DOC);

        // 基础查询条件
        queryWrapper.where(ASSETS_DOC.APPLICATION_ID.eq(assetsAggregateVO.getApplicationId()))
            .and(assetsAggregateVO.getClassificationId() != null ?
                ASSETS_DOC.CLASSIFICATION_ID.eq(assetsAggregateVO.getClassificationId()) : null)
            .and(assetsAggregateVO.getOnlyHandpick() != null && assetsAggregateVO.getOnlyHandpick() ?
                ASSETS_DOC.HANDPICK.eq(true) : null)
            .and(assetsAggregateVO.getKeyword() != null && !assetsAggregateVO.getKeyword().trim().isEmpty() ?
                ASSETS_DOC.TITLE.like("%" + assetsAggregateVO.getKeyword().trim() + "%") : null);

        // 专辑ID查询条件 - 通过关系表关联
        if (assetsAggregateVO.getCollectionId() != null) {
            queryWrapper.and(ASSETS_DOC.ID.in(
                QueryWrapper.create()
                    .select(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID)
                    .from(ASSETS_COLLECTION_RELEVANCE)
                    .where(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(assetsAggregateVO.getCollectionId()))
                    .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(30)) // 30-文档
            ));
        }

        return queryWrapper;
    }

    /**
     * 构建图片查询的基础条件
     *
     * @param assetsAggregateVO 查询条件
     * @param assetTypes        资产类型列表
     * @return 图片查询条件，如果不需要查询图片则返回null
     */
    private QueryWrapper buildImageBaseQuery(AssetsAggregateVO assetsAggregateVO, List<String> assetTypes) {
        if (assetTypes != null && !assetTypes.isEmpty() && !assetTypes.contains("image")) {
            return null;
        }

        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(ASSETS_IMAGE.ID, ASSETS_IMAGE.APPLICATION_ID, ASSETS_IMAGE.CLASSIFICATION_ID, ASSETS_IMAGE.TITLE,
                ASSETS_IMAGE.DESCRIPTION, ASSETS_IMAGE.URL.as("cover_url"), ASSETS_IMAGE.URL, ASSETS_IMAGE.FILE_FORMAT,
                ASSETS_IMAGE.FILE_SIZE, ASSETS_IMAGE.VIEW_COUNT, ASSETS_IMAGE.DOWNLOAD_COUNT, ASSETS_IMAGE.HANDPICK,
                ASSETS_IMAGE.STATUS, ASSETS_IMAGE.CREATED_AT, ASSETS_IMAGE.UPDATED_AT, ASSETS_IMAGE.CREATED_BY)
            .select("'image' as asset_type").from(ASSETS_IMAGE);

        // 基础查询条件
        queryWrapper.where(ASSETS_IMAGE.APPLICATION_ID.eq(assetsAggregateVO.getApplicationId()))
            .and(assetsAggregateVO.getClassificationId() != null ?
                ASSETS_IMAGE.CLASSIFICATION_ID.eq(assetsAggregateVO.getClassificationId()) : null)
            .and(assetsAggregateVO.getOnlyHandpick() != null && assetsAggregateVO.getOnlyHandpick() ?
                ASSETS_IMAGE.HANDPICK.eq(true) : null)
            .and(assetsAggregateVO.getKeyword() != null && !assetsAggregateVO.getKeyword().trim().isEmpty() ?
                ASSETS_IMAGE.TITLE.like("%" + assetsAggregateVO.getKeyword().trim() + "%") : null);

        // 专辑ID查询条件 - 通过关系表关联
        if (assetsAggregateVO.getCollectionId() != null) {
            queryWrapper.and(ASSETS_IMAGE.ID.in(
                QueryWrapper.create()
                    .select(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID)
                    .from(ASSETS_COLLECTION_RELEVANCE)
                    .where(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(assetsAggregateVO.getCollectionId()))
                    .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(10)) // 10-图片
            ));
        }

        return queryWrapper;
    }

    /**
     * 构建视频查询的基础条件
     *
     * @param assetsAggregateVO 查询条件
     * @param assetTypes        资产类型列表
     * @return 视频查询条件，如果不需要查询视频则返回null
     */
    private QueryWrapper buildVideoBaseQuery(AssetsAggregateVO assetsAggregateVO, List<String> assetTypes) {
        if (assetTypes != null && !assetTypes.isEmpty() && !assetTypes.contains("video")) {
            return null;
        }

        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(ASSETS_VIDEO.ID, ASSETS_VIDEO.APPLICATION_ID, ASSETS_VIDEO.CLASSIFICATION_ID, ASSETS_VIDEO.TITLE,
                ASSETS_VIDEO.DESCRIPTION, ASSETS_VIDEO.COVER_URL, ASSETS_VIDEO.URL, ASSETS_VIDEO.FILE_FORMAT,
                ASSETS_VIDEO.FILE_SIZE, ASSETS_VIDEO.VIEW_COUNT, ASSETS_VIDEO.DOWNLOAD_COUNT, ASSETS_VIDEO.HANDPICK,
                ASSETS_VIDEO.STATUS, ASSETS_VIDEO.CREATED_AT, ASSETS_VIDEO.UPDATED_AT, ASSETS_VIDEO.CREATED_BY)
            .select("'video' as asset_type").from(ASSETS_VIDEO);

        // 基础查询条件
        queryWrapper.where(ASSETS_VIDEO.APPLICATION_ID.eq(assetsAggregateVO.getApplicationId()))
            .and(assetsAggregateVO.getClassificationId() != null ?
                ASSETS_VIDEO.CLASSIFICATION_ID.eq(assetsAggregateVO.getClassificationId()) : null)
            .and(assetsAggregateVO.getOnlyHandpick() != null && assetsAggregateVO.getOnlyHandpick() ?
                ASSETS_VIDEO.HANDPICK.eq(true) : null)
            .and(assetsAggregateVO.getKeyword() != null && !assetsAggregateVO.getKeyword().trim().isEmpty() ?
                ASSETS_VIDEO.TITLE.like("%" + assetsAggregateVO.getKeyword().trim() + "%") : null);

        // 专辑ID查询条件 - 通过关系表关联
        if (assetsAggregateVO.getCollectionId() != null) {
            queryWrapper.and(ASSETS_VIDEO.ID.in(
                QueryWrapper.create()
                    .select(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID)
                    .from(ASSETS_COLLECTION_RELEVANCE)
                    .where(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(assetsAggregateVO.getCollectionId()))
                    .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(20)) // 20-视频
            ));
        }

        return queryWrapper;
    }

    /**
     * 构建用于计算总数的查询
     *
     * @param assetsAggregateVO 查询条件
     * @param assetTypes        资产类型列表
     * @return 计算总数的查询
     */
    private long getTotalCount(AssetsAggregateVO assetsAggregateVO, List<String> assetTypes) {
        long totalCount = 0;

        // 分别计算每种类型的数量
        if (assetTypes == null || assetTypes.isEmpty() || assetTypes.contains("doc")) {
            QueryWrapper docCountQuery = QueryWrapper.create().from(ASSETS_DOC)
                .where(ASSETS_DOC.APPLICATION_ID.eq(assetsAggregateVO.getApplicationId()))
                .and(assetsAggregateVO.getClassificationId() != null ?
                    ASSETS_DOC.CLASSIFICATION_ID.eq(assetsAggregateVO.getClassificationId()) : null)
                .and(assetsAggregateVO.getOnlyHandpick() != null && assetsAggregateVO.getOnlyHandpick() ?
                    ASSETS_DOC.HANDPICK.eq(true) : null)
                .and(assetsAggregateVO.getKeyword() != null && !assetsAggregateVO.getKeyword().trim().isEmpty() ?
                    ASSETS_DOC.TITLE.like("%" + assetsAggregateVO.getKeyword().trim() + "%") : null);

            // 专辑ID查询条件
            if (assetsAggregateVO.getCollectionId() != null) {
                docCountQuery.and(ASSETS_DOC.ID.in(
                    QueryWrapper.create()
                        .select(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID)
                        .from(ASSETS_COLLECTION_RELEVANCE)
                        .where(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(assetsAggregateVO.getCollectionId()))
                        .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(30)) // 30-文档
                ));
            }

            totalCount += assetsDocMapper.selectCountByQuery(docCountQuery);
        }

        if (assetTypes == null || assetTypes.isEmpty() || assetTypes.contains("image")) {
            QueryWrapper imageCountQuery = QueryWrapper.create().from(ASSETS_IMAGE)
                .where(ASSETS_IMAGE.APPLICATION_ID.eq(assetsAggregateVO.getApplicationId()))
                .and(assetsAggregateVO.getClassificationId() != null ?
                    ASSETS_IMAGE.CLASSIFICATION_ID.eq(assetsAggregateVO.getClassificationId()) : null)
                .and(assetsAggregateVO.getOnlyHandpick() != null && assetsAggregateVO.getOnlyHandpick() ?
                    ASSETS_IMAGE.HANDPICK.eq(true) : null)
                .and(assetsAggregateVO.getKeyword() != null && !assetsAggregateVO.getKeyword().trim().isEmpty() ?
                    ASSETS_IMAGE.TITLE.like("%" + assetsAggregateVO.getKeyword().trim() + "%") : null);

            // 专辑ID查询条件
            if (assetsAggregateVO.getCollectionId() != null) {
                imageCountQuery.and(ASSETS_IMAGE.ID.in(
                    QueryWrapper.create()
                        .select(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID)
                        .from(ASSETS_COLLECTION_RELEVANCE)
                        .where(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(assetsAggregateVO.getCollectionId()))
                        .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(10)) // 10-图片
                ));
            }

            totalCount += assetsImageMapper.selectCountByQuery(imageCountQuery);
        }

        if (assetTypes == null || assetTypes.isEmpty() || assetTypes.contains("video")) {
            QueryWrapper videoCountQuery = QueryWrapper.create().from(ASSETS_VIDEO)
                .where(ASSETS_VIDEO.APPLICATION_ID.eq(assetsAggregateVO.getApplicationId()))
                .and(assetsAggregateVO.getClassificationId() != null ?
                    ASSETS_VIDEO.CLASSIFICATION_ID.eq(assetsAggregateVO.getClassificationId()) : null)
                .and(assetsAggregateVO.getOnlyHandpick() != null && assetsAggregateVO.getOnlyHandpick() ?
                    ASSETS_VIDEO.HANDPICK.eq(true) : null)
                .and(assetsAggregateVO.getKeyword() != null && !assetsAggregateVO.getKeyword().trim().isEmpty() ?
                    ASSETS_VIDEO.TITLE.like("%" + assetsAggregateVO.getKeyword().trim() + "%") : null);

            // 专辑ID查询条件
            if (assetsAggregateVO.getCollectionId() != null) {
                videoCountQuery.and(ASSETS_VIDEO.ID.in(
                    QueryWrapper.create()
                        .select(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID)
                        .from(ASSETS_COLLECTION_RELEVANCE)
                        .where(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(assetsAggregateVO.getCollectionId()))
                        .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(20)) // 20-视频
                ));
            }

            totalCount += assetsVideoMapper.selectCountByQuery(videoCountQuery);
        }

        return totalCount;
    }

    /**
     * 构建分页查询
     *
     * @param docQuery          文档查询条件
     * @param imageQuery        图片查询条件
     * @param videoQuery        视频查询条件
     * @param pageNumber        页码
     * @param pageSize          每页大小
     * @param assetsAggregateVO 查询条件（包含排序信息）
     * @return 分页查询
     */
    private QueryWrapper buildPagedQuery(QueryWrapper docQuery, QueryWrapper imageQuery, QueryWrapper videoQuery,
        Long pageNumber, Long pageSize, AssetsAggregateVO assetsAggregateVO) {
        // 使用UNION ALL合并查询
        QueryWrapper unionQuery = null;
        if (docQuery != null) {
            unionQuery = docQuery;
            if (imageQuery != null) {
                unionQuery = unionQuery.unionAll(imageQuery);
            }
            if (videoQuery != null) {
                unionQuery = unionQuery.unionAll(videoQuery);
            }
        } else if (imageQuery != null) {
            unionQuery = imageQuery;
            if (videoQuery != null) {
                unionQuery = unionQuery.unionAll(videoQuery);
            }
        } else if (videoQuery != null) {
            unionQuery = videoQuery;
        }

        if (unionQuery != null) {
            // 将UNION ALL查询作为子查询，在外层应用排序和分页
            QueryWrapper outerQuery = QueryWrapper.create()
                .select("*")
                .from(unionQuery).as("union_result");
            
            // 根据sort参数决定排序方式
            if ("download".equals(assetsAggregateVO.getSort())) {
                // 按下载量倒序排列，然后按id倒序
                outerQuery.orderBy("download_count DESC, id DESC");
            } else {
                // 默认按id倒序排列
                outerQuery.orderBy("id DESC");
            }
            
            // 添加分页
            long offset = (pageNumber - 1) * pageSize;
            outerQuery.limit(offset, pageSize);
            
            return outerQuery;
        }

        return unionQuery;
    }
}
