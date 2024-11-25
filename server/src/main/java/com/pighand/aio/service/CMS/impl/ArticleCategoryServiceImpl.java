package com.pighand.aio.service.CMS.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.mapper.CMS.ArticleCategoryMapper;
import com.pighand.aio.service.CMS.ArticleCategoryService;
import com.pighand.aio.vo.CMS.ArticleCategoryVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.CMS.table.ArticleCategoryTableDef.ARTICLE_CATEGORY;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;

/**
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategoryMapper, ArticleCategoryDomain>
    implements ArticleCategoryService {

    /**
     * 创建
     *
     * @param articleCategoryVO
     * @return
     */
    @Override
    public ArticleCategoryVO create(ArticleCategoryVO articleCategoryVO) {
        super.mapper.insert(articleCategoryVO);

        return articleCategoryVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ArticleCategoryDomain find(Long id) {
        return super.mapper.find(id, ARTICLE.getTableName());
    }

    @Override
    public List<ArticleCategoryDomain> queryAll() {
        return super.mapper.selectListByQuery(new QueryWrapper());
    }

    /**
     * 分页或列表
     *
     * @param articleCategoryVO
     * @return PageOrList<ArticleCategoryVO>
     */
    @Override
    public PageOrList<ArticleCategoryVO> query(ArticleCategoryVO articleCategoryVO) {
        //        articleCategoryVO.setJoinTables(ARTICLE.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            // 默认查一级分类，即parentId为null
            .and(ARTICLE_CATEGORY.PARENT_ID.eq(articleCategoryVO.getParentId()))

            // like
            .and(ARTICLE_CATEGORY.NAME.like(articleCategoryVO.getName(), VerifyUtils::isNotEmpty));

        PageOrList<ArticleCategoryVO> result = super.mapper.query(articleCategoryVO, queryWrapper);

        // 统计子集数量
        List<Long> ids = result.getRecords().stream().map(ArticleCategoryVO::getId).toList();
        List<ArticleCategoryVO> children =
            this.queryChain().select(ARTICLE_CATEGORY.ID, ARTICLE_CATEGORY.PARENT_ID, ARTICLE_CATEGORY.NAME)
                .where(ARTICLE_CATEGORY.PARENT_ID.in(ids)).listAs(ArticleCategoryVO.class);

        Map<Long, List<ArticleCategoryVO>> childrenMap =
            children.stream().collect(Collectors.groupingBy(ArticleCategoryVO::getParentId));

        result.getRecords().forEach(item -> {
            item.setChildren(childrenMap.get(item.getId()));
        });

        return result;
    }

    /**
     * 修改
     *
     * @param articleCategoryVO
     */
    @Override
    public void update(ArticleCategoryVO articleCategoryVO) {
        super.mapper.update(articleCategoryVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
