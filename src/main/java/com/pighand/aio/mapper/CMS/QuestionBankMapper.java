package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.QuestionBankDomain;
import com.pighand.aio.vo.CMS.QuestionBankVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.CMS.table.QuestionBankTableDef.QUESTION_BANK;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper
public interface QuestionBankMapper extends BaseMapper<QuestionBankDomain> {

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

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<QuestionBankVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasQuestionSet = joinTables.contains(QUESTION_SET.getTableName());

        int length = 0;

        if (hasQuestionSet) {
            length++;
        }

        Consumer<FieldQueryBuilder<QuestionBankVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasQuestionSet) {
            Consumer<FieldQueryBuilder<QuestionBankVO>> consumer = (builder) -> {
                builder.field(QuestionBankVO::getQuestionSet).queryWrapper(
                    questionBank -> QueryWrapper.create().from(QUESTION_SET)
                        .where(QUESTION_SET.QUESTION_BANK_ID.eq(questionBank.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default QuestionBankVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(QUESTION_BANK.ID.eq(id));
        Consumer<FieldQueryBuilder<QuestionBankVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, QuestionBankVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default QuestionBankVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<QuestionBankVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, QuestionBankVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param questionBankDomain
     * @return
     */
    default PageOrList<QuestionBankVO> query(QuestionBankDomain questionBankDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(questionBankDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<QuestionBankVO>>[] relationManyBuilders =
            this.relationMany(questionBankDomain.getJoinTables());

        return this.page(questionBankDomain, finalQueryWrapper, QuestionBankVO.class, relationManyBuilders);
    }
}
