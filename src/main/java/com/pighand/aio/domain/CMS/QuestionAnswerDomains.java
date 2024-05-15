package com.pighand.aio.domain.CMS;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Data
public class QuestionAnswerDomains implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long questionSetId;
    @Schema(description = "答案 - 文本")
    private String answerText;
}
