package com.pighand.aio.vo.IoT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Data
@TableRef(DeviceTaskDomain.class)
@EqualsAndHashCode(callSuper = false)
public class DeviceTaskVO extends DeviceTaskDomain {

    // relation table: begin
    private DeviceVO device;
    // relation table: end

    // 任务所属设备的客户端ID
    private String clientId;
}
