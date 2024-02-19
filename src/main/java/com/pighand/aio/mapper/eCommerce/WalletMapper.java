package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.eCommerce.WalletDomain;
import com.pighand.aio.domain.eCommerce.table.WalletTableDef;
import com.pighand.aio.vo.eCommerce.WalletVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface WalletMapper extends BaseMapper<WalletDomain> {

    WalletTableDef WALLET = WalletTableDef.WALLET;

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        QueryWrapper queryWrapper = QueryWrapper.create();

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default WalletVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(WALLET.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, WalletVO.class);
    }

    /**
     * 分页或列表
     *
     * @param walletDomain
     * @return
     */
    default PageOrList<WalletVO> query(WalletDomain walletDomain) {
        QueryWrapper queryWrapper = this.baseQuery(walletDomain.getJoinTables());

        return this.page(walletDomain, queryWrapper, WalletVO.class);
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
