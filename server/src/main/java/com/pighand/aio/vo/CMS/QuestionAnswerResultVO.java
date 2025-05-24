package com.pighand.aio.vo.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 问题答案结果
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionAnswerResultVO {
    // 成功id
    private List<Long> correctIds;

    // 失败id
    private List<Long> incorrectIds;

    // 冷却时间
    private Long coolingTime;

    // 设备任务id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceTaskId;
}
