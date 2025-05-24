package com.pighand.aio.vo.other;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传文件地址
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadUrlVO {
    @Schema(description = "上传地址")
    private String uploadUrl;

    @Schema(description = "访问地址")
    private String url;
}
