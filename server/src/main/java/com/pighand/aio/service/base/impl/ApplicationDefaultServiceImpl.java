package com.pighand.aio.service.base.impl;

import com.pighand.aio.domain.base.ApplicationDefaultDomain;
import com.pighand.aio.mapper.base.ApplicationDefaultMapper;
import com.pighand.aio.service.base.ApplicationDefaultService;
import com.pighand.aio.vo.base.ApplicationDefaultVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pighand.aio.domain.base.table.ProjectDefaultTableDef.PROJECT_DEFAULT;

/**
 * 项目默认设置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
public class ApplicationDefaultServiceImpl extends BaseServiceImpl<ApplicationDefaultMapper, ApplicationDefaultDomain>
    implements ApplicationDefaultService {

    /**
     * 创建
     *
     * @param projectDefaultVO
     * @return
     */
    @Override
    public ApplicationDefaultVO create(ApplicationDefaultVO projectDefaultVO) {
        super.mapper.insert(projectDefaultVO);

        return projectDefaultVO;
    }

    /**
     * 详情
     * 多个头像，自动随机一个放到randomProfile中
     *
     * @param id
     * @return
     */
    @Override
    public ApplicationDefaultVO find(Long id) {
        ApplicationDefaultVO projectDefault =
            this.queryChain().where(PROJECT_DEFAULT.ID.eq(id)).oneAs(ApplicationDefaultVO.class);
        if (projectDefault == null) {
            return new ApplicationDefaultVO();
        }

        List<String> profile = projectDefault.getDefaultProfile();
        if (profile != null && profile.size() > 0) {
            String randomProfile = profile.get((int)(Math.random() * profile.size()));
            projectDefault.setRandomProfile(randomProfile);
        }

        return projectDefault;
    }

    /**
     * 分页或列表
     *
     * @param projectDefaultVO
     */
    @Override
    public PageOrList<ApplicationDefaultVO> query(ApplicationDefaultVO projectDefaultVO) {
        //        PageOrList pageInfo = projectDefaultVO.pageParamOrInit(PageType.NEXT_TOKEN);
        //        return super.mapper.query(pageInfo, projectDefaultVO);
        return null;
    }

    /**
     * 修改
     *
     * @param projectDefaultVO
     */
    @Override
    public void update(ApplicationDefaultVO projectDefaultVO) {
        //        super.mapper.updateById(projectDefaultVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
