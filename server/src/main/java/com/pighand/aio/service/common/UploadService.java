package com.pighand.aio.service.common;

import com.pighand.aio.vo.other.UploadRequestVO;
import com.pighand.aio.vo.other.UploadResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上传服务接口
 * <p>
 * 该接口定义了两个方法，分别用于通过认证URL上传和通过本地文件上传。
 */
public interface UploadService {

    String filePath(String extension, String path, Integer index);

    /*
     * 根据url获取桶中的文件key
     */
    String fileKeyByUrl(String fileUrl);

    /**
     * 通过认证URL上传文件
     *
     * @param uploads 上传文件的相关信息，包括URL、文件名等
     * @return 上传成功后返回的文件访问路径
     */
    UploadResponseVO byAuthUrl(List<UploadRequestVO> uploads);

    /**
     * 将临时文件改为正式文件
     *
     * @param fileUrl
     * @return
     */
    void updateFileOfficial(String fileUrl);

    /**
     * 通过本地文件上传
     *
     * @param files 上传的本地文件数组
     * @return 上传成功后返回的文件访问路径
     */
    List<String> byLocal(MultipartFile[] files);
}
