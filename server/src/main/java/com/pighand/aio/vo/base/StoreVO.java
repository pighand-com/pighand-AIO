package com.pighand.aio.vo.base;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.domain.base.TenantDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@TableRef(StoreDomain.class)
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreVO extends StoreDomain {

    // relation table: begin
    @JsonIncludeProperties({"id", "name"})
    private ApplicationDomain application;

    @JsonIncludeProperties({"id", "name"})
    private UserExtensionDomain creator;

    @JsonIncludeProperties({"id", "name"})
    private TenantDomain tenant;
    // relation table: end

    // query: begin
    private String[] createdAtRange;
    // query: end
}
