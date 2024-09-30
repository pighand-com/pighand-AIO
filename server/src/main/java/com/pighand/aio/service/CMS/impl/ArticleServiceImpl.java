package com.pighand.aio.service.CMS.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.domain.CMS.ArticleDomain;
import com.pighand.aio.mapper.CMS.ArticleMapper;
import com.pighand.aio.service.CMS.ArticleCategoryRelevanceService;
import com.pighand.aio.service.CMS.ArticleCategoryService;
import com.pighand.aio.service.CMS.ArticleService;
import com.pighand.aio.vo.CMS.ArticleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.pighand.aio.domain.CMS.table.ArticleCategoryRelevanceTableDef.ARTICLE_CATEGORY_RELEVANCE;
import static com.pighand.aio.domain.CMS.table.ArticleCategoryTableDef.ARTICLE_CATEGORY;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, ArticleDomain> implements ArticleService {

    private final ArticleCategoryService articleCategoryService;
    private final ArticleCategoryRelevanceService articleCategoryRelevanceService;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 创建
     *
     * @param articleVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleVO create(ArticleVO articleVO) {
        articleVO.setCreatedAt(new Date());
        articleVO.setCreatorId(Context.getLoginUser().getId());
        articleVO.setDeleted(false);
        articleVO.setDownloadCount(0);
        articleVO.setViewCount(0);
        super.mapper.insert(articleVO);

        // 插入分类
        if (articleVO.getCategories() != null) {
            List<ArticleCategoryRelevanceDomain> articleCategoryRelevanceDomains =
                articleVO.getCategories().stream().map(category -> {
                    ArticleCategoryRelevanceDomain articleCategoryRelevanceDomain =
                        new ArticleCategoryRelevanceDomain();
                    articleCategoryRelevanceDomain.setArticleId(articleVO.getId());

                    // 将list category转为字符串，以逗号分隔
                    String articleCategoryPath = "";
                    for (String item : category) {
                        articleCategoryPath += item + ",";
                    }
                    articleCategoryRelevanceDomain.setArticleCategoryPath(articleCategoryPath);
                    return articleCategoryRelevanceDomain;
                }).toList();
            articleCategoryRelevanceService.saveBatch(articleCategoryRelevanceDomains);
        }

        return articleVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ArticleVO find(Long id) {
        ArticleVO articleVO = super.mapper.find(id, ARTICLE_CATEGORY_RELEVANCE.getTableName());
        List<List<String>> categories = new ArrayList<>();
        Set<String> categorySet = new HashSet<>();
        articleVO.getArticleCategoryRelevance().forEach(relevance -> {
            List<String> category = Arrays.stream(relevance.getArticleCategoryPath().split(",")).toList().stream()
                .filter(item -> item != "").toList();

            categorySet.addAll(category);
            categories.add(category);
        });

        articleVO.setCategories(categories);

        List<ArticleCategoryDomain> category =
            articleCategoryService.queryChain().where(ARTICLE_CATEGORY.ID.in(categorySet)).list();
        Map<String, String> categoryMap = new HashMap<>();
        category.forEach(item -> {
            categoryMap.put(item.getId().toString(), item.getName());
        });

        List<String> categoryNames = new ArrayList<>();
        articleVO.getArticleCategoryRelevance().forEach(relevance -> {
            List<String> categoryNameItem = new ArrayList<>();
            Arrays.stream(relevance.getArticleCategoryPath().split(",")).toList().forEach(item -> {
                if (item != "") {
                    categoryNameItem.add(categoryMap.get(item));
                }
            });

            categoryNames.add(String.join(" - ", categoryNameItem));
        });
        articleVO.setCategoryNames(categoryNames);

        return articleVO;
    }

    /**
     * 统计
     *
     * @param id
     * @param type
     */
    @Override
    public void statistics(Long id, String type) {
        if (type.equals("download")) {
            // 增加下载量
            this.updateChain().setRaw(ARTICLE.DOWNLOAD_COUNT, "download_count + 1").where(ARTICLE.ID.eq(id)).update();
        } else if (type.equals("view")) {
            // 增加浏览量
            this.updateChain().setRaw(ARTICLE.VIEW_COUNT, "view_count + 1").where(ARTICLE.ID.eq(id)).update();
        }
    }

    /**
     * 分页或列表
     *
     * @param articleVO
     * @return PageOrList<ArticleVO>
     */
    @Override
    public PageOrList<ArticleVO> query(ArticleVO articleVO) {
        articleVO.setJoinTables(ARTICLE_CATEGORY_RELEVANCE.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(ARTICLE.BANNER_URL.like(articleVO.getBannerUrl(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ARTICLE.TITLE.like(articleVO.getTitle(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ARTICLE.FILE_URL.like(articleVO.getFileUrl(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ARTICLE.DESCRIPTION.like(articleVO.getDescription(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(ARTICLE.TYPE.eq(articleVO.getType(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ARTICLE.STATUS.eq(articleVO.getStatus(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ARTICLE.CREATOR_ID.eq(articleVO.getCreatorId(), VerifyUtils::isNotEmpty));

        if (articleVO.getCategoryId() != null) {
            queryWrapper.and("FIND_IN_SET(?, article_category_relevance.article_category_path) > 0",
                articleVO.getCategoryId());
            //            queryWrapper.and(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_CATEGORY_PATH.like(articleVO.getCategoryId() + ","));
        }

        if (articleVO.getCreatedAtRange() != null) {
            queryWrapper.and(ARTICLE.CREATED_AT.between(articleVO.getCreatedAtRange().get(0) + " 00:00:00",
                articleVO.getCreatedAtRange().get(1) + " 23:59:59"));
        }

        queryWrapper.orderBy(ARTICLE.ID.desc());

        PageOrList<ArticleVO> result = super.mapper.query(articleVO, queryWrapper);

        Set<String> categorySet = new HashSet<>();
        result.getRecords().forEach(article -> {
            if (article.getArticleCategoryRelevance() == null) {
                return;
            }

            article.getArticleCategoryRelevance().forEach(relevance -> {
                Arrays.stream(relevance.getArticleCategoryPath().split(",")).toList().forEach(item -> {
                    if (item != "") {
                        categorySet.add(item);
                    }
                });
            });
        });

        List<ArticleCategoryDomain> category =
            articleCategoryService.queryChain().where(ARTICLE_CATEGORY.ID.in(categorySet)).list();
        Map<String, String> categoryMap = new HashMap<>();
        category.forEach(item -> {
            categoryMap.put(item.getId().toString(), item.getName());
        });

        result.getRecords().forEach(article -> {
            List<String> categoryNames = new ArrayList<>();

            if (article.getArticleCategoryRelevance() != null) {
                article.getArticleCategoryRelevance().forEach(relevance -> {
                    List<String> categoryNameItem = new ArrayList<>();
                    Arrays.stream(relevance.getArticleCategoryPath().split(",")).toList().forEach(item -> {
                        if (item != "") {
                            categoryNameItem.add(categoryMap.get(item));
                        }
                    });

                    categoryNames.add(String.join(" - ", categoryNameItem));
                });
            }

            article.setCategoryNames(categoryNames);
        });

        return result;
    }

    /**
     * 修改
     *
     * @param articleVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ArticleVO articleVO) {
        if (VerifyUtils.isNotEmpty(articleVO.getTitle()) || VerifyUtils.isNotEmpty(articleVO.getBannerUrl())
            || VerifyUtils.isNotEmpty(articleVO.getFileUrl()) || VerifyUtils.isNotEmpty(articleVO.getDescription())
            || VerifyUtils.isNotEmpty(articleVO.getType()) || VerifyUtils.isNotEmpty(articleVO.getStatus())
            || VerifyUtils.isNotEmpty(articleVO.getCreatorId())) {
            super.mapper.update(articleVO);
        }

        if (articleVO.getCategories() != null) {
            List<ArticleCategoryRelevanceDomain> articleCategoryRelevanceDomains =
                articleVO.getCategories().stream().map(category -> {
                    ArticleCategoryRelevanceDomain articleCategoryRelevanceDomain =
                        new ArticleCategoryRelevanceDomain();
                    articleCategoryRelevanceDomain.setArticleId(articleVO.getId());

                    // 将list category转为字符串，以逗号分隔
                    String articleCategoryPath = "";
                    for (String item : category) {
                        articleCategoryPath += item + ",";
                    }
                    articleCategoryRelevanceDomain.setArticleCategoryPath(articleCategoryPath);
                    return articleCategoryRelevanceDomain;
                }).toList();

            articleCategoryRelevanceService.remove(
                QueryWrapper.create().where(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID.eq(articleVO.getId())));
            articleCategoryRelevanceService.saveBatch(articleCategoryRelevanceDomains);
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        ArticleVO articleVO = super.mapper.find(id);

        // 删除文章，删除图片
        this.removeFile(articleVO.getFileUrl());
        this.removeFile(articleVO.getBannerUrl());

        super.mapper.deleteById(id);
    }

    /**
     * 删除文件
     *
     * @param fileUrl
     */
    private void removeFile(String fileUrl) {
        if (VerifyUtils.isEmpty(fileUrl)) {
            return;
        }

        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);

            File file = new File(uploadPath + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
