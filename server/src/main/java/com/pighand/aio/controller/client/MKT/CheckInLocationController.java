package com.pighand.aio.controller.client.MKT;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import com.pighand.aio.domain.MKT.CheckInUserDomain;
import com.pighand.aio.service.MKT.CheckInLocationService;
import com.pighand.aio.service.MKT.CheckInRecordService;
import com.pighand.aio.service.MKT.CheckInUserService;
import com.pighand.aio.vo.MKT.CheckInLocationVO;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 营销 - 打卡地点（小程序端）
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@RestController(path = "client/check-in", docName = "营销 - 打卡活动（小程序端）")
@RequiredArgsConstructor
public class CheckInLocationController extends BaseController<CheckInLocationService> {

    private final CheckInUserService checkInUserService;

    private final CheckInRecordService checkInRecordService;

    /**
     * 查询打卡地点列表
     *
     * @param checkInLocationVO
     * @return
     */
    @Get(path = "locations", docSummary = "查询打卡地点列表")
    public Result<PageOrList<CheckInLocationVO>> getLocations(CheckInLocationVO checkInLocationVO) {
        PageOrList<CheckInLocationVO> result = super.service.query(checkInLocationVO);
        return new Result<>(result);
    }

    /**
     * 参加打卡活动（扫码进入）
     *
     * @param fromQrCode 是否来自扫码
     * @return
     */
    @Authorization
    @Post(path = "join", docSummary = "参加打卡活动")
    public Result<CheckInUserDomain> joinActivity(@RequestParam(required = false) Boolean fromQrCode) {
        LoginUser loginUser = Context.loginUser();

        // 检查是否已经参加过活动
        CheckInUserDomain existingUser = checkInUserService.findByUser(loginUser.getId());

        if (existingUser != null) {
            // 已参加活动的用户，检查剩余时间
            Date now = new Date();
            long remainingTime = existingUser.getEndTime().getTime() - now.getTime();
            long halfHourInMillis = 30 * 60 * 1000; // 半小时毫秒数

            if (remainingTime > 0 && remainingTime <= halfHourInMillis) {
                // 距离结束还剩半小时，追加2.5小时
                long extendTime = (long)(2.5 * 60 * 60 * 1000);
                Date newEndTime = new Date(existingUser.getEndTime().getTime() + extendTime);
                checkInUserService.extendEndTime(loginUser.getId(),
                    LocalDateTime.ofInstant(newEndTime.toInstant(), ZoneId.systemDefault()));

                // 更新existingUser的结束时间
                existingUser.setEndTime(newEndTime);

                return new Result<>(existingUser);
            } else {
                // 大于半小时的，时间不变，直接返回成功
                return new Result<>(existingUser);
            }
        }

        // 未参加的直接参加活动，默认2.5小时
        com.pighand.aio.vo.MKT.CheckInUserVO checkInUserVO = new com.pighand.aio.vo.MKT.CheckInUserVO();
        checkInUserVO.setUserId(loginUser.getId());
        checkInUserVO.setEndTime(new Date(System.currentTimeMillis() + (long)(2.5 * 60 * 60 * 1000))); // 默认2.5小时

        CheckInUserDomain newUser = checkInUserService.create(checkInUserVO);

        return new Result<>(newUser);
    }

    /**
     * 查询今日打卡记录
     *
     * @return
     */
    @Authorization
    @Get(path = "records/today", docSummary = "查询今日打卡记录")
    public Result<Object> getTodayRecords() {
        LoginUser loginUser = Context.loginUser();

        // 获取今天0点的时间
        LocalDate today = LocalDate.now();
        List<CheckInRecordDomain> todayRecords = checkInRecordService.findTodayRecords(loginUser.getId(), today);

        return new Result<>(todayRecords);
    }

    /**
     * 执行打卡
     *
     * @param locationId 打卡地点ID
     * @return
     */
    @Authorization
    @Post(path = "checkIn/{locationId}/{userId}", docSummary = "执行打卡")
    public Result checkIn(@PathVariable Long locationId, @PathVariable Long userId) {
        // 检查是否已参加活动
        CheckInUserDomain checkInUser = checkInUserService.findByUser(userId);
        if (checkInUser == null) {
            return new Result().success();
        }

        // 检查活动是否已结束
        if (new Date().after(checkInUser.getEndTime())) {
            throw new ThrowPrompt("打卡活动已结束");
        }

        // 检查今日是否已打卡
        LocalDate today = LocalDate.now();
        CheckInRecordDomain todayRecord = checkInRecordService.findTodayRecord(userId, locationId, today);
        if (todayRecord != null) {
            return new Result().success();
        }

        // 创建打卡记录
        checkInRecordService.createCheckInRecord(userId, locationId, today);

        return new Result().success();
    }

    /**
     * 查询用户活动状态
     *
     * @return
     */
    @Authorization
    @Get(path = "status", docSummary = "查询用户活动状态")
    public Result<Object> getActivityStatus() {
        LoginUser loginUser = Context.loginUser();

        // 检查是否已参加活动
        CheckInUserDomain checkInUser = checkInUserService.findByUser(loginUser.getId());
        if (checkInUser == null) {
            return new Result<>(java.util.Map.of("joined", false, "message", "您还未参加该打卡活动"));
        }

        // 获取今日打卡记录
        LocalDate today = LocalDate.now();
        List<CheckInRecordDomain> todayRecords = checkInRecordService.findTodayRecords(loginUser.getId(), today);

        return new Result<>(java.util.Map.of("joined", true, "endTime", checkInUser.getEndTime(), "isExpired",
            new Date().after(checkInUser.getEndTime()), "todayRecords", todayRecords));
    }
}