package com.pighand.aio.mapper.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.vo.MKT.LotteryParticipatePrizeVO;
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

import static com.pighand.aio.domain.MKT.table.LotteryParticipatePrizeTableDef.LOTTERY_PARTICIPATE_PRIZE;

/**
 * 营销 - 抽奖 - 参与类型 - 奖品
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Mapper
public interface LotteryParticipatePrizeMapper extends BaseMapper<LotteryParticipatePrizeDomain> {

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

        List<Function<LotteryParticipatePrizeVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<LotteryParticipatePrizeVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((LotteryParticipatePrizeVO)result, mainIdGetters, subTableQueriesSingle,
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
    default LotteryParticipatePrizeVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(LOTTERY_PARTICIPATE_PRIZE.ID.eq(id));

        LotteryParticipatePrizeVO result = this.selectOneByQueryAs(queryWrapper, LotteryParticipatePrizeVO.class);
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
    default LotteryParticipatePrizeVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        LotteryParticipatePrizeVO result = this.selectOneByQueryAs(finalQueryWrapper, LotteryParticipatePrizeVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param mktOtteryParticipatePrizeDomain
     * @return
     */
    default PageOrList<LotteryParticipatePrizeVO> query(LotteryParticipatePrizeDomain mktOtteryParticipatePrizeDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper =
            this.relationOne(mktOtteryParticipatePrizeDomain.getJoinTables(), queryWrapper);

        PageOrList<LotteryParticipatePrizeVO> result =
            this.page(mktOtteryParticipatePrizeDomain, finalQueryWrapper, LotteryParticipatePrizeVO.class);
        this.relationMany(mktOtteryParticipatePrizeDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
