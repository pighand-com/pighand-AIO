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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Table("cms_question_set")
@Data
public class QuestionSetDomain extends BaseDomainRecord<QuestionSetDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("questionSetCreate")
    @RequestFieldException("questionSetUpdate")
    private Long id;
    private Long questionBankId;
    @Column("type")
    @Schema(description = "题目类型 10文本回答 20单选 21多选")
    private Integer type;
    @Schema(description = "所属分页")
    private Integer page;
    @Length(max = 255)
    @Schema(description = "题目")
    private String question;
    @Length(max = 255)
    @Schema(description = "提示信息")
    private String prompt;
    @Schema(description = "选项 json数组结构", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<String> options;
    @Schema(description = "选择题的答案索引", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Integer> answer;
}
