package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.mapper.ECommerce.SessionTemplateMapper;
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
public class SessionTemplateService extends BaseServiceImpl<SessionTemplateMapper, SessionTemplateDomain>
     {

    /**
     * 创建
     *
     * @param sessionTemplateVO
     * @return
     */
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
    public SessionTemplateDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateVO
     * @return PageOrList<SessionTemplateVO>
     */
    public PageOrList<SessionTemplateVO> query(SessionTemplateVO sessionTemplateVO) {

        return super.mapper.query(sessionTemplateVO, null);
    }

    /**
     * 修改
     *
     * @param sessionTemplateVO
     */
    public void update(SessionTemplateVO sessionTemplateVO) {
        super.mapper.update(sessionTemplateVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
