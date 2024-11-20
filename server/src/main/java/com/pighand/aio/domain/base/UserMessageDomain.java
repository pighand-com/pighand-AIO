package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.UserMessageTypeEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Table("base_user_message")
@Data
public class UserMessageDomain extends BaseDomainRecord<UserMessageDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("userMessageCreate")
    @RequestFieldException("userMessageUpdate")
    private Long id;

    @Schema(description = "类型 10系统消息 11用户发送消息")
    private UserMessageTypeEnum type;

    @Schema(description = "发件人")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long senderId;
    @Schema(description = "收件人")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiverId;
    @Length(max = 32)
    @Schema(description = "标题")
    private String title;
    @Length(max = 65535)
    @Schema(description = "消息内容")
    private String content;
    @Schema(description = "发送时间")
    private Date sendAt;
    @Schema(description = "是否已读")
    private Boolean read;

    private Boolean deleted;
}
