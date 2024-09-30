package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.QuestionBankDomain;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.vo.CMS.QuestionBankVO;
import com.pighand.aio.vo.CMS.QuestionSetVO;
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

import static com.pighand.aio.domain.CMS.table.QuestionBankTableDef.QUESTION_BANK;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface QuestionBankMapper extends BaseMapper<QuestionBankDomain> {

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

        List<Function<QuestionBankVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<QuestionBankVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // QUESTION_SET
        if (joinTables.contains(QUESTION_SET.getTableName())) {
            mainIdGetters.add(QuestionBankVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new QuestionSetDomain().select(QUESTION_SET.DEFAULT_COLUMNS)
                    .where(QUESTION_SET.QUESTION_BANK_ID.in(ids)).listAs(QuestionSetVO.class));
            } else {
                subTableQueriesSingle.add(id -> new QuestionSetDomain().select(QUESTION_SET.DEFAULT_COLUMNS)
                    .where(QUESTION_SET.QUESTION_BANK_ID.eq(id)).listAs(QuestionSetVO.class));
            }

            subTableIdGetter.add(obj -> ((QuestionSetVO)obj).getQuestionBankId());
            subResultSetter.add((vo, list) -> vo.setQuestionSet((List<QuestionSetVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((QuestionBankVO)result, mainIdGetters, subTableQueriesSingle,
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
    default QuestionBankVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(QUESTION_BANK.ID.eq(id));

        QuestionBankVO result = this.selectOneByQueryAs(queryWrapper, QuestionBankVO.class);
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
    default QuestionBankVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        QuestionBankVO result = this.selectOneByQueryAs(finalQueryWrapper, QuestionBankVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param questionBankDomain
     * @return
     */
    default PageOrList<QuestionBankVO> query(QuestionBankDomain questionBankDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(questionBankDomain.getJoinTables(), queryWrapper);

        PageOrList<QuestionBankVO> result = this.page(questionBankDomain, finalQueryWrapper, QuestionBankVO.class);
        this.relationMany(questionBankDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
