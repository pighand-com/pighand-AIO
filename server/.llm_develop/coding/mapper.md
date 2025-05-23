# 数据访问层 (Mapper)

## 命名规范
- 接口名：使用 `Mapper` 作为后缀，例如 `UserMapper`
- 方法名：遵循 MyBatis-Flex 的命名规范

## 代码结构
```java
/**
 * 用户 - 关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDomain> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM base_user WHERE username = #{username} AND status != 3")
    UserDomain findByUsername(String username);
    
    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM base_user WHERE phone = #{phone} AND status != 3")
    UserDomain findByPhone(String phone);
    
    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM base_user WHERE email = #{email} AND status != 3")
    UserDomain findByEmail(String email);
}
```

## 注意事项
- 数据访问层接口继承 `BaseMapper<T>` 接口，泛型参数为对应的 Domain 类
- 使用 `@Mapper` 注解标记为 MyBatis 的 Mapper 接口
- 继承 `BaseMapper` 接口后，自动获得常用的 CRUD 方法
- 简单查询可以使用 `@Select`、`@Insert`、`@Update`、`@Delete` 注解
- 复杂查询建议使用 XML 配置文件
- 可以使用 MyBatis-Flex 的查询构造器进行动态查询

## XML 配置示例
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.pighand.aio.mapper.base.UserMapper">
    <sql id="Base_Column_List">
        `id`,`application_id`,`password`,`username`,`email`,`email_verified`,`phone_area`,`phone`,`phone_verified`,`initial_source_platform`,`status`
    </sql>
    
    <select id="findUserWithRoles" resultMap="UserWithRolesMap">
        SELECT u.*, r.id as role_id, r.name as role_name
        FROM base_user u
        LEFT JOIN base_user_role ur ON u.id = ur.user_id
        LEFT JOIN base_role r ON ur.role_id = r.id
        WHERE u.id = #{id} AND u.status != 3
    </select>
    
    <resultMap id="UserWithRolesMap" type="com.pighand.aio.vo.base.UserVO">
        <id property="id" column="id"/>
        <result property="username" column="username"/>
        <result property="email" column="email"/>
        <result property="phone" column="phone"/>
        <result property="status" column="status"/>
        <collection property="roles" ofType="com.pighand.aio.domain.base.RoleDomain">
            <id property="id" column="role_id"/>
            <result property="name" column="role_name"/>
        </collection>
    </resultMap>
</mapper>
```

## 动态查询示例
```java
// 在 ServiceImpl 中使用
public PageOrList<UserVO> query(UserVO userVO) {
    QueryWrapper queryWrapper = QueryWrapper.create()
        .where(USER.APPLICATION_ID.eq(Context.applicationId()))
        .and(USER.STATUS.ne(UserStatusEnum.DELETED.getValue()));
        
    if (VerifyUtils.isNotEmpty(userVO.getUsername())) {
        queryWrapper.and(USER.USERNAME.like(userVO.getUsername()));
    }
    
    if (VerifyUtils.isNotEmpty(userVO.getPhone())) {
        queryWrapper.and(USER.PHONE.eq(userVO.getPhone()));
    }
    
    if (userVO.getStatus() != null) {
        queryWrapper.and(USER.STATUS.eq(userVO.getStatus()));
    }
    
    return this.page(queryWrapper, userVO, UserVO.class);
}
```