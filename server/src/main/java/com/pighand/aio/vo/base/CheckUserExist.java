package com.pighand.aio.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 检查用户是否存在
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class CheckUserExist {
    private boolean isUserNameExist;

    private boolean isPhoneExist;

    private boolean isEmailExist;
}
