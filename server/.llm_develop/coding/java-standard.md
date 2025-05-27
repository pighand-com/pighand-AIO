# Java 开发规范

## 控制层 (Controller)

### 命名规范

- 类名：使用 `Controller` 作为后缀，例如 `UserController`
- 方法名：使用 HTTP 方法前缀，例如 `create`、`find`、`update`、`delete`、`query`

### 代码结构

```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Validated UserVO userVO) {
        UserVO result = userService.create(userVO);
        return ResponseEntity.success(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity find(@PathVariable Long id) {
        UserDomain result = userService.find(id);
        return ResponseEntity.success(result);
    }

    @GetMapping
    public ResponseEntity query(UserVO userVO) {
        PageOrList<UserVO> result = userService.query(userVO);
        return ResponseEntity.success(result);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody @Validated UserVO userVO) {
        userService.update(userVO);
        return ResponseEntity.success();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.success();
    }
}
```

### 注意事项

- 控制层只负责参数校验和结果返回，不包含业务逻辑
- 使用 `@Validated` 注解进行参数校验
- 统一使用 `ResponseEntity` 封装返回结果
- 遵循 RESTful API 设计规范
- 使用 SpringDoc 注解添加 API 文档

## 服务层 (Service)

### 命名规范

- 接口名：使用 `Service` 作为后缀，例如 `UserService`
- 实现类名：使用 `ServiceImpl` 作为后缀，例如 `UserServiceImpl`
- 方法名：与控制层对应，例如 `create`、`find`、`update`、`delete`、`query`

### 代码结构

```java
// 接口定义
public interface UserService extends BaseService<UserDomain> {
    UserVO create(UserVO userVO);
    UserDomain find(Long id);
    PageOrList<UserVO> query(UserVO userVO);
    void update(UserVO userVO);
    void delete(Long id);
}

// 实现类
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDomain> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserVO create(UserVO userVO) {
        // 业务逻辑处理
        UserDomain domain = new UserDomain();
        BeanUtils.copyProperties(userVO, domain);
        
        userMapper.insert(domain);
        
        userVO.setId(domain.getId());
        return userVO;
    }
    
    // 其他方法实现...
}
```

### 注意事项

- 服务层负责实现业务逻辑
- 使用 `@Transactional` 注解管理事务
- 继承 `BaseService` 和 `BaseServiceImpl` 获取通用方法
- 使用 BeanUtils 进行对象属性复制
- 避免在服务层直接处理 HTTP 相关的内容

## 领域模型 (Domain)

### 命名规范

- 类名：使用 `Domain` 作为后缀，例如 `UserDomain`
- 字段名：驼峰命名，与数据库字段对应

### 代码结构

```java
@Data
@TableName("user")
public class UserDomain {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
```

### 注意事项

- 使用 Lombok 注解简化代码
- 使用 MyBatis-Flex 注解映射数据库表和字段
- 领域模型应该与数据库表结构一一对应
- 可以添加基本的字段验证注解

## 视图对象 (VO)

### 命名规范

- 类名：使用 `VO` 作为后缀，例如 `UserVO`
- 字段名：与领域模型保持一致，可以添加额外字段

### 代码结构

```java
@Data
public class UserVO extends UserDomain {

    @NotBlank(message = "用户名不能为空", groups = {Create.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {Create.class})
    private String password;

    // 额外字段，不在领域模型中
    private String confirmPassword;

    // 分页参数
    private Integer page;
    private Integer pageSize;
    
    // 验证分组
    public interface Create {}
    public interface Update {}
}
```

### 注意事项

- 通常继承自对应的领域模型
- 添加参数校验注解
- 可以定义验证分组，用于不同场景的验证
- 可以添加额外的字段，用于接收请求参数或返回结果
- 可以包含分页参数

## 数据访问层 (Mapper)

### 命名规范

- 接口名：使用 `Mapper` 作为后缀，例如 `UserMapper`
- 方法名：遵循 MyBatis-Flex 的命名规范

### 代码结构

```java
@Mapper
public interface UserMapper extends BaseMapper<UserDomain> {

    // 自定义查询方法
    @Select("SELECT * FROM user WHERE username = #{username}")
    UserDomain findByUsername(String username);
    
    // 复杂查询可以使用XML配置
}
```

### 注意事项

- 继承 `BaseMapper` 获取通用的 CRUD 方法
- 简单查询可以使用注解方式
- 复杂查询建议使用 XML 配置
- 使用 MyBatis-Flex 的查询构造器进行动态查询

## 异常处理

### 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return ResponseEntity.error(500, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.error(400, message);
    }
}
```

### 自定义异常

```java
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
```

## 日志规范

### 日志配置

- 使用 SLF4J + Logback 作为日志框架
- 按照环境区分日志级别
- 生产环境日志文件按天滚动

### 日志使用

```java
private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

// 记录普通信息
logger.info("用户 {} 登录成功", username);

// 记录警告信息
logger.warn("用户 {} 尝试访问未授权资源 {}", username, resourceId);

// 记录错误信息
logger.error("系统异常", e);
```

## 单元测试

### 测试规范

- 使用 JUnit 5 + Mockito 进行单元测试
- 测试类名：被测试类名 + `Test`，例如 `UserServiceTest`
- 测试方法名：`test` + 被测试方法名 + 测试场景，例如 `testCreateUserSuccess`

### 测试示例

```java
@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUserSuccess() {
        // 准备测试数据
        UserVO userVO = new UserVO();
        userVO.setUsername("test");
        userVO.setPassword("123456");

        // Mock 依赖方法
        when(userMapper.insert(any(UserDomain.class))).thenReturn(1);

        // 执行测试
        UserVO result = userService.create(userVO);

        // 验证结果
        assertNotNull(result);
        assertEquals("test", result.getUsername());
        
        // 验证依赖方法是否被调用
        verify(userMapper, times(1)).insert(any(UserDomain.class));
    }
}
```