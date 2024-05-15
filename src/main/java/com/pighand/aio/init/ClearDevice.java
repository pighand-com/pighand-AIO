package com.pighand.aio.init;

import com.pighand.aio.service.IoT.DeviceService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;

@Component
public class ClearDevice {

    private final DeviceService deviceService;

    public ClearDevice(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * 清空所有设备连接。服务器重启时调用
     */
    @PostConstruct
    public void run() {
        deviceService.updateChain().set(DEVICE.LINK_STATUS, 10).set(DEVICE.RUNNING_STATUS, 10)
            .set(DEVICE.CLIENT_ID, null).where("1=1").update();
    }
}
