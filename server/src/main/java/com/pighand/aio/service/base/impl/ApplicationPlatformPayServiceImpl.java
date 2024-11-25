package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import com.pighand.aio.mapper.base.ApplicationPlatformPayMapper;
import com.pighand.aio.service.base.ApplicationPlatformPayService;
import com.pighand.aio.vo.base.ApplicationPlatformPayVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.ApplicationPlatformPayTableDef.APPLICATION_PLATFORM_PAY;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Service
public class ApplicationPlatformPayServiceImpl
    extends BaseServiceImpl<ApplicationPlatformPayMapper, ApplicationPlatformPayDomain>
    implements ApplicationPlatformPayService {

    /**
     * 创建
     *
     * @param projectPlatformPayVO
     * @return
     */
    @Override
    public ApplicationPlatformPayVO create(ApplicationPlatformPayVO projectPlatformPayVO) {
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
    public ApplicationPlatformPayDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param projectPlatformPayVO
     * @return PageOrList<ApplicationPlatformPayVO>
     */
    @Override
    public PageOrList<ApplicationPlatformPayVO> query(ApplicationPlatformPayVO projectPlatformPayVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(APPLICATION_PLATFORM_PAY.WECHAT_MERCHANT_ID.like(projectPlatformPayVO.getWechatMerchantId())).and(
                APPLICATION_PLATFORM_PAY.WECHAT_MERCHANT_PRIVATE_KEY.like(
                    projectPlatformPayVO.getWechatMerchantPrivateKey())).and(
                APPLICATION_PLATFORM_PAY.WECHAT_MERCHANT_CERTIFICATE.like(
                    projectPlatformPayVO.getWechatMerchantCertificate())).and(
                APPLICATION_PLATFORM_PAY.WECHAT_MERCHANT_CERTIFICATE_SERIAL.like(
                    projectPlatformPayVO.getWechatMerchantCertificateSerial()))
            .and(APPLICATION_PLATFORM_PAY.WECHAT_MERCHANT_V3.like(projectPlatformPayVO.getWechatMerchantV3()));

        return super.mapper.query(projectPlatformPayVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param projectPlatformPayVO
     */
    @Override
    public void update(ApplicationPlatformPayVO projectPlatformPayVO) {
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
