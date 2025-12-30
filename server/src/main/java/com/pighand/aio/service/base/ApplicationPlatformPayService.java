package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import com.pighand.aio.mapper.base.ApplicationPlatformPayMapper;
import com.pighand.aio.vo.base.ApplicationPlatformPayVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Optional;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Service
public class ApplicationPlatformPayService
    extends BaseServiceImpl<ApplicationPlatformPayMapper, ApplicationPlatformPayDomain>
     {

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 创建
     *
     * @param projectPlatformPayVO
     * @return
     */
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
    public ApplicationPlatformPayDomain find(Long id) {
        ApplicationPlatformPayDomain domain = super.mapper.find(id);

        String wechatMerchantCertificate =
            Optional.ofNullable(domain.getWechatMerchantCertificate()).orElse("apiclient_key.pem");
        String wechatMerchantPublicKey = Optional.ofNullable(domain.getWechatMerchantPublicKey()).orElse("pub_key.pem");

        domain.setWechatMerchantCertificate(Paths.get(uploadPath).resolve(wechatMerchantCertificate).toString());
        domain.setWechatMerchantPublicKey(Paths.get(uploadPath).resolve(wechatMerchantPublicKey).toString());

        return domain;
    }

    /**
     * 分页或列表
     *
     * @param projectPlatformPayVO
     * @return PageOrList<ApplicationPlatformPayVO>
     */
    public PageOrList<ApplicationPlatformPayVO> query(ApplicationPlatformPayVO projectPlatformPayVO) {

        QueryWrapper queryWrapper = QueryWrapper.create();

        return super.mapper.query(projectPlatformPayVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param projectPlatformPayVO
     */
    public void update(ApplicationPlatformPayVO projectPlatformPayVO) {
        super.mapper.update(projectPlatformPayVO);
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
