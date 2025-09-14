package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.AssetsCollectionDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import lombok.Data;

/**
 * CMS - 素材 - 专辑
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@TableRef(AssetsCollectionDomain.class)
@Data
public class AssetsCollectionVO extends AssetsCollectionDomain {

    // relation table: begin
    private UserExtensionDomain creator;
    // relation table: end
}
