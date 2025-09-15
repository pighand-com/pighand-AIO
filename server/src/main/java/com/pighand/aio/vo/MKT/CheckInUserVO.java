package com.pighand.aio.vo.MKT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.CheckInUserDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销 - 打卡用户参与信息
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Data
@TableRef(CheckInUserDomain.class)
@EqualsAndHashCode(callSuper = false)
public class CheckInUserVO extends CheckInUserDomain {
    // 继承自CheckInUserDomain，无需重复定义字段
    // 可以在这里添加额外的业务字段
}