package com.pighand.aio.service.base.impl;

import com.pighand.aio.domain.base.ApplicationAuthorizationDomain;
import com.pighand.aio.mapper.base.ApplicationAuthorizationMapper;
import com.pighand.aio.service.base.ApplicationAuthorizationService;
import com.pighand.aio.vo.base.ApplicationAuthorizationVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * Authorization配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
public class ApplicationAuthorizationServiceImpl
    extends BaseServiceImpl<ApplicationAuthorizationMapper, ApplicationAuthorizationDomain>
    implements ApplicationAuthorizationService {

    /**
     * 创建
     *
     * @param projectAuthorizationVO
     * @return
     */
    @Override
    public ApplicationAuthorizationVO create(ApplicationAuthorizationVO projectAuthorizationVO) {
        super.mapper.insert(projectAuthorizationVO);

        return projectAuthorizationVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ApplicationAuthorizationDomain find(Long id) {
        //        ApplicationAuthorizationDomain projectAuthorizationDomain = super.mapper.selectById(id);
        //        return projectAuthorizationDomain;
        return null;

    }

    /**
     * 分页或列表
     *
     * @param projectAuthorizationVO
     */
    @Override
    public PageOrList<ApplicationAuthorizationVO> query(ApplicationAuthorizationVO projectAuthorizationVO) {
        //        PageOrList pageInfo = projectAuthorizationVO.pageParamOrInit(PageType.NEXT_TOKEN);
        //        return super.mapper.query(pageInfo, projectAuthorizationVO);
        return null;

    }

    /**
     * 修改
     *
     * @param projectAuthorizationVO
     */
    @Override
    public void update(ApplicationAuthorizationVO projectAuthorizationVO) {
        //        super.mapper.updateById(projectAuthorizationVO);
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
