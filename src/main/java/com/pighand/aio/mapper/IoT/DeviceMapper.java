package com.pighand.aio.mapper.IoT;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.vo.IoT.DeviceVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;
import static com.pighand.aio.domain.IoT.table.DeviceTaskTableDef.DEVICE_TASK;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper
public interface DeviceMapper extends BaseMapper<DeviceDomain> {

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

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<DeviceVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasDeviceTask = joinTables.contains(DEVICE_TASK.getTableName());

        int length = 0;

        if (hasDeviceTask) {
            length++;
        }

        Consumer<FieldQueryBuilder<DeviceVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasDeviceTask) {
            Consumer<FieldQueryBuilder<DeviceVO>> consumer = (builder) -> {
                builder.field(DeviceVO::getDeviceTask).queryWrapper(
                    device -> QueryWrapper.create().from(DEVICE_TASK).where(DEVICE_TASK.DEVICE_ID.eq(device.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default DeviceVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(DEVICE.ID.eq(id));
        Consumer<FieldQueryBuilder<DeviceVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, DeviceVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default DeviceVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<DeviceVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, DeviceVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param deviceDomain
     * @return
     */
    default PageOrList<DeviceVO> query(DeviceDomain deviceDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(deviceDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<DeviceVO>>[] relationManyBuilders = this.relationMany(deviceDomain.getJoinTables());

        return this.page(deviceDomain, finalQueryWrapper, DeviceVO.class, relationManyBuilders);
    }
}
