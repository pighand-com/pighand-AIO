package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.vo.CMS.QuestionAnswerVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.CMS.table.QuestionAnswerTableDef.QUESTION_ANSWER;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper
public interface QuestionAnswerMapper extends BaseMapper<QuestionAnswerDomain> {

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

        if (joinTables.contains(QUESTION_SET.getTableName())) {
            queryWrapper.leftJoin(QUESTION_SET).on(QUESTION_SET.ID.eq(QUESTION_ANSWER.QUESTION_SET_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<QuestionAnswerVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<QuestionAnswerVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default QuestionAnswerVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(QUESTION_ANSWER.ID.eq(id));
        Consumer<FieldQueryBuilder<QuestionAnswerVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, QuestionAnswerVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default QuestionAnswerVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<QuestionAnswerVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, QuestionAnswerVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param questionAnswerDomain
     * @return
     */
    default PageOrList<QuestionAnswerVO> query(QuestionAnswerDomain questionAnswerDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(questionAnswerDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<QuestionAnswerVO>>[] relationManyBuilders =
            this.relationMany(questionAnswerDomain.getJoinTables());

        return this.page(questionAnswerDomain, finalQueryWrapper, QuestionAnswerVO.class, relationManyBuilders);
    }
}
