package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Table("cms_question_answer")
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionAnswerDomain extends BaseDomainRecord<QuestionAnswerDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("questionAnswerCreate")
    @RequestFieldException("questionAnswerUpdate")
    private Long id;

    private Long questionBankId;

    private Long questionSetId;

    @Schema(description = "答案 - 文本")
    private String answerText;

    @Schema(description = "答案 - 选项索引", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Integer> answerOption;

    private Long creatorId;

    private Date createdAt;
}
