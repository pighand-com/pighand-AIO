package com.pighand.aio.controller.base;

import com.pighand.aio.service.base.UserBindService;
import com.pighand.aio.vo.base.UserBindVO;
import com.pighand.framework.spring.api.annotation.Delete;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@RequiredArgsConstructor
@RestController(path = "user/bind", docName = "用户 - 绑定信息")
public class UserBindController extends BaseController<UserBindService> {

    /**
     * @param userBindVO
     * @return
     */
    @Post(docSummary = "绑定")
    public Result<UserBindVO> bind(@RequestBody UserBindVO userBindVO) {
        super.service.bind(userBindVO.getBindUserId());

        return new Result(userBindVO);
    }

    /**
     * @param unbindUserId
     */
    @Delete(docSummary = "取消绑定")
    public Result delete(Long unbindUserId) {
        super.service.unbind(unbindUserId);
        return new Result();
    }
}
