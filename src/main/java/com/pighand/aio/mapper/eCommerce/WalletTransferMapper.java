package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.WalletTransferDomain;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
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

import static com.pighand.aio.domain.ECommerce.table.WalletBillTableDef.WALLET_BILL;
import static com.pighand.aio.domain.ECommerce.table.WalletTransferTableDef.WALLET_TRANSFER;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface WalletTransferMapper extends BaseMapper<WalletTransferDomain> {

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

        // WALLET_BILL
        if (joinTables.contains(WALLET_BILL.getTableName())) {
            queryWrapper.leftJoin(WALLET_BILL).on(WALLET_BILL.WALLET_TRANSFER_ID.eq(WALLET_TRANSFER.ID));

            joinTables.remove(WALLET_BILL.getTableName());
        }

        // FROM_USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(WALLET_TRANSFER.FROM_USER_ID));

            joinTables.remove(USER.getTableName());
        }

        // TO_USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(WALLET_TRANSFER.TO_USER_ID));

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

        List<Function<WalletTransferVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<WalletTransferVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((WalletTransferVO)result, mainIdGetters, subTableQueriesSingle,
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
    default WalletTransferVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(WALLET_TRANSFER.ID.eq(id));

        WalletTransferVO result = this.selectOneByQueryAs(queryWrapper, WalletTransferVO.class);
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
    default WalletTransferVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        WalletTransferVO result = this.selectOneByQueryAs(finalQueryWrapper, WalletTransferVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param walletTransferDomain
     * @return
     */
    default PageOrList<WalletTransferVO> query(WalletTransferDomain walletTransferDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(walletTransferDomain.getJoinTables(), queryWrapper);

        PageOrList<WalletTransferVO> result =
            this.page(walletTransferDomain, finalQueryWrapper, WalletTransferVO.class);
        this.relationMany(walletTransferDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
