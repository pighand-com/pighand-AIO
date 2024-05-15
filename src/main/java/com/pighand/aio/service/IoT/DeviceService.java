package com.pighand.aio.service.IoT;

import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.vo.IoT.DeviceVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
public interface DeviceService extends BaseService<DeviceDomain> {

    /**
     * 创建
     *
     * @param deviceVO
     * @return
     */
    DeviceVO create(DeviceVO deviceVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DeviceDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param deviceVO
     * @return PageOrList<DeviceVO>
     */
    PageOrList<DeviceVO> query(DeviceVO deviceVO);

    /**
     * 修改
     *
     * @param deviceVO
     */
    void update(DeviceVO deviceVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 设备连接
     * 设置客户端id，更新连接状态为已连接，运行状态为未运行
     *
     * @param sn
     * @param clientId
     */
    void link(String sn, String clientId);

    /**
     * 设备断开连接
     * 清空客户端id，更新连接状态为未连接，运行状态为未运行
     *
     * @param clientId
     */
    void unlink(String clientId);

    void leisure(String sn);
}
