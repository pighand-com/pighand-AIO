package com.pighand.aio.controller.dashboard.MKT;

import com.pighand.aio.domain.MKT.CheckInLocationDomain;
import com.pighand.aio.service.MKT.CheckInLocationService;
import com.pighand.aio.vo.MKT.CheckInLocationVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 营销 - 打卡地点管理
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@RestController(path = "dashboard/check-in/location", docName = "营销 - 打卡地点管理")
@RequiredArgsConstructor
public class CheckInLocationController extends BaseController<CheckInLocationService> {

    /**
     * 创建打卡地点
     *
     * @param checkInLocationVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "checkInLocationCreate")
    public Result<CheckInLocationVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody CheckInLocationVO checkInLocationVO) {
        CheckInLocationVO result = super.service.create(checkInLocationVO);
        return new Result<>(result);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<CheckInLocationDomain> find(@PathVariable(name = "id") Long id) {
        CheckInLocationDomain result = super.service.find(id);
        return new Result<>(result);
    }

    /**
     * 分页或列表
     *
     * @param checkInLocationVO
     * @return
     */
    @Get(docSummary = "分页或列表", fieldGroup = "checkInLocationQuery")
    public Result<PageOrList<CheckInLocationVO>> query(CheckInLocationVO checkInLocationVO) {
        PageOrList<CheckInLocationVO> result = super.service.query(checkInLocationVO);
        return new Result<>(result);
    }

    /**
     * 修改
     *
     * @param id
     * @param checkInLocationVO
     * @return
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "checkInLocationUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody CheckInLocationVO checkInLocationVO) {
        checkInLocationVO.setId(id);
        super.service.update(checkInLocationVO);
        return new Result();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}