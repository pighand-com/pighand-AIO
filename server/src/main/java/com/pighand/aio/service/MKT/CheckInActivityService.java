package com.pighand.aio.service.MKT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.sdk.wechat.WechatSDK;
import com.pighand.aio.domain.MKT.CheckInActivityDomain;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.mapper.MKT.CheckInActivityMapper;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.vo.MKT.ActivityStatsVO;
import com.pighand.aio.vo.MKT.CheckInActivityVO;
import com.pighand.aio.vo.MKT.LocationStatVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;

/**
 * 营销 - 打卡活动
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Service
@RequiredArgsConstructor
public class CheckInActivityService extends BaseServiceImpl<CheckInActivityMapper, CheckInActivityDomain> {

    private final ApplicationPlatformKeyService applicationPlatformKeyService;

    private final CheckInRecordService checkInRecordService;
    private final CheckInUserService checkInUserService;

    /**
     * 创建打卡活动
     *
     * @param checkInActivityVO 打卡活动信息
     * @return 创建后的打卡活动信息
     */
    public CheckInActivityVO create(CheckInActivityVO checkInActivityVO) {
        super.mapper.insert(checkInActivityVO);
        return checkInActivityVO;
    }

    /**
     * 查询打卡活动详情
     *
     * @param id 打卡活动ID
     * @return 打卡活动详情
     */
    public CheckInActivityDomain find(Long id) {
        return super.mapper.selectOneById(id);
    }

    /**
     * 分页或列表查询打卡活动
     *
     * @param checkInActivityVO 查询条件
     * @return 分页或列表结果
     */
    public PageOrList<CheckInActivityVO> query(CheckInActivityVO checkInActivityVO) {
        QueryWrapper queryWrapper = QueryWrapper.create();

        // 根据名称模糊查询
        if (checkInActivityVO.getName() != null && !checkInActivityVO.getName().isEmpty()) {
            queryWrapper.like("name", checkInActivityVO.getName());
        }

        // 根据时长查询
        if (checkInActivityVO.getTime() != null) {
            queryWrapper.eq("time", checkInActivityVO.getTime());
        }

        return super.mapper.query(checkInActivityVO, queryWrapper);
    }

    /**
     * 更新打卡活动
     *
     * @param checkInActivityVO 更新的打卡活动信息
     * @return 更新后的打卡活动信息
     */
    public CheckInActivityVO update(CheckInActivityVO checkInActivityVO) {
        super.mapper.update(checkInActivityVO);
        return checkInActivityVO;
    }

    /**
     * 删除打卡活动
     *
     * @param id 打卡活动ID
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 生成打卡活动小程序二维码
     *
     * @param activityId 打卡活动ID
     * @return Base64编码的二维码图片
     */
    public String getWechatAppletQrcode(Long activityId) {
        // 获取微信小程序平台配置
        ApplicationPlatformKeyDomain key = applicationPlatformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);

        // 获取访问令牌
        String accessToken = WechatSDK.MINI_APPLET.accessToken(key.getAppid(), key.getSecret(), "client_credential");
        String token = WechatSDK.utils.extractAccessToken(accessToken);

        String qrcodeEnv = WechatSDK.utils.getAppletEnv();

        // 构建二维码参数
        HashMap params = new HashMap<>(5);
        params.put("page", "pages/MKT/pages/check-in");
        params.put("scene", "id=" + activityId);
        params.put("env_version", qrcodeEnv);
        params.put("is_hyaline", true);
        params.put("check_path", false);

        // 设置请求头
        HashMap<String, String> headers = new HashMap<>(1);
        String body = new ObjectMapper().valueToTree(params).toString();
        headers.put("Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));

        // 调用微信API生成二维码
        byte[] images = WechatSDK.MINI_APPLET.getAppletQRCode(token, params, headers);

        // 返回Base64编码的图片
        return Base64.getEncoder().encodeToString(images);
    }

    public ActivityStatsVO getStats(Long activityId, LocalDate date) {
        ActivityStatsVO vo = new ActivityStatsVO();
        vo.setDate(date);

        Long total = checkInUserService.countParticipants(activityId, date);
        vo.setTotalParticipants(total);

        java.util.List<LocationStatVO> locations = checkInRecordService.countByLocation(date, activityId);
        vo.setLocations(locations);

        return vo;
    }
}