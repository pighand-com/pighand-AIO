package com.pighand.aio.common.utils;

import com.mybatisflex.core.keygen.impl.SnowFlakeIDKeyGenerator;
import com.pighand.aio.common.enums.BillTypeEnum;
import com.pighand.aio.common.enums.TableIdEnum;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ID生成器
 */
public class IDGenerator {
    /**
     * 生成订单号
     *
     * @param tableIdEnum  业务类型
     * @param billTypeEnum
     * @return
     */
    public static String generate(TableIdEnum tableIdEnum, BillTypeEnum billTypeEnum) {
        Long id = null;
        try {
            SnowFlakeIDKeyGenerator keyGenerator = new SnowFlakeIDKeyGenerator(InetAddress.getByName("127.0.0.1"));
            id = keyGenerator.nextId();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        String builder = String.valueOf(tableIdEnum.value) + billTypeEnum.value + id;

        return builder;
    }
}
