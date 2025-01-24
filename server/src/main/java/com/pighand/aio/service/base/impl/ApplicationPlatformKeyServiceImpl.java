package com.pighand.aio.service.base.impl;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.mapper.base.ApplicationPlatformKeyMapper;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.base.ApplicationService;
import com.pighand.aio.vo.base.ApplicationPlatformKeyVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.ApplicationPlatformKeyTableDef.APPLICATION_PLATFORM_KEY;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
@RequiredArgsConstructor
public class ApplicationPlatformKeyServiceImpl
    extends BaseServiceImpl<ApplicationPlatformKeyMapper, ApplicationPlatformKeyDomain>
    implements ApplicationPlatformKeyService {

    private final ApplicationService applicationService;

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
     * @param platform
     * @returns platformKey {@link ApplicationPlatformKeyDomain}
     */
    @Override
    public ApplicationPlatformKeyDomain findByPlatform(PlatformEnum platform) {
        Long applicationId = Context.applicationId();
        ApplicationPlatformKeyDomain projectPlatformKeyDomain =
            this.queryChain().where(APPLICATION_PLATFORM_KEY.APPLICATION_ID.eq(applicationId))
                .and(APPLICATION_PLATFORM_KEY.PLATFORM.eq(platform.value)).one();

        if (projectPlatformKeyDomain == null) {
            throw new ThrowPrompt("未配置平台key");
        }

        return projectPlatformKeyDomain;
    }

    /**
     * 应用上传key
     *
     * @return
     */
    @Override
    public ApplicationPlatformKeyDomain uploadKey() {
        Long applicationId = Context.applicationId();
        ApplicationDomain applicationDomain = applicationService.find(applicationId);

        if (applicationDomain == null) {
            throw new ThrowPrompt("未配置上传类型");
        }

        return this.findByPlatform(applicationDomain.getUploadType());
    }
}
