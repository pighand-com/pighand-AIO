package com.pighand.aio.domain.IoT;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Table("iot_device_task")
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceTaskDomain extends BaseDomainRecord<DeviceTaskDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("deviceTaskCreate")
    @RequestFieldException("deviceTaskUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long deviceId;

    @Length(max = 65535)
    @Schema(description = "报文")
    private String message;

    @Schema(description = "任务运行状态 0不执行 10未执行 20执行中 30已执行")
    private Integer runningStatus;

    @Schema(description = "任务创建人")
    private Long creatorId;

    @Schema(description = "任务创建时间")
    private Date createdAt;

    @Schema(description = "任务开始时间")
    private Date runningAt;

    @Schema(description = "任务完成时间")
    private Date completedAt;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
}
