package com.pighand.aio.vo.other;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;

/**
 * 上传返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadResponseVO {
    private List<UploadUrlVO> urls;

    private HashMap<String, String> headers;
}
