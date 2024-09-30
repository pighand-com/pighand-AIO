package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 电商 - 场次 - 用户分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Table("ec_session_user_group")
@Data
public class SessionUserGroupDomain extends BaseDomainRecord<SessionUserGroupDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("sessionUserGroupCreate")
    @RequestFieldException("sessionUserGroupUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionTemplateGroupId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private Date createdAt;
}
