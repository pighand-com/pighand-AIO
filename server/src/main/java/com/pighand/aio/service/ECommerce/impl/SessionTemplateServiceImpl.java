package com.pighand.aio.service.ECommerce.impl;

import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.mapper.ECommerce.SessionTemplateMapper;
import com.pighand.aio.service.ECommerce.SessionTemplateService;
import com.pighand.aio.vo.ECommerce.SessionTemplateVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Service
public class SessionTemplateServiceImpl extends BaseServiceImpl<SessionTemplateMapper, SessionTemplateDomain>
    implements SessionTemplateService {

    /**
     * 创建
     *
     * @param sessionTemplateVO
     * @return
     */
    @Override
    public SessionTemplateVO create(SessionTemplateVO sessionTemplateVO) {
        super.mapper.insert(sessionTemplateVO);

        return sessionTemplateVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SessionTemplateDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateVO
     * @return PageOrList<SessionTemplateVO>
     */
    @Override
    public PageOrList<SessionTemplateVO> query(SessionTemplateVO sessionTemplateVO) {

        return super.mapper.query(sessionTemplateVO, null);
    }

    /**
     * 修改
     *
     * @param sessionTemplateVO
     */
    @Override
    public void update(SessionTemplateVO sessionTemplateVO) {
        super.mapper.update(sessionTemplateVO);
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
