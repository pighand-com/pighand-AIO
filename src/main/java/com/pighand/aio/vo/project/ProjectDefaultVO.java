package com.pighand.aio.vo.project;

import com.pighand.aio.domain.project.ProjectDefaultDomain;
import lombok.Data;

/**
 * 项目默认设置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
public class ProjectDefaultVO extends ProjectDefaultDomain {
    private String randomProfile;
}
