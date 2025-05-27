package com.pighand.aio.controller.dashboard.ECommerce;

import com.pighand.aio.service.ECommerce.GoodsSpuService;
import com.pighand.aio.vo.ECommerce.GoodsSpuVO;
import com.pighand.framework.spring.api.annotation.Delete;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@com.pighand.framework.spring.api.annotation.RestController(path = "goods", docName = "电商 - 商品")
public class GoodsController extends BaseController<GoodsSpuService> {

    /**
     * @param goodsSpuVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "goodsSpuCreate")
    public Result<GoodsSpuVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody GoodsSpuVO goodsSpuVO) {
        goodsSpuVO = super.service.create(goodsSpuVO);

        return new Result(goodsSpuVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<GoodsSpuVO> find(@PathVariable(name = "id") Long id) {
        GoodsSpuVO goodsSpuVO = super.service.find(id);

        return new Result(goodsSpuVO);
    }

    /**
     * @param goodsSpuVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "goodsSpuQuery")
    public Result<PageOrList<GoodsSpuVO>> query(GoodsSpuVO goodsSpuVO) {

        PageOrList<GoodsSpuVO> result = super.service.query(goodsSpuVO);

        return new Result(result);
    }

    /**
     * @param goodsSpuVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "goodsSpuUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody GoodsSpuVO goodsSpuVO) {
        goodsSpuVO.setId(id);
        super.service.update(goodsSpuVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}
