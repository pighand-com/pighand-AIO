package com.pighand.aio.mapper.IoT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mybatisflex.core.query.QueryMethods.column;
import static com.mybatisflex.core.query.QueryMethods.min;
import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;
import static com.pighand.aio.domain.IoT.table.DeviceTaskTableDef.DEVICE_TASK;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface DeviceTaskMapper extends BaseMapper<DeviceTaskDomain> {

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

        // DEVICE
        if (joinTables.contains(DEVICE.getTableName())) {
            queryWrapper.leftJoin(DEVICE).on(DEVICE.ID.eq(DEVICE_TASK.DEVICE_ID));

            joinTables.remove(DEVICE.getTableName());
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

        List<Function<DeviceTaskVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<DeviceTaskVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((DeviceTaskVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default DeviceTaskVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(DEVICE_TASK.ID.eq(id));

        DeviceTaskVO result = this.selectOneByQueryAs(queryWrapper, DeviceTaskVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default DeviceTaskVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        DeviceTaskVO result = this.selectOneByQueryAs(finalQueryWrapper, DeviceTaskVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param deviceTaskDomain
     * @return
     */
    default PageOrList<DeviceTaskVO> query(DeviceTaskDomain deviceTaskDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(deviceTaskDomain.getJoinTables(), queryWrapper);

        PageOrList<DeviceTaskVO> result = this.page(deviceTaskDomain, finalQueryWrapper, DeviceTaskVO.class);
        this.relationMany(deviceTaskDomain.getJoinTables(), result.getRecords());

        return result;
    }

    /**
     * 查询 任务未运行 且 设备待命状态或超时 的任务
     *
     * @return
     */
    default List<DeviceTaskVO> queryNotRunningTask() {
        // 当前时间设备2分钟超时
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -2);
        //        calendar.add(Calendar.SECOND, -2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String twoMinutesAgo = dateFormat.format(calendar.getTime());

        QueryWrapper subQueryWrapper =
            QueryWrapper.create().select(DEVICE_TASK.DEVICE_ID, min(DEVICE_TASK.ID).as("minId")).from(DEVICE_TASK)
                .where(DEVICE_TASK.RUNNING_STATUS.eq(10)).groupBy(DEVICE_TASK.DEVICE_ID);

        QueryWrapper queryWrapper =
            QueryWrapper.create().select(DEVICE_TASK.ID, DEVICE_TASK.DEVICE_ID, DEVICE_TASK.MESSAGE, DEVICE.CLIENT_ID)
                .from(DEVICE_TASK).innerJoin(subQueryWrapper).as("subQuery")
                .on(DEVICE_TASK.DEVICE_ID.eq(column("subQuery.device_id"))
                    .and(DEVICE_TASK.ID.eq(column("subQuery.minId")))).innerJoin(DEVICE)
                .on(DEVICE.ID.eq(DEVICE_TASK.DEVICE_ID).and(DEVICE.LINK_STATUS.eq(30))
                    .and(DEVICE.RUNNING_STATUS.eq(10).or(DEVICE.LAST_RUNNING_AT.le(twoMinutesAgo))));

        return this.selectListByQueryAs(queryWrapper, DeviceTaskVO.class);
    }
}
