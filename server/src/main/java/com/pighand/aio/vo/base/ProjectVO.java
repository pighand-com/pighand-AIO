package com.pighand.aio.vo.base;

import com.pighand.aio.domain.base.ProjectDomain;
import lombok.Data;

import java.util.List;

/**
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
public class ProjectVO extends ProjectDomain {

    private List<ProjectPlatformKeyVO> projectPlatformKey;
}
