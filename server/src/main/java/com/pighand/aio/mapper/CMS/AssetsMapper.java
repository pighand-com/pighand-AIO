package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsDomain;
import com.pighand.aio.vo.CMS.AssetsVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pighand.aio.domain.CMS.table.AssetsTableDef.ASSETS;
import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Mapper
public interface AssetsMapper extends BaseMapper<AssetsDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null || joinTables.isEmpty()) {
            return queryWrapper;
        }

        if (joinTables.contains(USER_EXTENSION.getName())) {
            queryWrapper.select(USER_EXTENSION.ID, USER_EXTENSION.NAME);
            queryWrapper.leftJoin(USER_EXTENSION).on(ASSETS.CREATED_BY.eq(USER_EXTENSION.ID));

            joinTables.remove(USER_EXTENSION.getName());
        }

        if (joinTables.contains(APPLICATION.getName())) {
            queryWrapper.select(APPLICATION.ID, APPLICATION.NAME);
            queryWrapper.leftJoin(APPLICATION).on(APPLICATION.ID.eq(ASSETS.APPLICATION_ID));

            joinTables.remove(APPLICATION.getName());
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<Function<AssetsVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<AssetsVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((AssetsVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default AssetsVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ASSETS.ID.eq(id));

        AssetsVO result = this.selectOneByQueryAs(queryWrapper, AssetsVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default AssetsVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        AssetsVO result = this.selectOneByQueryAs(finalQueryWrapper, AssetsVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param comAssetsDomain
     * @return
     */
    default PageOrList<AssetsVO> query(AssetsDomain comAssetsDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(comAssetsDomain.getJoinTables(), queryWrapper);

        PageOrList<AssetsVO> result = this.page(comAssetsDomain, finalQueryWrapper, AssetsVO.class);
        this.relationMany(comAssetsDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
