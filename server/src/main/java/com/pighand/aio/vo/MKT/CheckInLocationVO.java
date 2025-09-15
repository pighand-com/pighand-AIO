package com.pighand.aio.vo.MKT;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.CheckInLocationDomain;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * 营销 - 打卡地点
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Data
@TableRef(CheckInLocationDomain.class)
@EqualsAndHashCode(callSuper = false)
public class CheckInLocationVO extends CheckInLocationDomain {

    // 继承自CheckInLocationDomain，无需重复定义字段
    // 可以在这里添加额外的业务字段
}