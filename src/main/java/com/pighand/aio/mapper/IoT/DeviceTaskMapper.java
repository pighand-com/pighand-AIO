package com.pighand.aio.mapper.IoT;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

import static com.mybatisflex.core.query.QueryMethods.column;
import static com.mybatisflex.core.query.QueryMethods.min;
import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;
import static com.pighand.aio.domain.IoT.table.DeviceTaskTableDef.DEVICE_TASK;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper
public interface DeviceTaskMapper extends BaseMapper<DeviceTaskDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(List<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null) {
            return queryWrapper;
        }

        if (joinTables.contains(DEVICE.getTableName())) {
            queryWrapper.leftJoin(DEVICE).on(DEVICE.ID.eq(DEVICE_TASK.DEVICE_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<DeviceTaskVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<DeviceTaskVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default DeviceTaskVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(DEVICE_TASK.ID.eq(id));
        Consumer<FieldQueryBuilder<DeviceTaskVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, DeviceTaskVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default DeviceTaskVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<DeviceTaskVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, DeviceTaskVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param deviceTaskDomain
     * @return
     */
    default PageOrList<DeviceTaskVO> query(DeviceTaskDomain deviceTaskDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(deviceTaskDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<DeviceTaskVO>>[] relationManyBuilders =
            this.relationMany(deviceTaskDomain.getJoinTables());

        return this.page(deviceTaskDomain, finalQueryWrapper, DeviceTaskVO.class, relationManyBuilders);
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
