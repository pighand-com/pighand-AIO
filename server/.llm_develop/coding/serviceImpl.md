# 服务实现层 (ServiceImpl)

## 命名规范

- 类名：使用 `ServiceImpl` 作为后缀，例如 `UserServiceImpl`
- 方法名：与服务接口对应，例如 `create`、`find`、`update`、`delete`、`query`

## 代码结构

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDomain> implements UserService {

    private final UserExtensionService userExtensionService;
    private final UploadService uploadService;

    /**
     * 创建
     *
     * @param userVO
     * @return
     */
    @Override
    public UserVO create(UserVO userVO) {
        // 业务逻辑处理
        UserDomain domain = new UserDomain();
        BeanUtils.copyProperties(userVO, domain);
        
        // 密码加密
        if (VerifyUtils.isNotEmpty(userVO.getPassword())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            domain.setPassword(encoder.encode(userVO.getPassword()));
        }
        
        // 设置应用ID
        domain.setApplicationId(Context.applicationId());
        
        // 保存数据
        this.mapper.insert(domain);
        
        // 设置返回值
        userVO.setId(domain.getId());
        return userVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public UserVO find(Long id) {
        // 查询数据
        UserDomain domain = this.mapper.selectById(id);
        if (domain == null) {
            throw new ThrowPrompt("用户不存在");
        }
        
        // 转换为VO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(domain, userVO);
        
        // 查询关联数据
        UserExtensionDomain extension = userExtensionService.findByUserId(id);
        userVO.setExtension(extension);
        
        return userVO;
    }

    /**
     * 分页或列表
     *
     * @param userVO
     * @return PageOrList<UserVO>
     */
    @Override
    public PageOrList<UserVO> query(UserVO userVO) {
        // 构建查询条件
        QueryWrapper queryWrapper = QueryWrapper.create()
            .where(USER.APPLICATION_ID.eq(Context.applicationId()))
            .and(USER.STATUS.ne(UserStatusEnum.DELETED.getValue()));
            
        // 添加查询条件
        if (VerifyUtils.isNotEmpty(userVO.getUsername())) {
            queryWrapper.and(USER.USERNAME.like(userVO.getUsername()));
        }
        
        // 执行查询
        return this.page(queryWrapper, userVO, UserVO.class);
    }

    /**
     * 修改
     *
     * @param userVO
     */
    @Override
    public void update(UserVO userVO) {
        // 查询数据
        UserDomain domain = this.mapper.selectById(userVO.getId());
        if (domain == null) {
            throw new ThrowPrompt("用户不存在");
        }
        
        // 更新数据
        BeanUtils.copyProperties(userVO, domain);
        this.mapper.update(domain);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 查询数据
        UserDomain domain = this.mapper.selectById(id);
        if (domain == null) {
            throw new ThrowPrompt("用户不存在");
        }
        
        // 逻辑删除
        domain.setStatus(UserStatusEnum.DELETED.getValue());
        this.mapper.update(domain);
    }
}
```

## 注意事项

- 服务实现类继承 `BaseServiceImpl<M, T>` 类，泛型参数分别为对应的 Mapper 接口和 Domain 类
- 实现对应的服务接口
- 使用 `@Service` 注解标记为服务类
- 使用 `@RequiredArgsConstructor` 注解实现构造器注入
- 使用 `@Override` 注解标记重写方法
- 实现业务逻辑处理
- 使用 `BeanUtils.copyProperties` 进行对象属性复制
- 使用 `Context` 类获取上下文信息
- 使用 `ThrowPrompt` 抛出业务异常
- 使用 `VerifyUtils` 进行参数校验

## 关联查询

### 一对一关联

```java
// 查询关联数据
UserExtensionDomain extension = userExtensionService.findByUserId(id);
userVO.setExtension(extension);
```

### 一对多关联

```java
// 查询关联数据
List<UserRoleDomain> roles = userRoleService.findByUserId(id);
userVO.setRoles(roles);
```

### 多对多关联

```java
// 查询关联数据
List<Long> userIds = userVOList.stream().map(UserVO::getId).collect(Collectors.toList());
Map<Long, List<RoleDomain>> roleMap = roleService.findByUserIds(userIds);

// 设置关联数据
userVOList.forEach(userVO -> {
    userVO.setRoles(roleMap.getOrDefault(userVO.getId(), Collections.emptyList()));
});
```