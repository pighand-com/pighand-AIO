package com.pighand.aio.controller.dashboard.MKT;

import com.pighand.aio.service.MKT.CheckInActivityService;
import com.pighand.aio.vo.MKT.CheckInActivityVO;
import com.pighand.aio.vo.MKT.ActivityStatsVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
/**
 * MKT - 打卡活动
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@RestController(path = "dashboard/check-in/activity", docName = "MKT - 打卡活动")
public class CheckInActivityController extends BaseController<CheckInActivityService> {

    /**
     * 创建打卡活动
     *
     * @param checkInActivityVO 打卡活动信息
     * @return 创建后的打卡活动信息
     */
    @Post(docSummary = "创建打卡活动", fieldGroup = "checkInActivityCreate")
    public Result<CheckInActivityVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody CheckInActivityVO checkInActivityVO) {
        checkInActivityVO = super.service.create(checkInActivityVO);

        return new Result(checkInActivityVO);
    }

    /**
     * 查询打卡活动详情
     *
     * @param id 打卡活动ID
     * @return 打卡活动详情
     */
    @Get(path = "{id}", docSummary = "查询打卡活动详情")
    public Result<CheckInActivityVO> find(@PathVariable(name = "id") Long id) {
        CheckInActivityVO checkInActivityVO = new CheckInActivityVO();
        checkInActivityVO.setId(id);
        PageOrList<CheckInActivityVO> result = super.service.query(checkInActivityVO);
        CheckInActivityVO foundActivity = result.getRecords().isEmpty() ? null : result.getRecords().get(0);

        return new Result(foundActivity);
    }

    /**
     * 分页或列表查询打卡活动
     *
     * @param checkInActivityVO 查询条件
     * @return 分页或列表结果
     */
    @Get(docSummary = "分页或列表查询打卡活动", fieldGroup = "checkInActivityQuery")
    public Result<PageOrList<CheckInActivityVO>> query(CheckInActivityVO checkInActivityVO) {
        PageOrList<CheckInActivityVO> result = super.service.query(checkInActivityVO);

        return new Result(result);
    }

    /**
     * 更新打卡活动
     *
     * @param id                打卡活动ID
     * @param checkInActivityVO 更新的打卡活动信息
     * @return 更新后的打卡活动信息
     */
    @Put(path = "{id}", docSummary = "更新打卡活动", fieldGroup = "checkInActivityUpdate")
    public Result<CheckInActivityVO> update(@PathVariable(name = "id") Long id,
        @RequestBody CheckInActivityVO checkInActivityVO) {
        checkInActivityVO.setId(id);
        checkInActivityVO = super.service.update(checkInActivityVO);

        return new Result(checkInActivityVO);
    }

    /**
     * 删除打卡活动
     *
     * @param id 打卡活动ID
     * @return 删除结果
     */
    @Delete(path = "{id}", docSummary = "删除打卡活动")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);

        return new Result();
    }

    /**
     * 生成打卡活动小程序二维码
     *
     * @param id 打卡活动ID
     * @return Base64编码的二维码图片
     */
    @Get(path = "{id}/qrcode", docSummary = "生成打卡活动小程序二维码")
    public Result<String> getWechatAppletQrcode(@PathVariable(name = "id") Long id) {
        String qrcode = super.service.getWechatAppletQrcode(id);

        return new Result(qrcode);
    }

    @Get(path = "{id}/stats", docSummary = "活动按日期统计")
    public Result<ActivityStatsVO> stats(@PathVariable(name = "id") Long id,
        @RequestParam(name = "date") LocalDate date) {
        ActivityStatsVO vo = super.service.getStats(id, date);
        return new Result(vo);
    }
}