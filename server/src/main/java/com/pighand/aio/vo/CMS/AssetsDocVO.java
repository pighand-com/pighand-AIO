package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.aio.domain.CMS.AssetsDocDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.vo.base.OrgDepartmentSimpleVO;
import lombok.Data;

import java.util.List;

/**
 * CMS - 素材 - 文档
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Data
@TableRef(AssetsDocDomain.class)
public class AssetsDocVO extends AssetsDocDomain {

    // relation table: begin
    private AssetsClassificationDomain classification;

    // 专辑相关信息
    private List<AssetsCollectionRelevanceVO> collections;
    
    // 创建人信息
    private UserExtensionDomain creator;
    
    // 创建人组织架构
    private OrgDepartmentSimpleVO creatorDepartment;
    // relation table: end

    private List<Long> collectionIds;
}
