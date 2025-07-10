package com.pighand.aio.vo.system;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.system.SimpleSchedulingDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableRef(SimpleSchedulingDomain.class)
@EqualsAndHashCode(callSuper = false)
public class SimpleSchedulingVO extends SimpleSchedulingDomain {
}
