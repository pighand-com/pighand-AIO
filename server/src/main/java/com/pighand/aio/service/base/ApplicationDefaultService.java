package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.ApplicationDefaultDomain;
import com.pighand.aio.vo.base.ApplicationDefaultVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 项目默认设置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ApplicationDefaultService extends BaseService<ApplicationDefaultDomain> {

    /**
     * 创建
     *
     * @param projectDefaultVO
     * @return
     */
    ApplicationDefaultVO create(ApplicationDefaultVO projectDefaultVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ApplicationDefaultVO find(Long id);

    /**
     * 分页或列表
     *
     * @param projectDefaultVO
     * @return PageOrList<ProjectDefaultVO>
     */
    PageOrList<ApplicationDefaultVO> query(ApplicationDefaultVO projectDefaultVO);

    /**
     * 修改
     *
     * @param projectDefaultVO
     */
    void update(ApplicationDefaultVO projectDefaultVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
