package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateGourpVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 场次模板 - 分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
public interface SessionTemplateGourpService extends BaseService<SessionTemplateGourpDomain> {

    /**
     * 创建
     *
     * @param sessionTemplateGourpVO
     * @return
     */
    SessionTemplateGourpVO create(SessionTemplateGourpVO sessionTemplateGourpVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SessionTemplateGourpDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param sessionTemplateGourpVO
     * @return PageOrList<SessionTemplateGourpVO>
     */
    PageOrList<SessionTemplateGourpVO> query(SessionTemplateGourpVO sessionTemplateGourpVO);

    /**
     * 修改
     *
     * @param sessionTemplateGourpVO
     */
    void update(SessionTemplateGourpVO sessionTemplateGourpVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
