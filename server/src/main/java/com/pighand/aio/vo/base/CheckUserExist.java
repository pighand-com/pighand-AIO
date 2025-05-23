package com.pighand.aio.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 检查用户是否存在
 */
@Data
@AllArgsConstructor
public class CheckUserExist {
    private boolean isUserNameExist;

    private boolean isPhoneExist;

    private boolean isEmailExist;
}
