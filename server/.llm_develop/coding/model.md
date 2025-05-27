### 模型层(model)

- 文件名：表名-model.ts
- 包含数据库字段映射、索引、和先关操作
-

IBaseCacheModel已包含id、createdAt、updatedAt、isDelete字段，如果表中全部包含这几个字段，模型层只需继承IBaseCacheModel。否则需要集成Model（sequelize-typescript）

- 索引命名规则：
    - 普通索引：idx_表名_字段名
    - 唯一索引：uniq_表名_字段名

```ts
import IBaseCacheModel from './base-model';
import { Table, Column, DataType, Index } from'sequelize-typescript';

/**
 * 用户表
 */
@Table({
    tableName: 'user',
})
class UserModel extends IBaseCacheModel {
    @Index('idx_user_email')
    @Column({
        field: 'email',
        type: DataType.STRING,
        comment: '邮箱',
    })
    declare email: string;

    @Column({
        field: 'password',
        type: DataType.STRING,
        comment: '密码',
    })
    declare password: string;
}

export default UserModel;
```

### 关联查询

- 一对多：在业务逻辑层处理
- 多对多：在业务逻辑层处理，先整理从表的外键set，批量查询，根据外键整理成map，回填的主表数据中
- 一对一：使用数据库关联查询

```ts
// 一对一需要修改model，增加关联字段
@Table({
    tableName: 'user',
})
class UserModel extends IBaseCacheModel {
  @BelongsTo(() => UserInfoModel, 'userInfoId')
  declare userInfo?: UserInfoModel;
} 

@Table({
    tableName: 'user_info',
})
class UserInfoModel extends IBaseCacheModel {
  @Column({
    field: 'user_id',
    type: DataType.STRING,
    comment: '用户id',
  })
  declare userId: string;
}

```