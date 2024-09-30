package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.aio.domain.ECommerce.TicketUserValidityDomain;
import lombok.Data;

import java.util.List;

/**
 * 电商 - 票务 - 已购票使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Data
public class TicketUserValidityVO extends TicketUserValidityDomain {

    // relation table: begin
    // relation table: end

    // 可用范围
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Long> validityIds;
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<String> validityConfig;
    private List<TicketValidityDetailEntity> validityDetail;
}
