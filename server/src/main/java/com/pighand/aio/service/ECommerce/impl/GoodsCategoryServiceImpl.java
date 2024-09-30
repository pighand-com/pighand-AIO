package com.pighand.aio.service.ECommerce.impl;

import com.pighand.aio.domain.ECommerce.GoodsCategoryDomain;
import com.pighand.aio.mapper.ECommerce.GoodsCategoryMapper;
import com.pighand.aio.service.ECommerce.GoodsCategoryService;
import com.pighand.aio.vo.ECommerce.GoodsCategoryVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;

/**
 * 电商 - 商品类目
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Service
public class GoodsCategoryServiceImpl extends BaseServiceImpl<GoodsCategoryMapper, GoodsCategoryDomain>
    implements GoodsCategoryService {

    /**
     * 创建
     *
     * @param goodsCategoryVO
     * @return
     */
    @Override
    public GoodsCategoryVO create(GoodsCategoryVO goodsCategoryVO) {
        super.mapper.insert(goodsCategoryVO);

        return goodsCategoryVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public GoodsCategoryDomain find(Long id) {
        return super.mapper.find(id, GOODS_SPU.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param goodsCategoryVO
     * @return PageOrList<GoodsCategoryVO>
     */
    @Override
    public PageOrList<GoodsCategoryVO> query(GoodsCategoryVO goodsCategoryVO) {
        goodsCategoryVO.setJoinTables(GOODS_SPU.getTableName());

        return super.mapper.query(goodsCategoryVO, null);
    }

    /**
     * 修改
     *
     * @param goodsCategoryVO
     */
    @Override
    public void update(GoodsCategoryVO goodsCategoryVO) {
        super.mapper.update(goodsCategoryVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
