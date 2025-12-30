package com.pighand.aio.service.system;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.system.SimpleSchedulingDomain;
import com.pighand.aio.mapper.system.SimpleSchedulingMapper;
import com.pighand.aio.vo.system.SimpleSchedulingVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.system.table.SimpleSchedulingTableDef.SIMPLE_SCHEDULING;

/**
 * 简单定时器
 *
 * @author wangshuli
 * @createDate 2024-04-16 11:44:49
 */
@Service
public class SimpleSchedulingService extends BaseServiceImpl<SimpleSchedulingMapper, SimpleSchedulingDomain>
     {

    /**
     * 创建
     *
     * @param simpleSchedulingVO
     * @return
     */
    public SimpleSchedulingVO create(SimpleSchedulingVO simpleSchedulingVO) {
        super.mapper.insert(simpleSchedulingVO);

        return simpleSchedulingVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public SimpleSchedulingDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param simpleSchedulingVO
     * @return PageOrList<SimpleSchedulingVO>
     */
    public PageOrList<SimpleSchedulingVO> query(SimpleSchedulingVO simpleSchedulingVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(SIMPLE_SCHEDULING.CORN.like(simpleSchedulingVO.getCorn()))
            .and(SIMPLE_SCHEDULING.DESCRIPTION.like(simpleSchedulingVO.getDescription()))

            // equal
            .and(SIMPLE_SCHEDULING.ENABLE.eq(simpleSchedulingVO.getEnable()));

        return super.mapper.query(simpleSchedulingVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param simpleSchedulingVO
     */
    public void update(SimpleSchedulingVO simpleSchedulingVO) {
        super.mapper.update(simpleSchedulingVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}
