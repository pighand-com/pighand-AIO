package com.pighand.aio.mapper.IoT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.aio.vo.IoT.DeviceVO;
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

import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;
import static com.pighand.aio.domain.IoT.table.DeviceTaskTableDef.DEVICE_TASK;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface DeviceMapper extends BaseMapper<DeviceDomain> {

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

        List<Function<DeviceVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<DeviceVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // DEVICE_TASK
        if (joinTables.contains(DEVICE_TASK.getTableName())) {
            mainIdGetters.add(DeviceVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new DeviceTaskDomain().select(DEVICE_TASK.DEFAULT_COLUMNS)
                    .where(DEVICE_TASK.DEVICE_ID.in(ids)).listAs(DeviceTaskVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new DeviceTaskDomain().select(DEVICE_TASK.DEFAULT_COLUMNS).where(DEVICE_TASK.DEVICE_ID.eq(id))
                        .listAs(DeviceTaskVO.class));
            }

            subTableIdGetter.add(obj -> ((DeviceTaskVO)obj).getDeviceId());
            subResultSetter.add((vo, list) -> vo.setDeviceTask((List<DeviceTaskVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((DeviceVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default DeviceVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(DEVICE.ID.eq(id));

        DeviceVO result = this.selectOneByQueryAs(queryWrapper, DeviceVO.class);
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
    default DeviceVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        DeviceVO result = this.selectOneByQueryAs(finalQueryWrapper, DeviceVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param deviceDomain
     * @return
     */
    default PageOrList<DeviceVO> query(DeviceDomain deviceDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(deviceDomain.getJoinTables(), queryWrapper);

        PageOrList<DeviceVO> result = this.page(deviceDomain, finalQueryWrapper, DeviceVO.class);
        this.relationMany(deviceDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
