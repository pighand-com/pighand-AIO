package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Table("cms_question_bank")
@Data
public class QuestionBankDomain extends BaseDomainRecord<QuestionBankDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("questionBankCreate")
    @RequestFieldException("questionBankUpdate")
    private Long id;
    @Column("name")
    @Length(max = 32)
    private String name;
    @Column("description")
    @Length(max = 255)
    private String description;
    @Schema(description = "选题类型 10按顺序 20随机")
    private Integer selectType;
    @Schema(description = "随机提数 - select_type=20必填")
    private Integer randomNumber;
    @Schema(description = "频率 10一次 20不限次数-有间隔 21不限次数-无间隔")
    private Integer frequencyType;
    @Schema(description = "频率间隙（秒）。frequency_type=20时生效")
    private Long frequencyGap;
}
