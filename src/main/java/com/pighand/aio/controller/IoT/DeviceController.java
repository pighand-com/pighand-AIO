package com.pighand.aio.controller.IoT;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.vo.IoT.DeviceVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Authorization
@RestController(path = "device", docName = "IoT - 设备")
public class DeviceController extends BaseController<DeviceService> {

    /**
     * @param deviceVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "deviceCreate")
    public Result<DeviceVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody DeviceVO deviceVO) {
        deviceVO = super.service.create(deviceVO);

        return new Result(deviceVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<DeviceDomain> find(@PathVariable(name = "id") Long id) {
        DeviceDomain deviceDomain = super.service.find(id);

        return new Result(deviceDomain);
    }

    /**
     * @param deviceVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "deviceQuery")
    public Result<PageOrList<DeviceVO>> query(DeviceVO deviceVO) {
        PageOrList<DeviceVO> result = super.service.query(deviceVO);

        return new Result(result);
    }

    /**
     * @param deviceVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "deviceUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody DeviceVO deviceVO) {
        deviceVO.setId(id);

        super.service.update(deviceVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}
