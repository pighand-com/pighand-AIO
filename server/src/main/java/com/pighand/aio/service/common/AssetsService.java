package com.pighand.aio.service.common;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.common.AssetsDomain;
import com.pighand.aio.vo.common.AssetsVO;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
public interface AssetsService extends BaseService<AssetsDomain> {

    /**
     * 创建
     *
     * @param comAssetsVO
     * @return
     */
    AssetsVO create(AssetsVO comAssetsVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    AssetsDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param comAssetsVO
     * @return PageOrList<ComAssetsVO>
     */
    PageOrList<AssetsVO> query(AssetsVO comAssetsVO);

    /**
     * 修改
     *
     * @param comAssetsVO
     */
    void update(AssetsVO comAssetsVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
