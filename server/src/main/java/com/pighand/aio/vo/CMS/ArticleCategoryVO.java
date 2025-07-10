package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Data
@TableRef(ArticleCategoryDomain.class)
@EqualsAndHashCode(callSuper = false)
public class ArticleCategoryVO extends ArticleCategoryDomain {

    // relation table: begin
    // TODO: 增加报错：先查文章再查分类，分类报错；先查分类再查文章，文章报错
    private List<ArticleVO> article;
    // relation table: end

    // 子元素数量
    private Integer subCount;

    private List<ArticleCategoryVO> children;
}
