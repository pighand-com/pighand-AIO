package com.pighand.aio.service.MKT;

import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import com.pighand.aio.mapper.MKT.CheckInRecordMapper;
import com.pighand.aio.vo.MKT.CheckInRecordVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.pighand.aio.domain.MKT.table.CheckInRecordTableDef.CHECK_IN_RECORD;

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
    public CheckInRecordDomain createCheckInRecord(Long userId, Long locationId, LocalDate checkInDate) {
        CheckInRecordDomain record = new CheckInRecordDomain();
        record.setUserId(userId);
        record.setLocaltionId(locationId);
        record.setCheckInAt(Date.from(checkInDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        super.mapper.insert(record);
        return record;
    }
}