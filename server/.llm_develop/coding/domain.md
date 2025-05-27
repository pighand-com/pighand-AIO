# 领域模型 (Domain)

## 命名规范

- 类名：使用 `Domain` 作为后缀，例如 `UserDomain`
- 字段名：驼峰命名，与数据库字段对应

## 代码结构

```java
@Data
@Table("base_user")
public class UserDomain extends BaseDomainRecord<UserDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("userCreate")
    @RequestFieldException("userUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "应用id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "角色id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "邮箱是否验证")
    private Boolean emailVerified;

    @Schema(description = "手机区号")
    private String phoneArea;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "手机是否验证")
    private Boolean phoneVerified;

    @Schema(description = "初始来源平台")
    private Integer initialSourcePlatform;

    @Schema(description = "状态：1-正常，2-禁用，3-注销")
    private Integer status;
}
```

## 注意事项

- 领域模型继承 `BaseDomainRecord<T>` 类，泛型参数为当前类
- 实现 `Serializable` 接口
- 使用 `@Data` 注解自动生成 getter/setter 方法
- 使用 `@Table` 注解指定对应的数据库表名
- 使用 `@Id` 注解标记主键字段
- 使用 `@Schema` 注解添加字段描述，用于 API 文档生成
- 使用 `@JsonDeserialize` 和 `@JsonSerialize` 注解处理 JSON 序列化和反序列化
- 使用 `@RequestFieldException` 注解排除字段，不在特定的请求中使用
- 使用 `@NotNull` 等验证注解进行参数校验，配合验证分组使用
- 字段类型与数据库字段类型对应
- 字段名与数据库字段名对应，通常使用驼峰命名