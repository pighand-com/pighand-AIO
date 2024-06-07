package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ProjectPlatformPayDomain;
import com.pighand.aio.mapper.base.ProjectPlatformPayMapper;
import com.pighand.aio.service.base.ProjectPlatformPayService;
import com.pighand.aio.vo.base.ProjectPlatformPayVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.ProjectPlatformPayTableDef.PROJECT_PLATFORM_PAY;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Service
public class ProjectPlatformPayServiceImpl extends BaseServiceImpl<ProjectPlatformPayMapper, ProjectPlatformPayDomain>
    implements ProjectPlatformPayService {

    /**
     * 创建
     *
     * @param projectPlatformPayVO
     * @return
     */
    @Override
    public ProjectPlatformPayVO create(ProjectPlatformPayVO projectPlatformPayVO) {
        super.mapper.insert(projectPlatformPayVO);

        return projectPlatformPayVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ProjectPlatformPayDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param projectPlatformPayVO
     * @return PageOrList<ProjectPlatformPayVO>
     */
    @Override
    public PageOrList<ProjectPlatformPayVO> query(ProjectPlatformPayVO projectPlatformPayVO) {

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(PROJECT_PLATFORM_PAY.WECHAT_MERCHANT_ID.like(projectPlatformPayVO.getWechatMerchantId(),
            VerifyUtils::isNotEmpty));
        queryWrapper.and(
            PROJECT_PLATFORM_PAY.WECHAT_MERCHANT_PRIVATE_KEY.like(projectPlatformPayVO.getWechatMerchantPrivateKey(),
                VerifyUtils::isNotEmpty));
        queryWrapper.and(
            PROJECT_PLATFORM_PAY.WECHAT_MERCHANT_CERTIFICATE.like(projectPlatformPayVO.getWechatMerchantCertificate(),
                VerifyUtils::isNotEmpty));
        queryWrapper.and(PROJECT_PLATFORM_PAY.WECHAT_MERCHANT_CERTIFICATE_SERIAL.like(
            projectPlatformPayVO.getWechatMerchantCertificateSerial(), VerifyUtils::isNotEmpty));
        queryWrapper.and(PROJECT_PLATFORM_PAY.WECHAT_MERCHANT_V3.like(projectPlatformPayVO.getWechatMerchantV3(),
            VerifyUtils::isNotEmpty));

        return super.mapper.query(projectPlatformPayVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param projectPlatformPayVO
     */
    @Override
    public void update(ProjectPlatformPayVO projectPlatformPayVO) {
        super.mapper.update(projectPlatformPayVO);
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
