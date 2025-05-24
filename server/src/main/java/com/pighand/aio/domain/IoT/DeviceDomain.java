package com.pighand.aio.domain.IoT;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Table("iot_device")
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceDomain extends BaseDomainRecord<DeviceDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("deviceCreate")
    @RequestFieldException("deviceUpdate")
    private Long id;

    @Length(max = 128)
    @Schema(description = "设备编号")
    private String sn;

    @Length(max = 32)
    @Schema(description = "服务器客户端id，用于发信息使用")
    private String clientId;

    @Length(max = 32)
    @Schema(description = "分组")
    private String group;

    @Length(max = 128)
    @Schema(description = "描述")
    private String description;

    @Schema(description = "连接状态 10未连接 20连接中 30已连接")
    private Integer linkStatus;

    @Schema(description = "运行状态 10未运行 20运行中")
    private Integer runningStatus;

    @Length(max = 255)
    @Schema(description = "坐标", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<BigDecimal> coordinate;

    @Schema(description = "配置信息", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private Object config;

    @Schema(description = "最后一次运行时间，用作超时校验")
    private Date lastRunningAt;
}
