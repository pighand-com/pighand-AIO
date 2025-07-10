package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.BannerDomain;
import lombok.Data;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
@Data
@TableRef(BannerDomain.class)
public class BannerVO extends BannerDomain {

    // relation table: begin
    // relation table: end
}
