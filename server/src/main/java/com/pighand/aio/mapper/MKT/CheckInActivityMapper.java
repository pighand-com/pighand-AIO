package com.pighand.aio.mapper.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.CheckInActivityDomain;
import com.pighand.aio.vo.MKT.CheckInActivityVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 营销 - 打卡活动
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper
public interface CheckInActivityMapper extends BaseMapper<CheckInActivityDomain> {

    /**
     * 基础查询条件
     *
     * @param checkInActivityDomain
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @param joinTables 关联表
     * @param result     查询结果
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<CheckInActivityVO> checkInActivityVOList = new ArrayList<>();
        if (isList) {
            checkInActivityVOList = (List<CheckInActivityVO>) result;
        } else {
            checkInActivityVOList.add((CheckInActivityVO) result);
        }

        // 这里可以添加一对多关联查询逻辑
    }

    /**
     * 查询详情
     *
     * @param id
     * @param joinTables 关联表
     * @return
     */
    default CheckInActivityVO find(Long id, String... joinTables) {
        QueryWrapper queryWrapper = this.relationOne(Stream.of(joinTables).collect(Collectors.toSet()),
            QueryWrapper.create().where("id = ?", id));

        CheckInActivityVO result = this.selectOneByQueryAs(queryWrapper, CheckInActivityVO.class);
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
    default CheckInActivityVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        queryWrapper = this.relationOne(joinTableSet, queryWrapper);

        CheckInActivityVO result = this.selectOneByQueryAs(queryWrapper, CheckInActivityVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表查询
     *
     * @param checkInActivityDomain
     * @param queryWrapper
     * @return
     */
    default PageOrList<CheckInActivityVO> query(CheckInActivityDomain checkInActivityDomain,
        QueryWrapper queryWrapper) {
        queryWrapper = this.relationOne(checkInActivityDomain.getJoinTables(), queryWrapper);

        PageOrList<CheckInActivityVO> result = this.page(checkInActivityDomain, queryWrapper, CheckInActivityVO.class);
        this.relationMany(checkInActivityDomain.getJoinTables(), result.getRecords());

        return result;
    }
}