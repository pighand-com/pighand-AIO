package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TicketVO extends TicketDomain {

    // relation table: begin
    // relation table: end

    private List<TicketValidityVO> ticketValidity;

    private String system;
}
