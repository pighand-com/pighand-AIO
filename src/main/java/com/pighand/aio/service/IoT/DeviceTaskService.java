package com.pighand.aio.service.IoT;

import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.transaction.annotation.Transactional;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
public interface DeviceTaskService extends BaseService<DeviceTaskDomain> {

    /**
     * 创建
     *
     * @param deviceTaskVO
     * @return
     */
    DeviceTaskVO create(DeviceTaskVO deviceTaskVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DeviceTaskDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param deviceTaskVO
     * @return PageOrList<DeviceTaskVO>
     */
    PageOrList<DeviceTaskVO> query(DeviceTaskVO deviceTaskVO);

    /**
     * 执行IoT设备任务
     */
    void runDeviceTask();

    /**
     * 完成设备任务
     *
     * @param taskId
     */
    @Transactional(rollbackFor = Exception.class)
    void finishDeviceTask(Long taskId);

    void setMessage(Long id, String message);
}
