package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.CouponUserTransferDomain;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;
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

import static com.pighand.aio.domain.ECommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.ECommerce.table.CouponUserTransferTableDef.COUPON_USER_TRANSFER;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 优惠券 - 转赠记录
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface CouponUserTransferMapper extends BaseMapper<CouponUserTransferDomain> {

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

        // COUPON_USER
        if (joinTables.contains(COUPON_USER.getTableName())) {
            queryWrapper.leftJoin(COUPON_USER).on(COUPON_USER.ID.eq(COUPON_USER_TRANSFER.COUPON_USER_ID));

            joinTables.remove(COUPON_USER.getTableName());
        }

        // FROM_USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(COUPON_USER_TRANSFER.FROM_USER_ID));

            joinTables.remove(USER.getTableName());
        }

        // TO_USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(COUPON_USER_TRANSFER.TO_USER_ID));

            joinTables.remove(USER.getTableName());
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

        List<Function<CouponUserTransferVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<CouponUserTransferVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((CouponUserTransferVO)result, mainIdGetters, subTableQueriesSingle,
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
    default CouponUserTransferVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(COUPON_USER_TRANSFER.ID.eq(id));

        CouponUserTransferVO result = this.selectOneByQueryAs(queryWrapper, CouponUserTransferVO.class);
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
    default CouponUserTransferVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        CouponUserTransferVO result = this.selectOneByQueryAs(finalQueryWrapper, CouponUserTransferVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param couponUserTransferDomain
     * @return
     */
    default PageOrList<CouponUserTransferVO> query(CouponUserTransferDomain couponUserTransferDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(couponUserTransferDomain.getJoinTables(), queryWrapper);

        PageOrList<CouponUserTransferVO> result =
            this.page(couponUserTransferDomain, finalQueryWrapper, CouponUserTransferVO.class);
        this.relationMany(couponUserTransferDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
