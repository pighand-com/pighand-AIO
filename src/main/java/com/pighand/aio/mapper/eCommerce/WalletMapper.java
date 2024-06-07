package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.ECommerce.WalletDomain;
import com.pighand.aio.vo.ECommerce.WalletVO;
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

import static com.pighand.aio.domain.ECommerce.table.WalletTableDef.WALLET;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface WalletMapper extends BaseMapper<WalletDomain> {

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

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(WALLET.USER_ID));

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

        List<Function<WalletVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<WalletVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((WalletVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default WalletVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(WALLET.ID.eq(id));

        WalletVO result = this.selectOneByQueryAs(queryWrapper, WalletVO.class);
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
    default WalletVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        WalletVO result = this.selectOneByQueryAs(finalQueryWrapper, WalletVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param walletDomain
     * @return
     */
    default PageOrList<WalletVO> query(WalletDomain walletDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(walletDomain.getJoinTables(), queryWrapper);

        PageOrList<WalletVO> result = this.page(walletDomain, finalQueryWrapper, WalletVO.class);
        this.relationMany(walletDomain.getJoinTables(), result.getRecords());

        return result;
    }

    /**
     * 冻结
     */
    default void freeze() {
        UpdateChain.of(WalletDomain.class).setRaw(WALLET.FREEZE_TOKENS, WALLET.TOKENS.add(WALLET.FREEZE_TOKENS))
            .set(WALLET.TOKENS, 0).where(WALLET.TOKENS.gt(0)).update();
    }

    /**
     * 解冻
     *
     * @param userId
     */
    default void unfreeze(Long userId) {
        UpdateChain.of(WalletDomain.class).setRaw(WALLET.TOKENS, WALLET.FREEZE_TOKENS.add(WALLET.TOKENS))
            .set(WALLET.FREEZE_TOKENS, 0).where(WALLET.USER_ID.eq(userId)).update();
    }

}
