package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.ColumnAlias;
import com.pighand.aio.domain.base.UserMessageDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserMessageVO extends UserMessageDomain {
    @ColumnAlias("sender")
    private String sender;

    @ColumnAlias("receiver")
    private String receiver;
}
