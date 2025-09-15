# CMS 素材管理系统 API 接口文档

## 概述

本文档描述了CMS素材管理系统的客户端API接口，包括素材分类、专辑、文档、图片、视频和收藏功能。

## 基础信息

- **基础URL**: `/api`
- **返回格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

## 1. 素材分类管理

### 1.1 获取分类列表

**接口地址**: `GET /client/assets/classification`

**接口描述**: 获取所有可用的素材分类选项（不分页）

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| 查询条件 | AssetsClassificationVO | 否 | 分类查询条件 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "图片分类",
      "description": "图片素材分类"
    }
  ]
}
```

---

## 2. 素材专辑管理

### 2.1 获取专辑列表

**接口地址**: `GET /client/assets/collection`

**接口描述**: 获取所有可用的素材专辑选项（不分页）

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| 查询条件 | AssetsCollectionVO | 否 | 专辑查询条件 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "风景专辑",
      "description": "自然风景素材专辑"
    }
  ]
}
```

---

## 3. 文档素材管理

### 3.1 获取文档详情

**接口地址**: `GET /client/assets/doc/{id}`

**接口描述**: 获取指定文档的详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 文档ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "文档标题",
    "content": "文档内容",
    "fileUrl": "https://example.com/doc.pdf"
  }
}
```

### 3.2 获取文档列表

**接口地址**: `GET /client/assets/doc`

**接口描述**: 获取文档列表，支持分页和筛选

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| 查询条件 | AssetsDocVO | 否 | 文档查询条件（包含分页信息） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "文档标题",
        "fileUrl": "https://example.com/doc.pdf",
        "classification": {
          "id": 1,
          "name": "技术文档"
        }
      }
    ],
    "total": 100,
    "current": 1,
    "size": 10
  }
}
```

---

## 4. 图片素材管理

### 4.1 获取图片详情

**接口地址**: `GET /client/assets/image/{id}`

**接口描述**: 获取指定图片的详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 图片ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "图片标题",
    "imageUrl": "https://example.com/image.jpg",
    "thumbnailUrl": "https://example.com/thumb.jpg"
  }
}
```

### 4.2 获取图片列表

**接口地址**: `GET /client/assets/image`

**接口描述**: 获取图片列表，支持分页和筛选

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| 查询条件 | AssetsImageVO | 否 | 图片查询条件（包含分页信息） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "图片标题",
        "imageUrl": "https://example.com/image.jpg",
        "thumbnailUrl": "https://example.com/thumb.jpg",
        "classification": {
          "id": 1,
          "name": "风景图片"
        }
      }
    ],
    "total": 100,
    "current": 1,
    "size": 10
  }
}
```

---

## 5. 视频素材管理

### 5.1 获取视频详情

**接口地址**: `GET /client/assets/video/{id}`

**接口描述**: 获取指定视频的详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 视频ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "视频标题",
    "videoUrl": "https://example.com/video.mp4",
    "thumbnailUrl": "https://example.com/video-thumb.jpg",
    "duration": 120
  }
}
```

### 5.2 获取视频列表

**接口地址**: `GET /client/assets/video`

**接口描述**: 获取视频列表，支持分页和筛选

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| 查询条件 | AssetsVideoVO | 否 | 视频查询条件（包含分页信息） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "视频标题",
        "videoUrl": "https://example.com/video.mp4",
        "thumbnailUrl": "https://example.com/video-thumb.jpg",
        "duration": 120,
        "classification": {
          "id": 1,
          "name": "教学视频"
        }
      }
    ],
    "total": 100,
    "current": 1,
    "size": 10
  }
}
```

---

## 6. 素材收藏管理

### 6.1 创建收藏

**接口地址**: `POST /cms/assets/favourite`

**接口描述**: 添加素材到收藏夹

**请求体**:

```json
{
  "assetsId": 1,
  "assetsType": "image",
  "userId": 123
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "assetsId": 1,
    "assetsType": "image",
    "userId": 123,
    "createTime": "2025-01-01 12:00:00"
  }
}
```

### 6.2 获取收藏详情

**接口地址**: `GET /cms/assets/favourite/{id}`

**接口描述**: 获取指定收藏记录的详细信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 收藏记录ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "assetsId": 1,
    "assetsType": "image",
    "userId": 123,
    "createTime": "2025-01-01 12:00:00"
  }
}
```

### 6.3 获取收藏列表

**接口地址**: `GET /cms/assets/favourite`

**接口描述**: 获取用户的收藏列表，支持分页

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| 查询条件 | AssetsFavouriteVO | 否 | 收藏查询条件（包含分页信息） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "assetsId": 1,
        "assetsType": "image",
        "userId": 123,
        "createTime": "2025-01-01 12:00:00"
      }
    ],
    "total": 50,
    "current": 1,
    "size": 10
  }
}
```

### 6.4 更新收藏

**接口地址**: `PUT /cms/assets/favourite/{id}`

**接口描述**: 更新收藏记录信息

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 收藏记录ID |

**请求体**:

```json
{
  "assetsId": 1,
  "assetsType": "video",
  "userId": 123
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 6.5 删除收藏

**接口地址**: `DELETE /cms/assets/favourite/{id}`

**接口描述**: 删除收藏记录

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Long | 是 | 收藏记录ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 错误码说明

| 错误码 | 描述 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权访问 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 注意事项

1. 所有接口都需要在请求头中包含有效的认证信息
2. 分页查询默认每页10条记录，最大不超过100条
3. 文件上传大小限制为50MB
4. 支持的图片格式：JPG、PNG、GIF、WEBP
5. 支持的视频格式：MP4、AVI、MOV、WMV
6. 支持的文档格式：PDF、DOC、DOCX、TXT、MD

---

## 7. 人机验证管理

### 7.1 获取验证码

**接口地址**: `GET /client/CAPTCHA/code` 或 `GET /dashboard/CAPTCHA/code`

**接口描述**: 获取人机验证码，用于登录等安全验证场景

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| key | String | 是 | 验证码标识键 |

**请求示例**:
```
GET /client/CAPTCHA/code?key=login_captcha
```

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "codeId": "abc123def456",
    "codeImage": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...",
    "expireTime": 300
  }
}
```

---

## 8. 用户登录管理

### 8.1 微信小程序登录

**接口地址**: `POST /client/login/wechat/applet`

**接口描述**: 通过微信小程序授权码进行登录

**请求体**:

```json
{
  "code": "wx_auth_code_from_wechat"
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| code | String | 是 | 微信小程序授权码 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 123,
    "username": "user123",
    "nickname": "用户昵称",
    "avatar": "https://example.com/avatar.jpg",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_string",
    "expiresIn": 7200
  }
}
```

### 8.2 密码登录

**接口地址**: `POST /client/login`

**接口描述**: 通过用户名和密码进行登录，需要验证码验证

**请求体**:

```json
{
  "username": "user123",
  "password": "password123",
  "captcha": "验证码",
  "captchaKey": "验证码标识"
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| username | String | 是 | 用户名/手机号/邮箱 |
| password | String | 是 | 登录密码 |
| captcha | String | 是 | 验证码 |
| captchaKey | String | 是 | 验证码标识键 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 123,
    "username": "user123",
    "nickname": "用户昵称",
    "avatar": "https://example.com/avatar.jpg",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_string",
    "expiresIn": 7200
  }
}
```

**注意事项**:
- 密码登录需要先调用验证码接口获取验证码
- 验证码有效期为5分钟
- 连续登录失败3次将锁定账户30分钟

---

**文档版本**: v1.1  
**最后更新**: 2025-01-01  
**作者**: wangshuli