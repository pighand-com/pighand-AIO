package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.vo.CMS.QuestionSetVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.CMS.table.QuestionAnswerTableDef.QUESTION_ANSWER;
import static com.pighand.aio.domain.CMS.table.QuestionBankTableDef.QUESTION_BANK;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper
public interface QuestionSetMapper extends BaseMapper<QuestionSetDomain> {

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

        if (joinTables.contains(QUESTION_BANK.getTableName())) {
            queryWrapper.leftJoin(QUESTION_BANK).on(QUESTION_BANK.ID.eq(QUESTION_SET.QUESTION_BANK_ID));
        }

        if (joinTables.contains(QUESTION_ANSWER.getTableName())) {
            queryWrapper.leftJoin(QUESTION_ANSWER).on(QUESTION_ANSWER.QUESTION_SET_ID.eq(QUESTION_SET.ID));
        }

        if (joinTables.contains(QUESTION_BANK.getTableName())) {
            queryWrapper.leftJoin(QUESTION_BANK).on(QUESTION_BANK.ID.eq(QUESTION_SET.QUESTION_BANK_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<QuestionSetVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<QuestionSetVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default QuestionSetVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(QUESTION_SET.ID.eq(id));
        Consumer<FieldQueryBuilder<QuestionSetVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, QuestionSetVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default QuestionSetVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<QuestionSetVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, QuestionSetVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param questionSetDomain
     * @return
     */
    default PageOrList<QuestionSetVO> query(QuestionSetDomain questionSetDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(questionSetDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<QuestionSetVO>>[] relationManyBuilders =
            this.relationMany(questionSetDomain.getJoinTables());

        return this.page(questionSetDomain, finalQueryWrapper, QuestionSetVO.class, relationManyBuilders);
    }
}
