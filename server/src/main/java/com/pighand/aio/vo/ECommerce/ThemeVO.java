package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Data
@TableRef(ThemeDomain.class)
@EqualsAndHashCode(callSuper = false)
public class ThemeVO extends ThemeDomain {

    // relation table: begin
    //    private SessionVO session;
    //    private SessionTemplateVO sessionTemplate;
    // relation table: end

    private List<SessionVO> session;

    private List<SessionTemplateVO> sessionTemplate;
}
