package com.pighand.aio.service.system;

import com.pighand.aio.domain.system.SimpleSchedulingDomain;
import com.pighand.aio.vo.system.SimpleSchedulingVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 简单定时器
 *
 * @author wangshuli
 * @createDate 2024-04-16 11:44:49
 */
public interface SimpleSchedulingService extends BaseService<SimpleSchedulingDomain> {

    /**
     * 创建
     *
     * @param simpleSchedulingVO
     * @return
     */
    SimpleSchedulingVO create(SimpleSchedulingVO simpleSchedulingVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SimpleSchedulingDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param simpleSchedulingVO
     * @return PageOrList<SimpleSchedulingVO>
     */
    PageOrList<SimpleSchedulingVO> query(SimpleSchedulingVO simpleSchedulingVO);

    /**
     * 修改
     *
     * @param simpleSchedulingVO
     */
    void update(SimpleSchedulingVO simpleSchedulingVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
