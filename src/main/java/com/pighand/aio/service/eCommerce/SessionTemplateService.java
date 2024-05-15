package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
public interface SessionTemplateService extends BaseService<SessionTemplateDomain> {

    /**
     * 创建
     *
     * @param sessionTemplateVO
     * @return
     */
    SessionTemplateVO create(SessionTemplateVO sessionTemplateVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SessionTemplateDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param sessionTemplateVO
     * @return PageOrList<SessionTemplateVO>
     */
    PageOrList<SessionTemplateVO> query(SessionTemplateVO sessionTemplateVO);

    /**
     * 修改
     *
     * @param sessionTemplateVO
     */
    void update(SessionTemplateVO sessionTemplateVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
