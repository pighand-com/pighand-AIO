package com.pighand.aio.mapper.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.aio.domain.MKT.LotteryParticipateDomain;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;
import com.pighand.framework.spring.page.PageOrList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pighand.aio.table.MktLotteryParticipateTableDef.MKT_LOTTERY_PARTICIPATE;

/**
 * 营销 - 抽奖 - 参与类型
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Mapper
public interface LotteryParticipateMapper extends BaseMapper<LotteryParticipateDomain> {

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

        List<Function<LotteryParticipateVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<LotteryParticipateVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((LotteryParticipateVO)result, mainIdGetters, subTableQueriesSingle,
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
    default LotteryParticipateVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(MKT_LOTTERY_PARTICIPATE.ID.eq(id));

        LotteryParticipateVO result = this.selectOneByQueryAs(queryWrapper, LotteryParticipateVO.class);
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
    default LotteryParticipateVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        LotteryParticipateVO result = this.selectOneByQueryAs(finalQueryWrapper, LotteryParticipateVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param mktLotteryParticipateDomain
     * @return
     */
    default PageOrList<LotteryParticipateVO> query(LotteryParticipateDomain mktLotteryParticipateDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(mktLotteryParticipateDomain.getJoinTables(), queryWrapper);

        PageOrList<LotteryParticipateVO> result =
            this.page(mktLotteryParticipateDomain, finalQueryWrapper, LotteryParticipateVO.class);
        this.relationMany(mktLotteryParticipateDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
