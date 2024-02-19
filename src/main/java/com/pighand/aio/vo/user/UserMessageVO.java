package com.pighand.aio.vo.user;

import com.mybatisflex.annotation.ColumnAlias;
import com.pighand.aio.domain.user.UserMessageDomain;
import lombok.Data;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class UserMessageVO extends UserMessageDomain {
    @ColumnAlias("sender")
    private String sender;

    @ColumnAlias("receiver")
    private String receiver;
}
