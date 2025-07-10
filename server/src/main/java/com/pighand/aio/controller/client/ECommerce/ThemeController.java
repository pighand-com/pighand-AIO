package com.pighand.aio.controller.client.ECommerce;

import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.service.ECommerce.ThemeService;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@RestController(path = "client/theme", docName = "电商 - 主题")
public class ThemeController extends BaseController<ThemeService> {

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

    @Put(docSummary = "修改排队时间", value = "{id}/queue_duration")
    public Result updateQueueDuration(@PathVariable(name = "id") Long id, @RequestBody ThemeVO themeVO) {
        super.service.updateQueueDuration(id, themeVO.getQueueDuration());

        return new Result();
    }

}
