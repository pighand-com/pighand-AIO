package com.pighand.aio.service.MKT;

import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import com.pighand.aio.mapper.MKT.CheckInRecordMapper;
import com.pighand.aio.vo.MKT.CheckInRecordVO;
import com.pighand.aio.vo.MKT.LocationStatVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.distinct;
import static com.pighand.aio.domain.MKT.table.CheckInLocationTableDef.CHECK_IN_LOCATION;
import static com.pighand.aio.domain.MKT.table.CheckInRecordTableDef.CHECK_IN_RECORD;
import static com.pighand.aio.domain.MKT.table.CheckInUserTableDef.CHECK_IN_USER;

/**
 * 营销 - 打卡签到记录
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Service
@RequiredArgsConstructor
public class CheckInRecordService extends BaseServiceImpl<CheckInRecordMapper, CheckInRecordDomain> {

    /**
     * 创建打卡签到记录
     *
     * @param checkInRecordVO
     * @return
     */
    public CheckInRecordVO create(CheckInRecordVO checkInRecordVO) {
        super.mapper.insert(checkInRecordVO);
        return checkInRecordVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public CheckInRecordDomain find(Long id) {
        return super.mapper.selectOneById(id);
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
     * 查询用户今日在指定地点的打卡记录
     *
     * @param userId      用户ID
     * @param locationId  地点ID
     * @param checkInDate 打卡日期
     * @return
     */
    public CheckInRecordDomain findTodayRecord(Long userId, Long locationId, LocalDate checkInDate) {
        return super.queryChain().where(CHECK_IN_RECORD.USER_ID.eq(userId))
            .and(CHECK_IN_RECORD.LOCALTION_ID.eq(locationId)).and(CHECK_IN_RECORD.CHECK_IN_AT.eq(checkInDate)).one();
    }

    /**
     * 查询用户今日所有打卡记录
     *
     * @param userId 用户ID
     * @return
     */
    public List<CheckInRecordDomain> findTodayRecords(Long userId) {
        // 获取今天0点的时间
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Date startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

        // 获取今天23:59:59的时间
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        Date endDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());

        return super.queryChain().where(CHECK_IN_RECORD.USER_ID.eq(userId))
            .and(CHECK_IN_RECORD.CHECK_IN_AT.ge(startDate)).and(CHECK_IN_RECORD.CHECK_IN_AT.le(endDate)).list();
    }

    /**
     * 创建打卡记录
     *
     * @param userId      用户ID
     * @param locationId  地点ID
     * @param checkInDate 打卡日期
     * @return
     */
    public CheckInRecordDomain createCheckInRecord(Long userId, Long activityId, Long locationId,
        LocalDate checkInDate) {
        CheckInRecordDomain record = new CheckInRecordDomain();
        record.setActivityId(activityId);
        record.setUserId(userId);
        record.setLocaltionId(locationId);
        record.setCheckInAt(Date.from(checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        super.mapper.insert(record);
        return record;
    }

    public Long countDistinctUsers(LocalDate date, Long activityId) {
        LocalDateTime startOfDay = date.atStartOfDay();
        Date startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Date endDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());

        List<Long> userIds = super.queryChain().select(distinct(CHECK_IN_RECORD.USER_ID)).leftJoin(CHECK_IN_USER)
            .on(CHECK_IN_USER.USER_ID.eq(CHECK_IN_RECORD.USER_ID)).where(CHECK_IN_RECORD.CHECK_IN_AT.ge(startDate))
            .and(CHECK_IN_RECORD.CHECK_IN_AT.le(endDate)).and(CHECK_IN_USER.ACTIVITY_ID.eq(activityId))
            .listAs(Long.class);

        return (long)(userIds == null ? 0 : userIds.size());
    }

    public List<LocationStatVO> countByLocation(LocalDate date, Long activityId) {
        LocalDateTime startOfDay = date.atStartOfDay();
        Date startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Date endDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());

        QueryWrapper qw = QueryWrapper.create()
            .select(CHECK_IN_RECORD.LOCALTION_ID.as("locationId"), CHECK_IN_LOCATION.NAME.as("locationName"),
                QueryMethods.count(CHECK_IN_RECORD.ID).as("checkInCount")).from(CHECK_IN_RECORD)
            .leftJoin(CHECK_IN_LOCATION).on(CHECK_IN_LOCATION.ID.eq(CHECK_IN_RECORD.LOCALTION_ID))
            .leftJoin(CHECK_IN_USER).on(CHECK_IN_USER.USER_ID.eq(CHECK_IN_RECORD.USER_ID))
            .where(CHECK_IN_RECORD.CHECK_IN_AT.ge(startDate)).and(CHECK_IN_RECORD.CHECK_IN_AT.le(endDate))
            .and(CHECK_IN_USER.ACTIVITY_ID.eq(activityId)).groupBy(CHECK_IN_RECORD.LOCALTION_ID);

        return super.mapper.selectListByQueryAs(qw, LocationStatVO.class);
    }
}