package com.pighand.aio.vo.IoT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.IoT.DeviceDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Data
@TableRef(DeviceDomain.class)
@EqualsAndHashCode(callSuper = false)
public class DeviceVO extends DeviceDomain {

    // relation table: begin
    private List<DeviceTaskVO> deviceTask;
    // relation table: end

    // 是否已签到
    private Boolean clockIn;
}
