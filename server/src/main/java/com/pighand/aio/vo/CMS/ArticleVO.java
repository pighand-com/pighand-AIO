package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.ArticleDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Data
@TableRef(ArticleDomain.class)
@EqualsAndHashCode(callSuper = false)
public class ArticleVO extends ArticleDomain {

    // relation table: begin
    private ArticleCategoryVO articleCategory;

    private List<ArticleCategoryRelevanceVO> articleCategoryRelevance;
    // relation table: end

    // 分类id
    private Long categoryId;

    private List<List<String>> categories;

    private List<String> categoryNames;

    // 创建时间范围
    private List<String> createdAtRange;

    private String statisticsType;
}
