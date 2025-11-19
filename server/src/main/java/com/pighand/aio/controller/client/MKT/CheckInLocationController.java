package com.pighand.aio.controller.client.MKT;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.MKT.CheckInActivityDomain;
import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import com.pighand.aio.domain.MKT.CheckInUserDomain;
import com.pighand.aio.service.MKT.CheckInActivityService;
import com.pighand.aio.service.MKT.CheckInLocationService;
import com.pighand.aio.service.MKT.CheckInRecordService;
import com.pighand.aio.service.MKT.CheckInUserService;
import com.pighand.aio.vo.MKT.CheckInLocationVO;
import com.pighand.aio.vo.MKT.CheckInUserVO;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private final CheckInActivityService checkInActivityService;

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
     * @return
     */
    @Authorization
    @Post(path = "join", docSummary = "参加打卡活动")
    public Result<CheckInUserDomain> joinActivity(@RequestBody CheckInLocationVO checkInLocationVO) {
        LoginUser loginUser = Context.loginUser();

        CheckInActivityDomain activityDomain = checkInActivityService.find(checkInLocationVO.getId());
        if (activityDomain == null) {
            throw new ThrowPrompt("活动不存在");
        }

        // 检查是否已经参加过活动
        CheckInUserVO existingUser = checkInUserService.findByUser(loginUser.getId());

        if (existingUser != null) {
            if (existingUser.getActivityId() != null && !existingUser.getActivityId().equals(activityDomain.getId())
                && existingUser.getEndTime().isAfter(LocalDateTime.now())) {
                throw new ThrowPrompt("您已参加其他活动");
            }

            if (activityDomain.getTime() != null) {
                // 已参加活动的用户，检查剩余时间
                Date now = new Date();

                long remainingTime =
                    Date.from(existingUser.getEndTime().atZone(ZoneId.systemDefault()).toInstant()).getTime()
                        - now.getTime();
                long halfHourInMillis = 30 * 60 * 1000; // 半小时毫秒数

                if (remainingTime > 0 && remainingTime <= halfHourInMillis) {
                    // 距离结束还剩半小时，追加2.5小时
                    long extendTime = activityDomain.getTime() * 60 * 1000;
                    Date newEndTime = new Date(
                        Date.from(existingUser.getEndTime().atZone(ZoneId.systemDefault()).toInstant()).getTime()
                            + extendTime);
                    checkInUserService.extendEndTime(loginUser.getId(),
                        LocalDateTime.ofInstant(newEndTime.toInstant(), ZoneId.systemDefault()));

                    // 更新existingUser的结束时间
                    existingUser.setEndTime(newEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    existingUser.setActivityId(activityDomain.getId());

                    return new Result<>(existingUser);
                } else if (remainingTime <= 0) {
                    Date newEndTime = new Date(System.currentTimeMillis() + (activityDomain.getTime() * 60 * 1000));
                    checkInUserService.extendEndTime(loginUser.getId(),
                        LocalDateTime.ofInstant(newEndTime.toInstant(), ZoneId.systemDefault()));

                    // 更新existingUser的结束时间
                    existingUser.setEndTime(newEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    existingUser.setActivityId(activityDomain.getId());

                    return new Result<>(existingUser);
                } else {
                    // 大于半小时的，时间不变，直接返回成功
                    existingUser.setActivityId(activityDomain.getId());
                    return new Result<>(existingUser);
                }
            } else {
                LocalDateTime begin = LocalDateTime.now().with(activityDomain.getBeginTime());
                LocalDateTime end = LocalDateTime.now().with(activityDomain.getEndTime());

                existingUser.setBeginTime(begin);
                existingUser.setEndTime(end);
                existingUser.setActivityId(activityDomain.getId());

                checkInUserService.update(existingUser);

                return new Result<>(existingUser);
            }
        }

        // 未参加的直接参加活动，默认2.5小时
        com.pighand.aio.vo.MKT.CheckInUserVO checkInUserVO = new com.pighand.aio.vo.MKT.CheckInUserVO();
        checkInUserVO.setUserId(loginUser.getId());
        checkInUserVO.setActivityId(activityDomain.getId());
        if (activityDomain.getTime() != null) {
            checkInUserVO.setBeginTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            checkInUserVO.setEndTime(
                new Date(System.currentTimeMillis() + (activityDomain.getTime() * 60 * 1000)).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else {
            LocalDateTime begin = LocalDateTime.now().with(activityDomain.getBeginTime());
            LocalDateTime end = LocalDateTime.now().with(activityDomain.getEndTime());
            checkInUserVO.setBeginTime(begin);
            checkInUserVO.setEndTime(end);
        }

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
        List<CheckInRecordDomain> todayRecords = checkInRecordService.findTodayRecords(loginUser.getId());

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
        if (LocalDateTime.now().isAfter(checkInUser.getEndTime())) {
            throw new ThrowPrompt("打卡活动已结束");
        }

        // 检查今日是否已打卡
        LocalDate today = LocalDate.now();
        CheckInRecordDomain todayRecord = checkInRecordService.findTodayRecord(userId, locationId, today);
        if (todayRecord != null) {
            return new Result().success();
        }

        // 创建打卡记录
        checkInRecordService.createCheckInRecord(userId, checkInUser.getActivityId(), locationId, today);

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
        List<CheckInRecordDomain> todayRecords = checkInRecordService.findTodayRecords(loginUser.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new Result<>(Map.of("joined", true, "beginTime",
            checkInUser.getBeginTime() != null ? checkInUser.getBeginTime().format(formatter) : "", "endTime",
            checkInUser.getEndTime() != null ? checkInUser.getEndTime().format(formatter) : "", "isExpired",
            LocalDateTime.now().isAfter(checkInUser.getEndTime()), "todayRecords", todayRecords));
    }
}