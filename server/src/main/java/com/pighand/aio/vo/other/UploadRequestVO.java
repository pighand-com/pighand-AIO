package com.pighand.aio.vo.other;

import lombok.Data;

/**
 * 上传请求
 *
 * @author wangshuli
 */
@Data
public class UploadRequestVO {
    /**
     * 扩展名
     */
    private String extension;

    /**
     * 路径
     */
    private String path;
}
