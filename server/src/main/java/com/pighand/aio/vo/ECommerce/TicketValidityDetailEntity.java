package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 电商 - 票务 - 使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Data
public class TicketValidityDetailEntity {

    private Long id;

    private String name;

    @Schema(description = "配置信息", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private Object config;
}
