package com.pighand.aio.service.ECommerce.impl;

import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import com.pighand.aio.mapper.ECommerce.SessionTemplateGourpMapper;
import com.pighand.aio.service.ECommerce.SessionTemplateGourpService;
import com.pighand.aio.vo.ECommerce.SessionTemplateGourpVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * 电商 - 场次模板 - 分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Service
public class SessionTemplateGourpServiceImpl
    extends BaseServiceImpl<SessionTemplateGourpMapper, SessionTemplateGourpDomain>
    implements SessionTemplateGourpService {

    /**
     * 创建
     *
     * @param sessionTemplateGourpVO
     * @return
     */
    @Override
    public SessionTemplateGourpVO create(SessionTemplateGourpVO sessionTemplateGourpVO) {
        super.mapper.insert(sessionTemplateGourpVO);

        return sessionTemplateGourpVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SessionTemplateGourpDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateGourpVO
     * @return PageOrList<SessionTemplateGourpVO>
     */
    @Override
    public PageOrList<SessionTemplateGourpVO> query(SessionTemplateGourpVO sessionTemplateGourpVO) {

        return super.mapper.query(sessionTemplateGourpVO, null);
    }

    /**
     * 修改
     *
     * @param sessionTemplateGourpVO
     */
    @Override
    public void update(SessionTemplateGourpVO sessionTemplateGourpVO) {
        super.mapper.update(sessionTemplateGourpVO);
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
