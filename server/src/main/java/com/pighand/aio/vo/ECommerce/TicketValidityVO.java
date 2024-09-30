package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketValidityDomain;
import lombok.Data;

import java.util.List;

/**
 * 电商 - 票务 - 使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Data
public class TicketValidityVO extends TicketValidityDomain {

    // relation table: begin
    // relation table: end

    private List<TicketValidityDetailEntity> validityDetail;
}
