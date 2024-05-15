package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.BillDomain;
import com.pighand.aio.vo.ECommerce.BillVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
public interface BillService extends BaseService<BillDomain> {

    /**
     * 创建
     *
     * @param billVO
     * @return
     */
    BillVO create(BillVO billVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    BillDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param billVO
     * @return PageOrList<BillVO>
     */
    PageOrList<BillVO> query(BillVO billVO);

    /**
     * 修改
     *
     * @param billVO
     */
    void update(BillVO billVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
