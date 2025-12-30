package com.pighand.aio.service.IoT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.mapper.IoT.DeviceTaskMapper;
import com.pighand.aio.server.tcp.TcpClient;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;
import static com.pighand.aio.domain.IoT.table.DeviceTaskTableDef.DEVICE_TASK;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Service
@AllArgsConstructor
public class DeviceTaskService extends BaseServiceImpl<DeviceTaskMapper, DeviceTaskDomain>
     {

    private final DeviceService deviceService;

    /**
     * 创建
     *
     * @param deviceTaskVO
     * @return
     */
    public DeviceTaskVO create(DeviceTaskVO deviceTaskVO) {
        deviceTaskVO.setCreatedAt(new Date());
        deviceTaskVO.setCreatorId(Context.loginUser().getId());
        super.mapper.insert(deviceTaskVO);

        return deviceTaskVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public DeviceTaskDomain find(Long id) {

        return super.mapper.find(id, DEVICE.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param deviceTaskVO
     * @return PageOrList<DeviceTaskVO>
     */
    public PageOrList<DeviceTaskVO> query(DeviceTaskVO deviceTaskVO) {
        deviceTaskVO.setJoinTables(DEVICE.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(DEVICE_TASK.MESSAGE.like(deviceTaskVO.getMessage()))
            // equal
            .and(DEVICE_TASK.DEVICE_ID.eq(deviceTaskVO.getDeviceId()))
            .and(DEVICE_TASK.RUNNING_STATUS.eq(deviceTaskVO.getRunningStatus()))
            .and(DEVICE_TASK.CREATOR_ID.eq(deviceTaskVO.getCreatorId()));

        return super.mapper.query(deviceTaskVO, queryWrapper);
    }

    /**
     * 执行IoT设备任务
     */
    public void runDeviceTask() {
        Date now = new Date();
        List<DeviceTaskVO> notRunningTask = this.mapper.queryNotRunningTask();

        for (DeviceTaskVO deviceTask : notRunningTask) {

            // 设备状态改为运行中
            deviceService.updateChain().set(DEVICE.RUNNING_STATUS, 20).set(DEVICE.LAST_RUNNING_AT, now)
                .where(DEVICE.ID.eq(deviceTask.getDeviceId())).update();

            // 执行任务
            TcpClient.sendMessage(deviceTask.getClientId(), deviceTask.getMessage());

            // 更新任务状态，直接完成
            // TODO: 可配置直接完成状态
            this.updateChain().set(DEVICE_TASK.RUNNING_STATUS, 30).set(DEVICE_TASK.RUNNING_AT, now)
                .where(DEVICE_TASK.ID.eq(deviceTask.getId())).update();
        }
    }

    /**
     * 完成设备任务
     *
     * @param taskId
     */
    @Transactional(rollbackFor = Exception.class)
    public void finishDeviceTask(Long taskId) {
        if (taskId == null) {
            return;
        }

        DeviceTaskDomain deviceTaskDomain = this.getById(taskId);
        if (deviceTaskDomain == null) {
            return;
        }

        // 设备状态改为待命
        if (deviceTaskDomain.getDeviceId() != null) {
            deviceService.updateChain().set(DEVICE.RUNNING_STATUS, 10)
                .where(DEVICE.ID.eq(deviceTaskDomain.getDeviceId())).update();
        }

        // 更新任务状态
        this.updateChain().set(DEVICE_TASK.RUNNING_STATUS, 30).set(DEVICE_TASK.COMPLETED_AT, new Date())
            .where(DEVICE_TASK.ID.eq(taskId)).update();
    }

    public void setMessage(Long id, String message) {
        DeviceTaskDomain deviceTaskDomain = this.getById(id);

        if (deviceTaskDomain.getRunningStatus().equals(0)) {
            // 查询设备信息
            DeviceDomain deviceDomain = deviceService.getById(deviceTaskDomain.getDeviceId());

            String finalMessage = "";
            if (deviceDomain.getConfig() == null) {
                // 无配置，不传编号
                finalMessage = "AABBCC" + deviceDomain.getSn() + "0303XXFF";
            } else if (deviceDomain.getConfig().toString().indexOf("input") != -1) {
                // 输入文字
                finalMessage = "BBCCDD" + deviceDomain.getSn() + message + "FF";
            } else {
                // 传编号
                finalMessage = "AABBCC" + deviceDomain.getSn() + "0303" + message + "FF";
            }

            this.updateChain().set(DEVICE_TASK.MESSAGE, finalMessage).set(DEVICE_TASK.RUNNING_STATUS, 10)
                .where(DEVICE_TASK.ID.eq(id)).update();
        }
    }
}
