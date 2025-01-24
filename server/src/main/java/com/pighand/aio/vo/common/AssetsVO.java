package com.pighand.aio.vo.common;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.domain.common.AssetsDomain;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToListLongSerializer;
import lombok.Data;

import java.util.List;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@TableRef(AssetsDomain.class)
@Data
public class AssetsVO extends AssetsDomain {

    // relation table: begin
    @JsonIncludeProperties({"id", "name"})
    private ApplicationDomain application;

    @JsonIncludeProperties({"id", "name"})
    private UserExtensionDomain creator;
    // relation table: end

    @RequestFieldException("comAssetsCreate")
    private List<String> urls;

    @JsonDeserialize(using = ToListLongSerializer.class)
    private List<Long> ids;
}
