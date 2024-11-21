package com.pighand.aio.mapper.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.vo.base.ApplicationPlatformKeyVO;
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

import static com.pighand.aio.domain.base.table.ApplicationPlatformKeyTableDef.APPLICATION_PLATFORM_KEY;
import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;

/**
 * 项目 - 三方平台appid、secret等配置
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ApplicationPlatformKeyMapper extends BaseMapper<ApplicationPlatformKeyDomain> {

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

        // APPLICATION
        if (joinTables.contains(APPLICATION.getTableName())) {
            queryWrapper.leftJoin(APPLICATION).on(APPLICATION.ID.eq(APPLICATION_PLATFORM_KEY.APPLICATION_ID));

            joinTables.remove(APPLICATION.getTableName());
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

        List<Function<ApplicationPlatformKeyVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ApplicationPlatformKeyVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((ApplicationPlatformKeyVO)result, mainIdGetters, subTableQueriesSingle,
                subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default ApplicationPlatformKeyVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(APPLICATION_PLATFORM_KEY.ID.eq(id));

        ApplicationPlatformKeyVO result = this.selectOneByQueryAs(queryWrapper, ApplicationPlatformKeyVO.class);
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
    default ApplicationPlatformKeyVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ApplicationPlatformKeyVO result = this.selectOneByQueryAs(finalQueryWrapper, ApplicationPlatformKeyVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param projectPlatformKeyDomain
     * @return
     */
    default PageOrList<ApplicationPlatformKeyVO> query(ApplicationPlatformKeyDomain projectPlatformKeyDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(projectPlatformKeyDomain.getJoinTables(), queryWrapper);

        PageOrList<ApplicationPlatformKeyVO> result =
            this.page(projectPlatformKeyDomain, finalQueryWrapper, ApplicationPlatformKeyVO.class);
        this.relationMany(projectPlatformKeyDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
