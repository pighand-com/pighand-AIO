package com.pighand.aio.service.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInLocationDomain;
import com.pighand.aio.mapper.MKT.CheckInLocationMapper;
import com.pighand.aio.vo.MKT.CheckInLocationVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.MKT.table.CheckInLocationTableDef.CHECK_IN_LOCATION;

/**
 * 营销 - 打卡地点
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Service
@RequiredArgsConstructor
public class CheckInLocationService extends BaseServiceImpl<CheckInLocationMapper, CheckInLocationDomain> {

    /**
     * 创建打卡地点
     *
     * @param checkInLocationVO
     * @return
     */
    public CheckInLocationVO create(CheckInLocationVO checkInLocationVO) {
        super.mapper.insert(checkInLocationVO);
        return checkInLocationVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public CheckInLocationDomain find(Long id) {
        return super.mapper.selectOneById(id);
    }

    /**
     * 分页或列表
     *
     * @param checkInLocationVO
     * @return
     */
    public PageOrList<CheckInLocationVO> query(CheckInLocationVO checkInLocationVO) {
        QueryWrapper queryWrapper = QueryWrapper.create().select(CHECK_IN_LOCATION.DEFAULT_COLUMNS)
            // 根据应用ID查询
            .and(CHECK_IN_LOCATION.APPLICATION_ID.eq(checkInLocationVO.getApplicationId()))
            // 根据店铺ID查询
            .and(CHECK_IN_LOCATION.STORE_ID.eq(checkInLocationVO.getStoreId()))
            // 根据名称模糊查询
            .and(CHECK_IN_LOCATION.NAME.like(checkInLocationVO.getName()));
        
        return super.mapper.query(checkInLocationVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param checkInLocationVO
     * @return
     */
    public CheckInLocationVO update(CheckInLocationVO checkInLocationVO) {
        super.mapper.update(checkInLocationVO);
        return checkInLocationVO;
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