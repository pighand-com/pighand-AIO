package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.common.sdk.tencentCloud.TencentCloudSDK;
import com.pighand.aio.domain.CMS.AssetsDomain;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.mapper.CMS.AssetsMapper;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.CMS.AssetsVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import com.qcloud.cos.model.DeleteObjectsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.CMS.table.AssetsTableDef.ASSETS;
import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Service
@RequiredArgsConstructor
public class AssetsService extends BaseServiceImpl<AssetsMapper, AssetsDomain>  {

    private final UploadService uploadService;

    private final ApplicationPlatformKeyService projectPlatformKeyService;

    private final TencentCloudSDK tencentCloudSDK;

    /**
     * 创建。支持同时上传多个文件
     *
     * @param comAssetsVO
     * @return
     */
    public AssetsVO create(AssetsVO comAssetsVO) {
        List<String> urls = comAssetsVO.getUrls();
        if (urls.size() == 1) {
            String url = urls.get(0);
            uploadService.updateFileOfficial(url);

            comAssetsVO.setUrl(url);
            super.mapper.insert(comAssetsVO);
        } else {
            List<AssetsDomain> assets = new ArrayList<>(urls.size());
            for (String url : urls) {
                uploadService.updateFileOfficial(url);

                AssetsDomain assetsDomain = new AssetsDomain();
                assetsDomain.setTag(comAssetsVO.getTag());
                assetsDomain.setUrl(url);
                assets.add(assetsDomain);
            }
            super.mapper.insertBatch(assets);
        }

        return comAssetsVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public AssetsDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param comAssetsVO
     * @return PageOrList<ComAssetsVO>
     */
    public PageOrList<AssetsVO> query(AssetsVO comAssetsVO) {
        comAssetsVO.setJoinTables(APPLICATION.getName(), USER_EXTENSION.getName());

        QueryWrapper queryWrapper = QueryWrapper.create().select(ASSETS.DEFAULT_COLUMNS)

            // like
            .and(ASSETS.TAG.like(comAssetsVO.getTag())).and(ASSETS.URL.like(comAssetsVO.getUrl()));

        return super.mapper.query(comAssetsVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param comAssetsVO
     */
    public void update(AssetsVO comAssetsVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS.ID.eq(comAssetsVO.getId()));

        updateChain.set(ASSETS.TAG, comAssetsVO.getTag(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        AssetsDomain assets = super.queryChain().select(ASSETS.URL).where(ASSETS.ID.eq(id)).one();

        if (assets == null) {
            return;
        }

        // 删除COS文件
        ApplicationPlatformKeyDomain applicationPlatformKey = projectPlatformKeyService.uploadKey();
        String key = uploadService.fileKeyByUrl(assets.getUrl());

        tencentCloudSDK.cosClient(applicationPlatformKey.getAppid(), applicationPlatformKey.getSecret(),
            applicationPlatformKey.getRegion()).deleteObject(applicationPlatformKey.getBucket(), key);

        super.mapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void batchDelete(List<Long> ids) {
        List<AssetsDomain> assets = super.queryChain().select(ASSETS.ID, ASSETS.URL).where(ASSETS.ID.in(ids)).list();

        List<DeleteObjectsRequest.KeyVersion> deleteKeys = new ArrayList<>(assets.size());
        List<Long> deleteIds = new ArrayList<>(assets.size());

        // 删除COS文件
        for (AssetsDomain assetsDomain : assets) {
            String key = uploadService.fileKeyByUrl(assetsDomain.getUrl());
            DeleteObjectsRequest.KeyVersion keyVersion = new DeleteObjectsRequest.KeyVersion(key);
            deleteKeys.add(keyVersion);

            deleteIds.add(assetsDomain.getId());
        }

        ApplicationPlatformKeyDomain applicationPlatformKey = projectPlatformKeyService.uploadKey();

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(applicationPlatformKey.getBucket());
        deleteObjectsRequest.setKeys(deleteKeys);
        deleteObjectsRequest.setKeys(deleteKeys);

        tencentCloudSDK.cosClient(applicationPlatformKey.getAppid(), applicationPlatformKey.getSecret(),
            applicationPlatformKey.getRegion()).deleteObjects(deleteObjectsRequest);

        // 删除数据库
        super.mapper.deleteBatchByIds(deleteIds);
    }
}
