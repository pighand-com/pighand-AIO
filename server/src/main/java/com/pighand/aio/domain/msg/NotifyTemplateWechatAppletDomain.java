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
 * 消息 - 通知模板 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 18:35:39
 */
@Table(value = "msg_notify_template_wechat_applet")
@Data
public class NotifyTemplateWechatAppletDomain extends BaseDomainRecord<NotifyTemplateWechatAppletDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("notifyTemplateWechatAppletCreate")
    @RequestFieldException("notifyTemplateWechatAppletUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long applicationId;

    @Column("type")
    @Schema(description = "类型 100抽奖")
    private Integer type;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 64)
    @Schema(description = "模板id")
    private String templateId;

    @Schema(description = "数据。格式json数组。item key：key-模板data字段；label-内容标题；field-获取bean值的字段，按“.”分割查不同表数据；default-默认值", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Object> data;

    @Column("url")
    @Length(max = 255)
    @Schema(description = "跳转路径 http开头跳转网页，page开头跳转小程序")
    private String url;
}
