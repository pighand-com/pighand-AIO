# 实体映射器 (EntityMapper)

## 概述
实体映射器是一种用于处理实体对象之间转换的工具类，在PigHand-AIO项目中主要用于Domain对象和VO对象之间的转换。

## 命名规范
- 类名：使用 `EntityMapper` 作为后缀，例如 `UserEntityMapper`
- 方法名：使用 `toXXX` 作为前缀，例如 `toVO`、`toDomain`

## 代码结构
```java
/**
 * 用户实体映射器
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Component
public class UserEntityMapper {

    /**
     * 将Domain对象转换为VO对象
     *
     * @param domain Domain对象
     * @return VO对象
     */
    public UserVO toVO(UserDomain domain) {
        if (domain == null) {
            return null;
        }
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(domain, vo);
        return vo;
    }
    
    /**
     * 将VO对象转换为Domain对象
     *
     * @param vo VO对象
     * @return Domain对象
     */
    public UserDomain toDomain(UserVO vo) {
        if (vo == null) {
            return null;
        }
        
        UserDomain domain = new UserDomain();
        BeanUtils.copyProperties(vo, domain);
        return domain;
    }
    
    /**
     * 将Domain对象列表转换为VO对象列表
     *
     * @param domainList Domain对象列表
     * @return VO对象列表
     */
    public List<UserVO> toVOList(List<UserDomain> domainList) {
        if (domainList == null) {
            return Collections.emptyList();
        }
        
        return domainList.stream()
            .map(this::toVO)
            .collect(Collectors.toList());
    }
    
    /**
     * 将VO对象列表转换为Domain对象列表
     *
     * @param voList VO对象列表
     * @return Domain对象列表
     */
    public List<UserDomain> toDomainList(List<UserVO> voList) {
        if (voList == null) {
            return Collections.emptyList();
        }
        
        return voList.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
}
```

## 注意事项
- 使用 `@Component` 注解标记为Spring组件
- 使用 `BeanUtils.copyProperties` 进行对象属性复制
- 提供单个对象转换和列表对象转换的方法
- 处理null值情况
- 对于复杂属性，需要手动设置

## 使用示例

### 在Service中使用
```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDomain> implements UserService {

    private final UserEntityMapper entityMapper;
    
    @Override
    public UserVO find(Long id) {
        UserDomain domain = this.mapper.selectById(id);
        if (domain == null) {
            throw new ThrowPrompt("用户不存在");
        }
        
        return entityMapper.toVO(domain);
    }
    
    @Override
    public PageOrList<UserVO> query(UserVO userVO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
            .where(USER.APPLICATION_ID.eq(Context.applicationId()))
            .and(USER.STATUS.ne(UserStatusEnum.DELETED.getValue()));
            
        if (VerifyUtils.isNotEmpty(userVO.getUsername())) {
            queryWrapper.and(USER.USERNAME.like(userVO.getUsername()));
        }
        
        List<UserDomain> domainList = this.mapper.selectListByQuery(queryWrapper);
        List<UserVO> voList = entityMapper.toVOList(domainList);
        
        return PageOrList.createList(voList);
    }
}
```

## 复杂属性处理
```java
public UserVO toVO(UserDomain domain) {
    if (domain == null) {
        return null;
    }
    
    UserVO vo = new UserVO();
    BeanUtils.copyProperties(domain, vo);
    
    // 处理复杂属性
    if (domain.getStatus() != null) {
        vo.setStatusName(UserStatusEnum.getNameByValue(domain.getStatus()));
    }
    
    if (domain.getInitialSourcePlatform() != null) {
        vo.setPlatformName(PlatformEnum.getNameByValue(domain.getInitialSourcePlatform()));
    }
    
    return vo;
}
```