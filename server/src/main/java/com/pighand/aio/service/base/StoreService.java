package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.vo.base.StoreVO;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
public interface StoreService extends BaseService<StoreDomain> {

    /**
     * 创建
     *
     * @param baseStoreVO
     * @return
     */
    StoreVO create(StoreVO baseStoreVO);

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
     * @param baseStoreVO
     * @return PageOrList<BaseStoreVO>
     */
    PageOrList<StoreVO> query(StoreVO baseStoreVO);

    /**
     * 修改
     *
     * @param baseStoreVO
     */
    void update(StoreVO baseStoreVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
