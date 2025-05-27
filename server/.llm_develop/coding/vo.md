# 视图对象 (VO)

## 命名规范

- 类名：使用 `VO` 作为后缀，例如 `UserVO`
- 字段名：与领域模型保持一致，可以添加额外字段

## 代码结构

```java
@TableRef(UserDomain.class)
@Data
public class UserVO extends UserDomain {

    // relation table: begin
    private UserExtensionDomain extension;

    private ApplicationDomain application;
    // relation table: end

    private String token;

    private Integer bindCount;

    private List<ApplicationDomain> relevanceApplications;
}
```

## 注意事项

- 视图对象通常继承自对应的领域模型
- 使用 `@TableRef` 注解指定对应的领域模型类
- 使用 `@Data` 注解自动生成 getter/setter 方法
- 可以添加额外的字段，用于接收请求参数或返回结果
- 可以添加关联对象，用于存储关联数据
- 可以添加验证注解，用于参数校验
- 可以定义验证分组，用于不同场景的验证

## 验证分组示例

```java
@Data
public class UserVO extends UserDomain {

    @NotBlank(message = "用户名不能为空", groups = {ValidationGroup.Create.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {ValidationGroup.Create.class})
    private String password;

    // 验证分组
    public interface Create {}
    public interface Update {}
}
```

## 分页参数示例

```java
@Data
public class UserVO extends UserDomain {

    // 分页参数
    private Integer page;
    private Integer pageSize;
}
```