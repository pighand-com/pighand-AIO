package com.pighand.aio.controller.ECommerce;

import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.service.ECommerce.ThemeService;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@RestController(path = "theme", docName = "电商 - 主题")
public class ThemeController extends BaseController<ThemeService> {

    /**
     * @param themeVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "themeCreate")
    public Result<ThemeVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody ThemeVO themeVO) {
        themeVO = super.service.create(themeVO);

        return new Result(themeVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ThemeDomain> find(@PathVariable(name = "id") Long id) {
        ThemeDomain themeDomain = super.service.find(id);

        return new Result(themeDomain);
    }

    /**
     * @param themeVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "themeQuery")
    public Result<PageOrList<ThemeVO>> query(ThemeVO themeVO) {
        PageOrList<ThemeVO> result = super.service.query(themeVO);

        return new Result(result);
    }

    /**
     * @param themeVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "themeUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody ThemeVO themeVO) {
        themeVO.setId(id);

        // Prohibit modifying fields
        themeVO.setId(null);

        super.service.update(themeVO);

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
