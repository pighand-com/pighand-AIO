package com.pighand.aio.controller.IoT;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.service.IoT.DeviceTaskService;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Authorization
@RestController(path = "device/task", docName = "IoT - 设备任务")
public class DeviceTaskController extends BaseController<DeviceTaskService> {

    /**
     * @param deviceTaskVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "deviceTaskCreate")
    public Result<DeviceTaskVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody DeviceTaskVO deviceTaskVO) {
        deviceTaskVO = super.service.create(deviceTaskVO);

        return new Result(deviceTaskVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<DeviceTaskDomain> find(@PathVariable(name = "id") Long id) {
        DeviceTaskDomain deviceTaskDomain = super.service.find(id);

        return new Result(deviceTaskDomain);
    }

    /**
     * @param deviceTaskVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "deviceTaskQuery")
    public Result<PageOrList<DeviceTaskVO>> query(DeviceTaskVO deviceTaskVO) {
        PageOrList<DeviceTaskVO> result = super.service.query(deviceTaskVO);

        return new Result(result);
    }

    @Post(path = "{id}", docSummary = "设置消息")
    public Result setMessage(@PathVariable(name = "id") Long id, @RequestBody DeviceTaskVO deviceTaskVO) {
        super.service.setMessage(id, deviceTaskVO.getMessage());
        return new Result();
    }
}
