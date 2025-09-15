package com.pighand.aio.mapper.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInUserDomain;
import com.pighand.aio.vo.MKT.CheckInUserVO;
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

import static com.pighand.aio.domain.MKT.table.CheckInUserTableDef.CHECK_IN_USER;
import static com.pighand.aio.domain.MKT.table.CheckInLocationTableDef.CHECK_IN_LOCATION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 营销 - 打卡用户参与信息
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper
public interface CheckInUserMapper extends BaseMapper<CheckInUserDomain> {

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

        // CHECK_IN_LOCATION (已移除关联)
        if (joinTables.contains("check_in_location")) {
            // 不再支持地点关联查询
            joinTables.remove("check_in_location");
        }

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(CHECK_IN_USER.USER_ID));
            joinTables.remove(USER.getTableName());
        }

        // USER_EXTENSION
        if (joinTables.contains(USER_EXTENSION.getTableName())) {
            queryWrapper.leftJoin(USER_EXTENSION).on(USER_EXTENSION.ID.eq(CHECK_IN_USER.USER_ID));
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

        List<CheckInUserVO> checkInUserVOList = new ArrayList<>();
        if (isList) {
            checkInUserVOList = (List<CheckInUserVO>) result;
        } else {
            checkInUserVOList.add((CheckInUserVO) result);
        }
    }

    /**
     * 查询详情
     *
     * @param id
     * @param joinTables 关联表
     * @return
     */
    default CheckInUserVO find(Long id, String... joinTables) {
        QueryWrapper queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()),
            QueryWrapper.create().where(CHECK_IN_USER.ID.eq(id)));

        CheckInUserVO result = this.selectOneByQueryAs(queryWrapper, CheckInUserVO.class);
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
    default CheckInUserVO find(QueryWrapper queryWrapper, String... joinTables) {
        queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()), queryWrapper);

        CheckInUserVO result = this.selectOneByQueryAs(queryWrapper, CheckInUserVO.class);
        this.relationMany(Stream.of(joinTables).collect(Collectors.toSet()), result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param checkInUserDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<CheckInUserVO> query(CheckInUserDomain checkInUserDomain,
        QueryWrapper queryWrapper) {
        queryWrapper = this.relationOne(checkInUserDomain.getJoinTables(), queryWrapper);

        PageOrList<CheckInUserVO> result = this.page(checkInUserDomain, queryWrapper, CheckInUserVO.class);
        this.relationMany(checkInUserDomain.getJoinTables(), result.getRecords());

        return result;
    }
}