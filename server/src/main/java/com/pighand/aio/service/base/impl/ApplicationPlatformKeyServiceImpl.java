package com.pighand.aio.service.base.impl;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.mapper.base.ApplicationPlatformKeyMapper;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.vo.base.ApplicationPlatformKeyVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.ProjectPlatformKeyTableDef.PROJECT_PLATFORM_KEY;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
public class ApplicationPlatformKeyServiceImpl
    extends BaseServiceImpl<ApplicationPlatformKeyMapper, ApplicationPlatformKeyDomain>
    implements ApplicationPlatformKeyService {

    /**
     * 创建
     *
     * @param platformKeyVO
     * @return
     */
    @Override
    public ApplicationPlatformKeyVO create(ApplicationPlatformKeyVO platformKeyVO) {
        super.mapper.insert(platformKeyVO);

        return platformKeyVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ApplicationPlatformKeyDomain find(Long id) {
        //        PlatformKeyDomain platformKeyDomain = super.mapper.selectById(id);
        return null;
    }

    /**
     * 分页或列表
     *
     * @param platformKeyVO
     */
    @Override
    public PageOrList<ApplicationPlatformKeyVO> query(ApplicationPlatformKeyVO platformKeyVO) {
        //        PageOrList pageInfo = platformKeyVO.pageParamOrInit(PageType.NEXT_TOKEN);
        //        return super.mapper.query(pageInfo, platformKeyVO);
        return null;
    }

    /**
     * 修改
     *
     * @param platformKeyVO
     */
    @Override
    public void update(ApplicationPlatformKeyVO platformKeyVO) {
        //        super.mapper.updateById(platformKeyVO);
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

    /**
     * 查询key
     *
     * @param projectId
     * @param platform
     * @returns platformKey {@link ApplicationPlatformKeyDomain}
     */
    @Override
    public ApplicationPlatformKeyDomain findByPlatform(Long projectId, PlatformEnum platform) {
        ApplicationPlatformKeyDomain projectPlatformKeyDomain =
            this.queryChain().where(PROJECT_PLATFORM_KEY.PROJECT_ID.eq(projectId))
                .and(PROJECT_PLATFORM_KEY.PLATFORM.eq(platform.value)).one();

        if (projectPlatformKeyDomain == null) {
            throw new ThrowPrompt("未配置三方平台key");
        }

        return projectPlatformKeyDomain;
    }
}
