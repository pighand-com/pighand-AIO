package com.pighand.aio.controller.ECommerce;

import com.pighand.aio.domain.ECommerce.BillDomain;
import com.pighand.aio.service.ECommerce.BillService;
import com.pighand.aio.vo.ECommerce.BillVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@RestController(path = "bill", docName = "商城 - 账单")
public class BillController extends BaseController<BillService> {

    /**
     * @param billVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "billCreate")
    public Result<BillVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody BillVO billVO) {
        billVO = super.service.create(billVO);

        return new Result(billVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<BillDomain> find(@PathVariable(name = "id") Long id) {
        BillDomain billDomain = super.service.find(id);

        return new Result(billDomain);
    }

    /**
     * @param billVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "billQuery")
    public Result<PageOrList<BillVO>> query(BillVO billVO) {
        PageOrList<BillVO> result = super.service.query(billVO);

        return new Result(result);
    }

    /**
     * @param billVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "billUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody BillVO billVO) {
        billVO.setId(id);

        // Prohibit modifying fields
        billVO.setCreatorId(null);
        billVO.setCreatedAt(null);

        super.service.update(billVO);

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
