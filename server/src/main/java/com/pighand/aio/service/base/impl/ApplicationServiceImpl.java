package com.pighand.aio.service.base.impl;

import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.mapper.base.ApplicationMapper;
import com.pighand.aio.service.base.ApplicationService;
import com.pighand.aio.vo.base.ApplicationVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<ApplicationMapper, ApplicationDomain>
    implements ApplicationService {

    /**
     * 创建
     *
     * @param projectVO
     * @return
     */
    @Override
    public ApplicationVO create(ApplicationVO projectVO) {
        super.mapper.insert(projectVO);

        return projectVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ApplicationDomain find(Long id) {
        ApplicationDomain projectDomain = super.mapper.selectOneById(id);
        return projectDomain;
    }

    /**
     * 分页或列表
     *
     * @param projectVO
     */
    @Override
    public PageOrList<ApplicationVO> query(ApplicationVO projectVO) {
        //        PageOrList pageInfo = projectVO.pageParamOrInit(PageType.NEXT_TOKEN);
        //        return super.mapper.query(pageInfo, projectVO);
        return null;
    }

    /**
     * 修改
     *
     * @param projectVO
     */
    @Override
    public void update(ApplicationVO projectVO) {
        //        super.mapper.updateById(projectVO);
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
