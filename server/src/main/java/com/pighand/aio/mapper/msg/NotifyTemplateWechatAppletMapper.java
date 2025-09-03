package com.pighand.aio.mapper.msg;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.msg.NotifyTemplateWechatAppletDomain;
import com.pighand.aio.vo.msg.NotifyTemplateWechatAppletVO;
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

import static com.pighand.aio.domain.msg.table.NotifyTemplateWechatAppletTableDef.NOTIFY_TEMPLATE_WECHAT_APPLET;

/**
 * 通知 - 模板 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 18:35:39
 */
@Mapper
public interface NotifyTemplateWechatAppletMapper extends BaseMapper<NotifyTemplateWechatAppletDomain> {

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

        List<Function<NotifyTemplateWechatAppletVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<NotifyTemplateWechatAppletVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((NotifyTemplateWechatAppletVO)result, mainIdGetters, subTableQueriesSingle,
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
    default NotifyTemplateWechatAppletVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(NOTIFY_TEMPLATE_WECHAT_APPLET.ID.eq(id));

        NotifyTemplateWechatAppletVO result = this.selectOneByQueryAs(queryWrapper, NotifyTemplateWechatAppletVO.class);
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
    default NotifyTemplateWechatAppletVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        NotifyTemplateWechatAppletVO result =
            this.selectOneByQueryAs(finalQueryWrapper, NotifyTemplateWechatAppletVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param notifyTemplateWechatAppletDomain
     * @return
     */
    default PageOrList<NotifyTemplateWechatAppletVO> query(
        NotifyTemplateWechatAppletDomain notifyTemplateWechatAppletDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper =
            this.relationOne(notifyTemplateWechatAppletDomain.getJoinTables(), queryWrapper);

        PageOrList<NotifyTemplateWechatAppletVO> result =
            this.page(notifyTemplateWechatAppletDomain, finalQueryWrapper, NotifyTemplateWechatAppletVO.class);
        this.relationMany(notifyTemplateWechatAppletDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
