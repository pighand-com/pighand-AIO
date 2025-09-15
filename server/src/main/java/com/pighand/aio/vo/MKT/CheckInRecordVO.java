package com.pighand.aio.vo.MKT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销 - 打卡签到记录
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Data
@TableRef(CheckInRecordDomain.class)
@EqualsAndHashCode(callSuper = false)
public class CheckInRecordVO extends CheckInRecordDomain {
    // 继承自CheckInRecordDomain，无需重复定义字段
    // 可以在这里添加额外的业务字段
}