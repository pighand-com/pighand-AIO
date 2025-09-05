package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 组织 - 部门-用户关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Table(value = "base_org_department_user_relevance")
@Data
public class OrgDepartmentUserRelevanceDomain extends BaseDomainRecord<OrgDepartmentUserRelevanceDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("baseOrgDepartmentUserRelevanceCreate")
    @RequestFieldException("baseOrgDepartmentUserRelevanceUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long departmentId;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long userId;
}
