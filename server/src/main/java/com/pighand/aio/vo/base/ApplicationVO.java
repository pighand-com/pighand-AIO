package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.ApplicationDomain;
import lombok.Data;

import java.util.List;

/**
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@TableRef(ApplicationDomain.class)
@Data
public class ApplicationVO extends ApplicationDomain {

    private List<ApplicationPlatformKeyVO> ApplicationPlatformKey;
}
