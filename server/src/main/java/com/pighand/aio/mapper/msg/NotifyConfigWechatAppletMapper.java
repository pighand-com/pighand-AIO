package com.pighand.aio.mapper.msg;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.msg.NotifyConfigWechatAppletDomain;
import com.pighand.aio.vo.msg.NotifyConfigWechatAppletVO;
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

import static com.pighand.aio.domain.msg.table.NotifyConfigWechatAppletTableDef.NOTIFY_CONFIG_WECHAT_APPLET;

/**
 * 消息 - 通知配置 - 微信小程序
 *
 * @author wangshuli
 * @createDate 2025-08-25 21:40:13
 */
@Mapper
public interface NotifyConfigWechatAppletMapper extends BaseMapper<NotifyConfigWechatAppletDomain> {

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

        List<Function<NotifyConfigWechatAppletVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<NotifyConfigWechatAppletVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((NotifyConfigWechatAppletVO)result, mainIdGetters, subTableQueriesSingle,
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
    default NotifyConfigWechatAppletVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(NOTIFY_CONFIG_WECHAT_APPLET.ID.eq(id));

        NotifyConfigWechatAppletVO result = this.selectOneByQueryAs(queryWrapper, NotifyConfigWechatAppletVO.class);
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
    default NotifyConfigWechatAppletVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        NotifyConfigWechatAppletVO result =
            this.selectOneByQueryAs(finalQueryWrapper, NotifyConfigWechatAppletVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param msgNotifyConfigWechatAppletDomain
     * @return
     */
    default PageOrList<NotifyConfigWechatAppletVO> query(
        NotifyConfigWechatAppletDomain msgNotifyConfigWechatAppletDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper =
            this.relationOne(msgNotifyConfigWechatAppletDomain.getJoinTables(), queryWrapper);

        PageOrList<NotifyConfigWechatAppletVO> result =
            this.page(msgNotifyConfigWechatAppletDomain, finalQueryWrapper, NotifyConfigWechatAppletVO.class);
        this.relationMany(msgNotifyConfigWechatAppletDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
