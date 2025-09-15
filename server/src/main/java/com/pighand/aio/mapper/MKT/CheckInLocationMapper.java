package com.pighand.aio.mapper.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInLocationDomain;
import com.pighand.aio.vo.MKT.CheckInLocationVO;
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

import static com.pighand.aio.domain.MKT.table.CheckInLocationTableDef.CHECK_IN_LOCATION;

/**
 * 营销 - 打卡地点
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper
public interface CheckInLocationMapper extends BaseMapper<CheckInLocationDomain> {

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

        List<CheckInLocationVO> checkInLocationVOList = new ArrayList<>();
        if (isList) {
            checkInLocationVOList = (List<CheckInLocationVO>) result;
        } else {
            checkInLocationVOList.add((CheckInLocationVO) result);
        }
    }

    /**
     * 查询详情
     *
     * @param id
     * @param joinTables 关联表
     * @return
     */
    default CheckInLocationVO find(Long id, String... joinTables) {
        QueryWrapper queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()),
            QueryWrapper.create().where(CHECK_IN_LOCATION.ID.eq(id)));

        CheckInLocationVO result = this.selectOneByQueryAs(queryWrapper, CheckInLocationVO.class);
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
    default CheckInLocationVO find(QueryWrapper queryWrapper, String... joinTables) {
        queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()), queryWrapper);

        CheckInLocationVO result = this.selectOneByQueryAs(queryWrapper, CheckInLocationVO.class);
        this.relationMany(Stream.of(joinTables).collect(Collectors.toSet()), result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param checkInLocationDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<CheckInLocationVO> query(CheckInLocationDomain checkInLocationDomain,
        QueryWrapper queryWrapper) {
        queryWrapper = this.relationOne(checkInLocationDomain.getJoinTables(), queryWrapper);

        PageOrList<CheckInLocationVO> result = this.page(checkInLocationDomain, queryWrapper, CheckInLocationVO.class);
        this.relationMany(checkInLocationDomain.getJoinTables(), result.getRecords());

        return result;
    }
}