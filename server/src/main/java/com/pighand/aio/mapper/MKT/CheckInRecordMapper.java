package com.pighand.aio.mapper.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import com.pighand.aio.vo.MKT.CheckInRecordVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pighand.aio.domain.MKT.table.CheckInRecordTableDef.CHECK_IN_RECORD;
import static com.pighand.aio.domain.MKT.table.CheckInLocationTableDef.CHECK_IN_LOCATION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 营销 - 打卡记录
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper
public interface CheckInRecordMapper extends BaseMapper<CheckInRecordDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null || joinTables.isEmpty()) {
            return queryWrapper;
        }

        // CHECK_IN_LOCATION
        if (joinTables.contains(CHECK_IN_LOCATION.getTableName())) {
            queryWrapper.leftJoin(CHECK_IN_LOCATION).on(CHECK_IN_LOCATION.ID.eq(CHECK_IN_RECORD.LOCALTION_ID));
            joinTables.remove(CHECK_IN_LOCATION.getTableName());
        }

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(CHECK_IN_RECORD.USER_ID));
            joinTables.remove(USER.getTableName());
        }

        // USER_EXTENSION
        if (joinTables.contains(USER_EXTENSION.getTableName())) {
            queryWrapper.leftJoin(USER_EXTENSION).on(USER_EXTENSION.ID.eq(CHECK_IN_RECORD.USER_ID));
            joinTables.remove(USER_EXTENSION.getTableName());
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<CheckInRecordVO> checkInRecordVOList = new ArrayList<>();
        if (isList) {
            checkInRecordVOList = (List<CheckInRecordVO>) result;
        } else {
            checkInRecordVOList.add((CheckInRecordVO) result);
        }
    }

    /**
     * 查询详情
     *
     * @param id
     * @param joinTables 关联表
     * @return
     */
    default CheckInRecordVO find(Long id, String... joinTables) {
        QueryWrapper queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()),
            QueryWrapper.create().where(CHECK_IN_RECORD.ID.eq(id)));

        CheckInRecordVO result = this.selectOneByQueryAs(queryWrapper, CheckInRecordVO.class);
        this.relationMany(Stream.of(joinTables).collect(Collectors.toSet()), result);

        return result;
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default CheckInRecordVO find(QueryWrapper queryWrapper, String... joinTables) {
        queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()), queryWrapper);

        CheckInRecordVO result = this.selectOneByQueryAs(queryWrapper, CheckInRecordVO.class);
        this.relationMany(Stream.of(joinTables).collect(Collectors.toSet()), result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param checkInRecordDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<CheckInRecordVO> query(CheckInRecordDomain checkInRecordDomain,
        QueryWrapper queryWrapper) {
        queryWrapper = this.relationOne(checkInRecordDomain.getJoinTables(), queryWrapper);

        PageOrList<CheckInRecordVO> result = this.page(checkInRecordDomain, queryWrapper, CheckInRecordVO.class);
        this.relationMany(checkInRecordDomain.getJoinTables(), result.getRecords());

        return result;
    }
}