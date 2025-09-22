package com.pighand.aio.vo.MKT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.CheckInActivityDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销 - 打卡活动
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Data
@TableRef(CheckInActivityDomain.class)
@EqualsAndHashCode(callSuper = false)
public class CheckInActivityVO extends CheckInActivityDomain {

    // 继承自CheckInActivityDomain，无需重复定义字段
    // 可以在这里添加额外的业务字段或查询条件
}