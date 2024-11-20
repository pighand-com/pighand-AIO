package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.vo.base.ApplicationPlatformKeyVO;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ApplicationPlatformKeyService extends BaseService<ApplicationPlatformKeyDomain> {

    /**
     * 创建
     *
     * @param platformKeyVO
     * @return
     */
    ApplicationPlatformKeyVO create(ApplicationPlatformKeyVO platformKeyVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ApplicationPlatformKeyDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param platformKeyVO
     * @return PageOrList<PlatformKeyVO>
     */
    PageOrList<ApplicationPlatformKeyVO> query(ApplicationPlatformKeyVO platformKeyVO);

    /**
     * 修改
     *
     * @param platformKeyVO
     */
    void update(ApplicationPlatformKeyVO platformKeyVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 查询key
     *
     * @param projectId
     * @param platform
     * @returns platformKey {@link ApplicationPlatformKeyDomain}
     */
    ApplicationPlatformKeyDomain findByPlatform(Long projectId, PlatformEnum platform);
}
