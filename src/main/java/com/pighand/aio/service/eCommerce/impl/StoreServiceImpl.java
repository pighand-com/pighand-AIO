package com.pighand.aio.service.eCommerce.impl;

import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.eCommerce.StoreDomain;
import com.pighand.aio.domain.eCommerce.table.CouponTableDef;
import com.pighand.aio.mapper.eCommerce.StoreMapper;
import com.pighand.aio.service.eCommerce.StoreService;
import com.pighand.aio.vo.eCommerce.StoreVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper, StoreDomain> implements StoreService {

    /**
     * 创建
     *
     * @param storeVO
     * @return
     */
    @Override
    public StoreVO create(StoreVO storeVO) {
        super.mapper.insert(storeVO);

        return storeVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public StoreDomain find(Long id) {
        List<String> joinTables = List.of("coupon");

        return super.mapper.find(id, joinTables);
    }

    /**
     * 分页或列表
     *
     * @param storeVO
     * @return PageOrList<StoreVO>
     */
    @Override
    public PageOrList<StoreVO> query(StoreVO storeVO) {
        storeVO.setJoinTables(List.of(CouponTableDef.COUPON.getTableName()));

        return super.mapper.query(storeVO);
    }

    /**
     * 修改
     *
     * @param storeVO
     */
    @Override
    public void update(StoreVO storeVO) {
        super.mapper.update(storeVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
