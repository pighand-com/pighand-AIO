package com.pighand.aio.service.base.impl;

import com.pighand.aio.domain.base.ApplicationTripartitePlatformDomain;
import com.pighand.aio.mapper.base.ApplicationTripartitePlatformMapper;
import com.pighand.aio.service.base.ApplicationTripartitePlatformService;
import com.pighand.aio.vo.base.ApplicationTripartitePlatformVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * 项目三方平台配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
public class ApplicationTripartitePlatformServiceImpl
    extends BaseServiceImpl<ApplicationTripartitePlatformMapper, ApplicationTripartitePlatformDomain>
    implements ApplicationTripartitePlatformService {

    /**
     * 创建
     *
     * @param projectTripartitePlatformVO
     * @return
     */
    @Override
    public ApplicationTripartitePlatformVO create(ApplicationTripartitePlatformVO projectTripartitePlatformVO) {
        super.mapper.insert(projectTripartitePlatformVO);

        return projectTripartitePlatformVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ApplicationTripartitePlatformDomain find(Long id) {
        //        ApplicationTripartitePlatformDomain projectTripartitePlatformDomain =
        //                super.mapper.selectById(id);
        //        return projectTripartitePlatformDomain;
        return null;
    }

    /**
     * 分页或列表
     *
     * @param projectTripartitePlatformVO
     */
    @Override
    public PageOrList<ApplicationTripartitePlatformVO> query(
        ApplicationTripartitePlatformVO projectTripartitePlatformVO) {
        //        PageOrList pageInfo = projectTripartitePlatformVO.pageParamOrInit(PageType.NEXT_TOKEN);
        //        return super.mapper.query(pageInfo, projectTripartitePlatformVO);
        return null;
    }

    /**
     * 修改
     *
     * @param projectTripartitePlatformVO
     */
    @Override
    public void update(ApplicationTripartitePlatformVO projectTripartitePlatformVO) {
        //        super.mapper.updateById(projectTripartitePlatformVO);
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
