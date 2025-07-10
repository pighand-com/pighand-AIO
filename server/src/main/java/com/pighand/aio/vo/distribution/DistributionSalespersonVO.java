package com.pighand.aio.vo.distribution;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Data
@TableRef(DistributionSalespersonDomain.class)
@EqualsAndHashCode(callSuper = false)
public class DistributionSalespersonVO extends DistributionSalespersonDomain {

    // relation table: begin
    private UserDomain user;
    // relation table: end

    // query: begin
    private String userPhone;
    // query: end
}
