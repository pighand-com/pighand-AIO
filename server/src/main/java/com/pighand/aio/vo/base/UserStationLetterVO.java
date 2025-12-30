package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.UserStationLetterDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统 - 站内信配置
 *
 * @author wangshuli
 * @createDate 2025-12-31
 */
@Data
@TableRef(UserStationLetterDomain.class)
@EqualsAndHashCode(callSuper = false)
public class UserStationLetterVO extends UserStationLetterDomain {
}
