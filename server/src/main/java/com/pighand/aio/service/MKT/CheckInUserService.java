package com.pighand.aio.service.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInUserDomain;
import com.pighand.aio.mapper.MKT.CheckInUserMapper;
import com.pighand.aio.vo.MKT.CheckInUserVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.pighand.aio.domain.MKT.table.CheckInUserTableDef.CHECK_IN_USER;

/**
 * 营销 - 打卡用户参与信息
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Service
@RequiredArgsConstructor
public class CheckInUserService extends BaseServiceImpl<CheckInUserMapper, CheckInUserDomain> {

    /**
     * 创建打卡用户参与信息
     *
     * @param checkInUserVO
     * @return
     */
    public CheckInUserVO create(CheckInUserVO checkInUserVO) {
        super.mapper.insert(checkInUserVO);
        return checkInUserVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public CheckInUserDomain find(Long id) {
        return super.mapper.selectOneById(id);
    }

    /**
     * 分页或列表
     *
     * @param checkInUserVO
     * @return
     */
    public PageOrList<CheckInUserVO> query(CheckInUserVO checkInUserVO) {
        QueryWrapper queryWrapper = QueryWrapper.create().select(CHECK_IN_USER.DEFAULT_COLUMNS)
            .and(CHECK_IN_USER.USER_ID.eq(checkInUserVO.getUserId()));

        return super.mapper.query(checkInUserVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param checkInUserVO
     * @return
     */
    public CheckInUserVO update(CheckInUserVO checkInUserVO) {
        super.mapper.update(checkInUserVO);
        return checkInUserVO;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 查询用户在指定地点的参与信息（已废弃，使用findByUser代替）
     *
     * @param locationId 地点ID
     * @param userId     用户ID
     * @return
     */
    @Deprecated
    public CheckInUserDomain findByLocationAndUser(Long locationId, Long userId) {
        return findByUser(userId);
    }

    /**
     * 查询用户的参与信息（不限地点）
     *
     * @param userId 用户ID
     * @return
     */
    public CheckInUserVO findByUser(Long userId) {
        return super.queryChain().where(CHECK_IN_USER.USER_ID.eq(userId)).oneAs(CheckInUserVO.class);
    }

    /**
     * 延长用户打卡截止时间（已废弃，使用单参数版本代替）
     *
     * @param locationId 地点ID
     * @param userId     用户ID
     * @param endTime    新的截止时间
     */
    @Deprecated
    public void extendEndTime(Long locationId, Long userId, LocalDateTime endTime) {
        extendEndTime(userId, endTime);
    }

    /**
     * 延长用户打卡截止时间（不限地点）
     *
     * @param userId  用户ID
     * @param endTime 新的截止时间
     */
    public void extendEndTime(Long userId, LocalDateTime endTime) {
        super.updateChain().set(CHECK_IN_USER.END_TIME, endTime).where(CHECK_IN_USER.USER_ID.eq(userId)).update();
    }
}