package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.aio.domain.CMS.AssetsImageDomain;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
import lombok.Data;

import java.util.List;

/**
 * CMS - 素材 - 图片
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@TableRef(AssetsImageDomain.class)
@Data
public class AssetsImageVO extends AssetsImageDomain {

    // relation table: begin
    private AssetsClassificationDomain classification;
    
    // 专辑相关信息
    private List<AssetsCollectionRelevanceVO> collections;
    // relation table: end

    private List<Long> collectionIds;
}
