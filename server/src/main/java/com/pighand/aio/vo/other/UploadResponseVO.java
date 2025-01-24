package com.pighand.aio.vo.other;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 上传返回
 */
@Data
public class UploadResponseVO {
    private List<UploadUrlVO> urls;

    private HashMap<String, String> headers;
}
