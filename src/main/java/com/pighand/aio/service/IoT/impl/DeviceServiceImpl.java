package com.pighand.aio.service.IoT.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.mapper.IoT.DeviceMapper;
import com.pighand.aio.mapper.IoT.DeviceTaskMapper;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.vo.IoT.DeviceVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Service
@AllArgsConstructor
public class DeviceServiceImpl extends BaseServiceImpl<DeviceMapper, DeviceDomain> implements DeviceService {

    private final DeviceTaskMapper deviceTaskMapper;

    /**
     * 创建
     *
     * @param deviceVO
     * @return
     */
    @Override
    public DeviceVO create(DeviceVO deviceVO) {
        super.mapper.insert(deviceVO);

        return deviceVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public DeviceDomain find(Long id) {
        //        Set<String> joinTables = List.of(DEVICE_TASK.getTableName());

        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param deviceVO
     * @return PageOrList<DeviceVO>
     */
    @Override
    public PageOrList<DeviceVO> query(DeviceVO deviceVO) {
        //        deviceVO.setJoinTables(DEVICE_TASK.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(DEVICE.SN.like(deviceVO.getSn(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(DEVICE.GROUP.eq(deviceVO.getGroup(), VerifyUtils::isNotEmpty));
        queryWrapper.and(DEVICE.LINK_STATUS.eq(deviceVO.getLinkStatus(), VerifyUtils::isNotEmpty));
        queryWrapper.and(DEVICE.RUNNING_STATUS.eq(deviceVO.getRunningStatus(), VerifyUtils::isNotEmpty));

        PageOrList<DeviceVO> result = super.mapper.query(deviceVO, queryWrapper);

        //        // 获取当前时间的前3天
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.add(Calendar.DAY_OF_MONTH, -3);
        //        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        //        String threeDaysAgo = dateFormat.format(calendar.getTime());
        //
        //        // 查询3天内是否已打卡
        //
        //        List<DeviceTaskDomain> tasks = deviceTaskMapper.selectListByQuery(
        //            QueryWrapper.create().select(DEVICE_TASK.DEVICE_ID)
        //                .where(DEVICE_TASK.CREATOR_ID.eq(Context.getLoginUser().getId()))
        //                .and(DEVICE_TASK.CREATED_AT.ge(threeDaysAgo)));
        //
        //        Set<Long> deviceIds = tasks.stream().map(DeviceTaskDomain::getDeviceId).collect(Collectors.toSet());
        //
        //        result.getRecords().forEach(device -> {
        //            device.setClockIn(deviceIds.contains(device.getId()));
        //        });

        return result;
    }

    /**
     * 修改
     *
     * @param deviceVO
     */
    @Override
    public void update(DeviceVO deviceVO) {
        super.mapper.update(deviceVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 设备连接
     * 设置客户端id，更新连接状态为已连接
     *
     * @param sn
     * @param clientId
     */
    @Override
    public void link(String sn, String clientId) {
        this.updateChain().set(DEVICE.LINK_STATUS, 30).set(DEVICE.CLIENT_ID, clientId).where(DEVICE.SN.eq(sn)).update();
    }

    /**
     * 设备断开连接
     * 清空客户端id，更新连接状态为未连接，运行状态为未运行
     *
     * @param clientId
     */
    @Override
    public void unlink(String clientId) {
        this.updateChain().set(DEVICE.LINK_STATUS, 10).set(DEVICE.RUNNING_STATUS, 10).set(DEVICE.CLIENT_ID, null)
            .where(DEVICE.CLIENT_ID.eq(clientId)).update();
    }

    /**
     * 设置为空闲
     *
     * @param sn
     */
    @Override
    public void leisure(String sn) {
        this.updateChain().set(DEVICE.RUNNING_STATUS, 10).where(DEVICE.SN.eq(sn)).update();
    }
}
