package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserStationLetterDomain;
import com.pighand.aio.mapper.base.UserStationLetterMapper;
import com.pighand.aio.vo.base.UserStationLetterVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pighand.aio.domain.base.table.UserStationLetterDomainTableDef.USER_STATION_LETTER_DOMAIN;

/**
 * 系统 - 站内信配置
 *
 * @author wangshuli
 * @createDate 2025-12-31
 */
@Service
public class UserStationLetterService extends BaseServiceImpl<UserStationLetterMapper, UserStationLetterDomain> {

    /**
     * 根据applicationId查询
     * @param applicationId
     * @return
     */
    public List<UserStationLetterVO> queryByApplicationId(Long applicationId) {
        return this.queryChain()
                .where(USER_STATION_LETTER_DOMAIN.APPLICATION_ID.eq(applicationId))
                .listAs(UserStationLetterVO.class);
    }
}
