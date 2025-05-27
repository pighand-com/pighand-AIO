# 服务层 (Service)

## 命名规范

- 接口名：使用 `Service` 作为后缀，例如 `UserService`
- 实现类名：使用 `ServiceImpl` 作为后缀，例如 `UserServiceImpl`
- 方法名：与控制层对应，例如 `create`、`find`、`update`、`delete`、`query`

## 接口定义

```java
public interface UserService extends BaseService<UserDomain> {

    /**
     * 创建
     *
     * @param userVO
     * @return
     */
    UserVO create(UserVO userVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserVO find(Long id);

    /**
     * 分页或列表
     *
     * @param userVO
     * @return PageOrList<UserVO>
     */
    PageOrList<UserVO> query(UserVO userVO);

    /**
     * 修改
     *
     * @param userVO
     */
    void update(UserVO userVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
```

## 注意事项

- 服务层接口继承 `BaseService<T>` 接口，泛型参数为对应的 Domain 类
- 定义与控制层对应的业务方法
- 方法参数和返回值通常使用 VO 对象
- 接口中只定义方法签名，不包含实现逻辑