package com.pighand.aio.vo.distribution;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Data
@TableRef(DistributionGoodsRuleDomain.class)
@EqualsAndHashCode(callSuper = false)
public class DistributionGoodsRuleVO extends DistributionGoodsRuleDomain {

    // relation table: begin
    // relation table: end

    private String objectName;
}
