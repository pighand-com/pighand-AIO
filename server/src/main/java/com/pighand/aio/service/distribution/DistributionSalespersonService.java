package com.pighand.aio.service.distribution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.sdk.wechat.WechatSDK;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.mapper.distribution.DistributionSalespersonMapper;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static com.pighand.aio.domain.base.table.UserTableDef.USER;
import static com.pighand.aio.domain.distribution.table.DistributionSalespersonTableDef.DISTRIBUTION_SALESPERSON;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
@RequiredArgsConstructor
public class DistributionSalespersonService
    extends BaseServiceImpl<DistributionSalespersonMapper, DistributionSalespersonDomain>
     {

    private final ApplicationPlatformKeyService projectPlatformKeyService;

    /**
     * 创建
     *
     * @param distDistributionSalespersonVO
     * @return
     */
    public DistributionSalespersonVO create(DistributionSalespersonVO distDistributionSalespersonVO) {
        distDistributionSalespersonVO.setStatus(10);
        super.mapper.insert(distDistributionSalespersonVO);

        return distDistributionSalespersonVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public DistributionSalespersonDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 根据用户ID查询销售资格
     *
     * @param userId
     * @return
     */
    public DistributionSalespersonVO findByUserId(Long userId) {
        return queryChain().where(DISTRIBUTION_SALESPERSON.USER_ID.eq(userId))
            .and(DISTRIBUTION_SALESPERSON.STATUS.eq(10)).oneAs(DistributionSalespersonVO.class);
    }

    /**
     * 分页或列表
     *
     * @param distDistributionSalespersonVO
     * @return PageOrList<DistDistributionSalespersonVO>
     */
    public PageOrList<DistributionSalespersonVO> query(DistributionSalespersonVO distDistributionSalespersonVO) {
        distDistributionSalespersonVO.setJoinTables(USER.getName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(DISTRIBUTION_SALESPERSON.ID, DISTRIBUTION_SALESPERSON.USER_ID, DISTRIBUTION_SALESPERSON.STATUS)

            // like
            .and(USER.PHONE.like(distDistributionSalespersonVO.getUserPhone(), VerifyUtils::isNotEmpty))

            // equal
            .and(DISTRIBUTION_SALESPERSON.USER_ID.eq(distDistributionSalespersonVO.getUserId()))
            .and(DISTRIBUTION_SALESPERSON.STATUS.eq(distDistributionSalespersonVO.getStatus()));

        return super.mapper.query(distDistributionSalespersonVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param distDistributionSalespersonVO
     */
    public void update(DistributionSalespersonVO distDistributionSalespersonVO) {
        UpdateChain updateChain =
            this.updateChain().where(DISTRIBUTION_SALESPERSON.ID.eq(distDistributionSalespersonVO.getId()));

        updateChain.set(DISTRIBUTION_SALESPERSON.ID, distDistributionSalespersonVO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 启用
     *
     * @param id
     */
    public void enable(Long id) {
        UpdateChain updateChain = this.updateChain().where(DISTRIBUTION_SALESPERSON.ID.eq(id));
        updateChain.set(DISTRIBUTION_SALESPERSON.STATUS, 10);
        updateChain.update();
    }

    /**
     * 停用
     *
     * @param id
     */
    public void disable(Long id) {
        UpdateChain updateChain = this.updateChain().where(DISTRIBUTION_SALESPERSON.ID.eq(id));
        updateChain.set(DISTRIBUTION_SALESPERSON.STATUS, 0);
        updateChain.update();
    }

    /**
     * 获取销售二维码
     */
    public String getWechatAppletQrcode(Long salespersonId) {
        ApplicationPlatformKeyDomain key = projectPlatformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);

        String accessToken = WechatSDK.MINI_APPLET.accessToken(key.getAppid(), key.getSecret(), "client_credential");

        String token = WechatSDK.utils.extractAccessToken(accessToken);

        String qrcodeEnv = WechatSDK.utils.getAppletEnv();

        HashMap params = new HashMap<>(5);
        params.put("page", "pages/base/pages/index");
        params.put("scene", "sales=" + salespersonId);
        params.put("env_version", qrcodeEnv);
        params.put("is_hyaline", true);
        params.put("check_path", false);

        HashMap headers = new HashMap<>(1);
        String body = new ObjectMapper().valueToTree(params).toString();
        headers.put("Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));

        byte[] images = WechatSDK.MINI_APPLET.getAppletQRCode(token, params, headers);

        return Base64.getEncoder().encodeToString(images);
    }
}
