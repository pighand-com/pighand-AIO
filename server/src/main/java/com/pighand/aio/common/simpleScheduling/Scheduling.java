package com.pighand.aio.common.simpleScheduling;

import com.pighand.aio.domain.system.SimpleSchedulingDomain;
import com.pighand.aio.service.ECommerce.SessionService;
import com.pighand.aio.service.IoT.DeviceTaskService;
import com.pighand.aio.service.system.SimpleSchedulingService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.pighand.aio.domain.system.table.SimpleSchedulingTableDef.SIMPLE_SCHEDULING;

/**
 * 简单定时任务，单机模式
 */
@Component
@EnableScheduling
@AllArgsConstructor
public class Scheduling implements SchedulingConfigurer {
    private final SimpleSchedulingService simpleSchedulingService;

    private final SessionService sessionService;

    private final DeviceTaskService deviceTaskService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        List<SimpleSchedulingDomain> scheduling =
            simpleSchedulingService.queryChain().select(SIMPLE_SCHEDULING.CORN, SIMPLE_SCHEDULING.FUNCTION_NAME)
                .where(SIMPLE_SCHEDULING.ENABLE.eq(true)).list();

        scheduling.forEach(schedulingItem -> {
            scheduledTaskRegistrar.addTriggerTask(() -> process(schedulingItem.getFunctionName()),
                triggerContext -> new CronTrigger(schedulingItem.getCorn()).nextExecutionTime(triggerContext)
                    .toInstant());
        });

    }

    private void process(String functionName) {
        switch (functionName) {
            // 创建新的场次
            case "newSession":
                sessionService.create(1L);
                break;
            // 执行IoT设备任务
            case "runDeviceTask":
                deviceTaskService.runDeviceTask();
                break;
            default:
                break;
        }
    }

}
