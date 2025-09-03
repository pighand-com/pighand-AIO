package com.pighand.aio.domain.msg;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * 消息 - 通知配置 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 21:40:13
 */
@Table(value = "msg_notify_config_wechat_applet")
@Data
public class NotifyConfigWechatAppletDomain extends BaseDomainRecord<NotifyConfigWechatAppletDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("msgNotifyConfigWechatAppletCreate")
    @RequestFieldException("msgNotifyConfigWechatAppletUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "模板(msg_notify_config_wechat_applet)id")
    private Long notifyTemplateId;

    @Schema(description = "类型 100抽奖")
    private Integer dataType;

    @NotNull(groups = {ValidationGroup.Create.class})
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "数据id")
    private Long dataId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 64)
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "模板id")
    private String templateId;

    @Schema(description = "数据。格式json数组。item key：key-模板data字段；label-内容标题；field-获取bean值的字段，按“.”分割查不同表数据；default-默认值", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Object> data;

    @Column("url")
    @Length(max = 255)
    @Schema(description = "跳转路径 http开头跳转网页，page开头跳转小程序")
    private String url;

    @Schema(description = "是否已通知")
    private Boolean notified;
}
