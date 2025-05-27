# XML 映射文件 (Mapper XML)

## 命名规范

- 文件名：与 Mapper 接口对应，例如 `UserMapper.xml`
- 命名空间：与 Mapper 接口的全限定名一致

## 文件位置

- 位于 `src/main/resources/mapper` 目录下
- 按模块分子目录，例如 `base`、`CMS`、`ECommerce` 等

## 代码结构

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.pighand.aio.mapper.base.UserMapper">
    <sql id="Base_Column_List">
        `id`,`application_id`,`password`,`username`,`email`,`email_verified`,`phone_area`,`phone`,`phone_verified`,`initial_source_platform`,`status`
    </sql>
    
    <!-- 自定义查询方法 -->
    <select id="findUserWithRoles" resultMap="UserWithRolesMap">
        SELECT u.*, r.id as role_id, r.name as role_name
        FROM base_user u
        LEFT JOIN base_user_role ur ON u.id = ur.user_id
        LEFT JOIN base_role r ON ur.role_id = r.id
        WHERE u.id = #{id} AND u.status != 3
    </select>
    
    <!-- 结果映射 -->
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

## 注意事项

- 命名空间必须与对应的 Mapper 接口全限定名一致
- 使用 `<sql>` 标签定义可重用的 SQL 片段
- 使用 `<resultMap>` 标签定义复杂的结果映射
- 使用 `<select>`、`<insert>`、`<update>`、`<delete>` 标签定义 SQL 语句
- 方法 ID 必须与 Mapper 接口中的方法名一致
- 参数类型和返回类型必须与 Mapper 接口中的方法签名一致

## 常用标签

### SQL 片段

```xml
<sql id="Base_Column_List">
    `id`,`application_id`,`password`,`username`,`email`,`email_verified`,`phone_area`,`phone`,`phone_verified`,`initial_source_platform`,`status`
</sql>
```

### 结果映射

```xml
<resultMap id="UserMap" type="com.pighand.aio.domain.base.UserDomain">
    <id property="id" column="id"/>
    <result property="applicationId" column="application_id"/>
    <result property="username" column="username"/>
    <result property="email" column="email"/>
    <result property="phone" column="phone"/>
    <result property="status" column="status"/>
</resultMap>
```

### 一对一关联

```xml
<resultMap id="UserWithExtensionMap" type="com.pighand.aio.vo.base.UserVO">
    <id property="id" column="id"/>
    <result property="username" column="username"/>
    <result property="email" column="email"/>
    <result property="phone" column="phone"/>
    <result property="status" column="status"/>
    <association property="extension" javaType="com.pighand.aio.domain.base.UserExtensionDomain">
        <id property="id" column="extension_id"/>
        <result property="userId" column="user_id"/>
        <result property="nickname" column="nickname"/>
        <result property="avatar" column="avatar"/>
    </association>
</resultMap>
```

### 一对多关联

```xml
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
```

### 动态 SQL

```xml
<select id="findByCondition" resultMap="UserMap">
    SELECT <include refid="Base_Column_List"/>
    FROM base_user
    <where>
        <if test="username != null and username != ''">
            AND username = #{username}
        </if>
        <if test="phone != null and phone != ''">
            AND phone = #{phone}
        </if>
        <if test="email != null and email != ''">
            AND email = #{email}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
        AND status != 3
    </where>
</select>
```