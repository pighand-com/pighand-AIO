package com.pighand.aio.service.ECommerce;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.ECommerce.StoreDomain;
import com.pighand.aio.vo.ECommerce.StoreVO;

/**
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface StoreService extends BaseService<StoreDomain> {

    /**
     * 创建
     *
     * @param storeVO
     * @return
     */
    StoreVO create(StoreVO storeVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    StoreDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param storeVO
     * @return PageOrList<StoreVO>
     */
    PageOrList<StoreVO> query(StoreVO storeVO);

    /**
     * 修改
     *
     * @param storeVO
     */
    void update(StoreVO storeVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
