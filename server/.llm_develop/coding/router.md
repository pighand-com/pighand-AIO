### 路由层(router)
- 放在单独的文件夹中，文件名：表名。然后在路由中注册
- 文件夹中包含文件：
  - index.ts：路由文件
  - openapi.ts：接口文档

#### 接口文档
- 接口文档使用openapi格式

代码举例：
```ts
import * as v from 'valibot';
import { describeRoute } from 'hono-openapi'
import { resolver } from 'hono-openapi/valibot';

export default describeRoute({
  description: 'User login API',
  requestBody: {
    description: 'Login credentials',
    content: {
    'application/json': {
      schema: {
      type: 'object',
      required: ['email', 'password'],
      properties: {
        email: {
        type: 'string',
        description: '邮箱',
        },
        password: {
        type: 'string',
        description: '密码'
        }
      }
      }
    }
    }
  },
  responses: {
    200: {
      description: 'Successful response',
      content: {
        'text/plain': { schema: resolver(v.string()) },
      },
    },
  },
});
```

#### 路由文件
- 请求类型选择
    - 创建用POST
    - 查询用GET
    - 更新用PUT
    - 删除用DELETE
- 鉴权中间件：如果接口需要鉴权，使用中间件：authRequiredMiddleware()

代码举例：
```ts
import { Hono } from "hono";

import { authRequiredMiddleware } from "../../middleware/auth-middleware";
import userController from "../../controller/user-controller";
import openapi from './openapi';

const api = new Hono();

api.post("/users", openapi, authRequiredMiddleware, ...userController.create);

export default api;
```

#### 路由注册
- 路由注册在router根目录的index.ts中
- 路由注册方式：app.route('/api/user', userRouter);

代码举例：
```ts
import { Hono } from "hono";
import userRouter from "./user";

const api = new Hono();
api.route('/api/v1', userRouter);

export default api;
```