package com.pighand.aio.vo.base;

import com.pighand.aio.domain.base.ApplicationDefaultDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目默认设置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationDefaultVO extends ApplicationDefaultDomain {
    private String randomProfile;
}
