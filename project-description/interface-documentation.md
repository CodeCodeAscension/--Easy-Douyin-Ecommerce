## **接口文档**

### **通用说明**
1. **请求头**：
   - 经过网关测试需要用户登录的接口，请求头需设置 `Authorization` 字段。
   - 不经过网关直接测试微服务接口，请求头需设置 `X-User-Id` 和 `X-User-Power` 字段。

2. **响应格式**：
   所有接口都会返回 `ResponseResult` 对象，形式如下：

   | 参数名称 | 参数说明    | 类型  |
   |------|---------|-----|
   | code | 接口返回状态码 | 整数  |
   | data | 接口返回的数据 | 对象  |
   | msg  | 接口返回的信息 | 字符串 |

3. **权限说明**：
   - 部分接口需要用户登陆，部分接口需要管理员登陆。接口权限详情可以看网关配置定义。

---

## **用户服务接口文档**

### **1. 用户注册**
- **接口地址**：`/api/v1/user/register`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody RegisterDto`

  | 参数名称            | 参数说明 | 传参位置        | 是否必须 | 数据类型 |
  |-----------------|------|-------------|------|------|
  | email           | 用户邮箱 | RegisterDto | 是    | 字符串  |
  | password        | 用户密码 | RegisterDto | 是    | 字符串  |
  | confirmPassword | 确认密码 | RegisterDto | 是    | 字符串  |

- **请求示例**：
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }
  ```

- **返回对象**：`ResponseResult<RegisterVo>`，其中 `data` 字段的定义为：

  | 参数名称   | 参数说明 | 数据类型 |
  |--------|------|------|
  | userId | 用户ID | 长整型  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "注册成功",
    "data": {
      "userId": 1892225722047713282
    }
  }
  ```

---

### **2. 用户登录**
- **接口地址**：`/api/v1/user/login`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody LoginDto`

  | 参数名称     | 参数说明 | 传参位置     | 是否必须 | 数据类型 |
  |----------|------|----------|------|------|
  | email    | 用户邮箱 | LoginDto | 是    | 字符串  |
  | password | 用户密码 | LoginDto | 是    | 字符串  |

- **请求示例**：
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

- **返回对象**：`ResponseResult<LoginVo>`，其中 `data` 字段的定义为：

  | 参数名称         | 参数说明 | 数据类型 |
  |--------------|------|------|
  | userId       | 用户ID | 长整型  |
  | accessToken  | 访问令牌 | 字符串  |
  | refreshToken | 刷新令牌 | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "登录成功",
    "data": {
      "userId": 1892225722047713281,
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  }
  ```

---

### **3. 用户登出**
- **接口地址**：`/api/v1/user/logout`
- **请求方式**：`POST`
- **请求参数**：无
- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "登出成功",
    "data": null
  }
  ```

---

### **4. 刷新访问令牌**
- **接口地址**：`/api/v1/user/refresh`
- **请求方式**：`PUT`
- **请求参数**：`@RequestHeader("Refresh-Token")`

  | 参数名称          | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |---------------|------|------|------|------|
  | Refresh-Token | 刷新令牌 | 请求头  | 是    | 字符串  |

- **返回对象**：`ResponseResult<LoginVo>`，其中 `data` 字段的定义为：

  | 参数名称         | 参数说明 | 数据类型 |
  |--------------|------|------|
  | userId       | 用户ID | 长整型  |
  | accessToken  | 访问令牌 | 字符串  |
  | refreshToken | 刷新令牌 | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "令牌刷新成功",
    "data": {
      "userId": 1892225722047713281,
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  }
  ```

---

### **5. 获取用户信息**
- **接口地址**：`/api/v1/users`
- **请求方式**：`GET`
- **请求参数**：无
- **返回对象**：`ResponseResult<UserInfoVo>`，其中 `data` 字段的定义为：

  | 参数名称       | 参数说明            | 数据类型   |
  |------------|-----------------|--------|
  | userId     | 用户ID            | 长整型    |
  | email      | 用户邮箱            | 字符串    |
  | username   | 用户名             | 字符串    |
  | phone      | 手机号             | 字符串    |
  | city       | 所在城市            | 字符串    |
  | province   | 所在省份            | 字符串    |
  | country    | 所在国家            | 字符串    |
  | zipCode    | 邮政编码            | 字符串    |
  | currency   | 用户使用的货币         | 字符串    |
  | status     | 状态（0正常，1封禁，2注销） | 整数     |
  | reason     | 注销或封禁原因         | 字符串    |
  | createTime | 创建时间            | 本地日期时间 |
  | updateTime | 修改时间            | 本地日期时间 |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取用户信息成功",
    "data": {
      "userId": 1892225722047713281,
      "email": "user@example.com",
      "username": "张三",
      "phone": "1234567890",
      "city": "上海",
      "province": "上海",
      "country": "中国",
      "zipCode": "200000",
      "currency": "CNY",
      "status": 0,
      "reason": null,
      "createTime": "2023-10-01T12:00:00",
      "updateTime": "2023-10-05T14:30:00"
    }
  }
  ```

---

### **6. 用户注销**
- **接口地址**：`/api/v1/users`
- **请求方式**：`DELETE`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody LogoffDto`

  | 参数名称   | 参数说明             | 传参位置      | 是否必须 | 数据类型 |
  |--------|------------------|-----------|------|------|
  | status | 注销或封禁状态（1封禁，2注销） | LogoffDto | 是    | 整数   |
  | reason | 注销或封禁原因          | LogoffDto | 否    | 字符串  |

- **请求示例**：
  ```json
  {
    "status": 2,
    "reason": "用户主动注销"
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "用户注销成功",
    "data": null
  }
  ```

---

### **7. 更新用户信息**
- **接口地址**：`/api/v1/users`
- **请求方式**：`PUT`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody UserUpdateDto`

  | 参数名称     | 参数说明    | 传参位置          | 是否必须 | 数据类型 |
  |----------|---------|---------------|------|------|
  | username | 用户名     | UserUpdateDto | 否    | 字符串  |
  | phone    | 手机号     | UserUpdateDto | 否    | 字符串  |
  | city     | 所在城市    | UserUpdateDto | 否    | 字符串  |
  | province | 所在省份    | UserUpdateDto | 否    | 字符串  |
  | country  | 所在国家    | UserUpdateDto | 否    | 字符串  |
  | zipCode  | 邮政编码    | UserUpdateDto | 否    | 字符串  |
  | currency | 用户使用的货币 | UserUpdateDto | 否    | 字符串  |

- **请求示例**：
  ```json
  {
    "username": "张三",
    "phone": "1234567890",
    "city": "上海",
    "province": "上海",
    "country": "中国",
    "zipCode": "200000",
    "currency": "CNY"
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "用户信息更新成功",
    "data": null
  }
  ```

---

### **8. 管理员操作接口**

#### **8.1 封禁某个用户**
- **接口地址**：`/api/v1/users/admin`
- **请求方式**：`DELETE`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody BannedDto`

  | 参数名称   | 参数说明 | 传参位置      | 是否必须 | 数据类型 |
  |--------|------|-----------|------|------|
  | userId | 用户ID | BannedDto | 是    | 长整型  |
  | reason | 封禁原因 | BannedDto | 否    | 字符串  |

- **请求示例**：
  ```json
  {
    "userId": 1892225722047713281,
    "reason": "违反社区规定"
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "用户封禁成功",
    "data": null
  }
  ```

---

#### **8.2 修改某个用户权限**
- **接口地址**：`/api/v1/users/admin`
- **请求方式**：`PUT`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody UpdatePowerDto`

  | 参数名称   | 参数说明   | 传参位置           | 是否必须 | 数据类型 |
  |--------|--------|----------------|------|------|
  | userId | 用户ID   | UpdatePowerDto | 是    | 长整型  |
  | status | 是否为管理员 | UpdatePowerDto | 是    | 布尔值  |

- **请求示例**：
  ```json
  {
    "userId": 1892225722047713281,
    "status": true
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "用户权限修改成功",
    "data": null
  }
  ```

---

## **商品服务接口文档**

### **1. 根据商品ID获取商品信息**
- **接口地址**：`/api/v1/products/{productId}`
- **请求方式**：`GET`
- **请求参数**：`@PathVariable productId`

  | 参数名称      | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |-----------|------|------|------|------|
  | productId | 商品ID | 路径参数 | 是    | 长整型  |

- **返回对象**：`ResponseResult<ProductInfoVo>`，其中 `data` 字段的定义为：

  | 参数名称         | 参数说明        | 数据类型   |
  |--------------|-------------|--------|
  | id           | 商品ID        | 长整型    |
  | name         | 商品名称        | 字符串    |
  | description  | 商品描述        | 字符串    |
  | price        | 商品价格        | 浮点     |
  | sold         | 商品销量        | 整数     |
  | stock        | 商品库存        | 整数     |
  | merchantName | 商家名称        | 字符串    |
  | categories   | 所属类别        | 字符串列表  |
  | status       | 状态（0上架，1下架） | 整数     |
  | createTime   | 创建时间        | 本地日期时间 |
  | updateTime   | 修改时间        | 本地日期时间 |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取商品信息成功",
    "data": {
      "id": 123456789,
      "name": "智能手机",
      "description": "高性能智能手机",
      "price": 2999.99,
      "sold": 50,
      "stock": 100,
      "merchantName": "某商家",
      "categories": ["电子产品", "手机"],
      "status": 0,
      "createTime": "2023-10-01T12:00:00",
      "updateTime": "2023-10-05T14:30:00"
    }
  }
  ```

---

### **2. 指定某种类别查询商品信息**
- **接口地址**：`/api/v1/products`
- **请求方式**：`GET`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody ListProductsDto`

  | 参数名称         | 参数说明       | 传参位置            | 是否必须 | 数据类型 |
  |--------------|------------|-----------------|------|------|
  | page         | 第几页（默认1）   | ListProductsDto | 否    | 整数   |
  | pageSize     | 每页大小（默认20） | ListProductsDto | 否    | 整数   |
  | categoryName | 类型名称       | ListProductsDto | 否    | 字符串  |

- **请求示例**：
  ```json
  {
    "page": 1,
    "pageSize": 10,
    "categoryName": "电子产品"
  }
  ```

- **返回对象**：`ResponseResult<IPage<ProductInfoVo>>`，其中 `data` 字段的定义为：

  | 参数名称    | 参数说明   | 数据类型                  |
  |---------|--------|-----------------------|
  | records | 商品信息列表 | `List<ProductInfoVo>` |
  | total   | 总记录数   | 长整型                   |
  | size    | 每页大小   | 整数                    |
  | current | 当前页码   | 整数                    |
  | pages   | 总页数    | 整数                    |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取商品列表成功",
    "data": {
      "records": [
        {
          "id": 123456789,
          "name": "智能手机",
          "description": "高性能智能手机",
          "price": 2999.99,
          "sold": 50,
          "stock": 100,
          "merchantName": "某商家",
          "categories": ["电子产品", "手机"],
          "status": 0,
          "createTime": "2023-10-01T12:00:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

### **3. 指定条件查询商品信息**
- **接口地址**：`/api/v1/products/search`
- **请求方式**：`GET`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody SearchProductsDto`

  | 参数名称         | 参数说明       | 传参位置              | 是否必须 | 数据类型 |
  |--------------|------------|-------------------|------|------|
  | page         | 第几页（默认1）   | SearchProductsDto | 否    | 整数   |
  | pageSize     | 每页大小（默认20） | SearchProductsDto | 否    | 整数   |
  | productName  | 商品名称       | SearchProductsDto | 否    | 字符串  |
  | priceLow     | 最低价格       | SearchProductsDto | 否    | 浮点   |
  | priceHigh    | 最高价格       | SearchProductsDto | 否    | 浮点   |
  | stock        | 库存         | SearchProductsDto | 否    | 整数   |
  | sold         | 销量         | SearchProductsDto | 否    | 整数   |
  | merchantName | 商家名称       | SearchProductsDto | 否    | 字符串  |
  | categoryName | 类型名称       | SearchProductsDto | 否    | 字符串  |

- **请求示例**：
  ```json
  {
    "page": 1,
    "pageSize": 10,
    "productName": "手机",
    "priceLow": 1000,
    "priceHigh": 5000
  }
  ```

- **返回对象**：`ResponseResult<IPage<ProductInfoVo>>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "条件查询商品信息成功",
    "data": {
      "records": [
        {
          "id": 123456789,
          "name": "智能手机",
          "description": "高性能智能手机",
          "price": 2999.99,
          "sold": 50,
          "stock": 100,
          "merchantName": "某商家",
          "categories": ["电子产品", "手机"],
          "status": 0,
          "createTime": "2023-10-01T12:00:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

### **4. 增加库存**
- **接口地址**：`/api/v1/products/add`
- **请求方式**：`PUT`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AddProductDto`

  | 参数名称      | 参数说明    | 传参位置          | 是否必须 | 数据类型 |
  |-----------|---------|---------------|------|------|
  | productId | 商品ID    | AddProductDto | 是    | 长整型  |
  | addStock  | 增加的库存数量 | AddProductDto | 是    | 整数   |

- **请求示例**：
  ```json
  {
    "productId": 123456789,
    "addStock": 10
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "库存增加成功",
    "data": null
  }
  ```

---


### **5. 创建商品**
- **接口地址**：`/api/v1/products`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody CreateProductDto`

  | 参数名称         | 参数说明        | 传参位置             | 是否必须 | 数据类型  |
  |--------------|-------------|------------------|------|-------|
  | name         | 商品名称        | CreateProductDto | 是    | 字符串   |
  | description  | 商品描述        | CreateProductDto | 是    | 字符串   |
  | price        | 商品价格        | CreateProductDto | 是    | 浮点    |
  | stock        | 商品库存        | CreateProductDto | 否    | 整数    |
  | merchantName | 商家名称        | CreateProductDto | 是    | 字符串   |
  | categories   | 所属类别        | CreateProductDto | 是    | 字符串列表 |
  | status       | 状态（0上架，1下架） | CreateProductDto | 否    | 整数    |

- **请求示例**：
  ```json
  {
    "name": "智能手机",
    "description": "高性能智能手机",
    "price": 2999.99,
    "stock": 100,
    "merchantName": "某商家",
    "categories": ["电子产品", "手机"],
    "status": 0
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "商品创建成功",
    "data": null
  }
  ```

---

### **6. 批量创建商品**
- **接口地址**：`/api/v1/products/batch`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody List<CreateProductDto>`

  **CreateProductDto 的定义**：

  | 参数名称         | 参数说明        | 数据类型  |
  |--------------|-------------|-------|
  | name         | 商品名称        | 字符串   |
  | description  | 商品描述        | 字符串   |
  | price        | 商品价格        | 浮点    |
  | stock        | 商品库存        | 整数    |
  | merchantName | 商家名称        | 字符串   |
  | categories   | 所属类别        | 字符串列表 |
  | status       | 状态（0上架，1下架） | 整数    |

- **请求示例**：
  ```json
  [
    {
      "name": "智能手机",
      "description": "高性能智能手机",
      "price": 2999.99,
      "stock": 100,
      "merchantName": "某商家",
      "categories": ["电子产品", "手机"],
      "status": 0
    },
    {
      "name": "笔记本电脑",
      "description": "高性能笔记本电脑",
      "price": 5999.99,
      "stock": 50,
      "merchantName": "某商家",
      "categories": ["电子产品", "电脑"],
      "status": 0
    }
  ]
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "批量创建商品成功",
    "data": null
  }
  ```

---

### **7. 更新商品信息**
- **接口地址**：`/api/v1/products`
- **请求方式**：`PUT`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody UpdateProductDto`

  | 参数名称         | 参数说明        | 传参位置             | 是否必须 | 数据类型  |
  |--------------|-------------|------------------|------|-------|
  | id           | 商品ID        | UpdateProductDto | 是    | 长整型   |
  | name         | 商品名称        | UpdateProductDto | 否    | 字符串   |
  | description  | 商品描述        | UpdateProductDto | 否    | 字符串   |
  | price        | 商品价格        | UpdateProductDto | 否    | 浮点    |
  | sold         | 商品销量        | UpdateProductDto | 否    | 整数    |
  | stock        | 商品库存        | UpdateProductDto | 否    | 整数    |
  | merchantName | 商家名称        | UpdateProductDto | 否    | 字符串   |
  | categories   | 所属类别        | UpdateProductDto | 否    | 字符串列表 |
  | status       | 状态（0上架，1下架） | UpdateProductDto | 否    | 整数    |

- **请求示例**：
  ```json
  {
    "id": 123456789,
    "name": "智能手机（新款）",
    "price": 3999.99,
    "stock": 150
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "商品信息更新成功",
    "data": null
  }
  ```

---

## **购物车服务接口文档**

### **1. 添加购物车**
- **接口地址**：`/api/v1/carts`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AddItemDTO`

  | 参数名称      | 参数说明 | 传参位置       | 是否必须 | 数据类型 |
  |-----------|------|------------|------|------|
  | productId | 商品ID | AddItemDTO | 是    | 长整型  |
  | quantity  | 商品数量 | AddItemDTO | 是    | 整数   |

- **请求示例**：
  ```json
  {
    "productId": 123456789,
    "quantity": 2
  }
  ```

- **返回对象**：`ResponseResult<AddItemDTO>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "productId": 123456789,
      "quantity": 2
    }
  }
  ```

---

### **2. 清空购物车**
- **接口地址**：`/api/v1/carts`
- **请求方式**：`DELETE`
- **请求参数**：无
- **返回对象**：`ResponseResult<Void>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "购物车已清空",
    "data": null
  }
  ```

---

### **3. 获取购物车信息**
- **接口地址**：`/api/v1/carts`
- **请求方式**：`GET`
- **请求参数**：无
- **返回对象**：`ResponseResult<CartInfoVo>`，其中 `data` 字段的定义为：

  | 参数名称       | 参数说明               | 数据类型               |
  |------------|--------------------|--------------------|
  | id         | 购物车ID              | 长整型                |
  | userId     | 用户ID               | 长整型                |
  | status     | 状态（0待支付，1已支付，2已删除） | 整数                 |
  | cartItems  | 购物车商品列表            | List<CartItemInfo> |
  | createTime | 创建时间               | 本地日期时间             |
  | updateTime | 修改时间               | 本地日期时间             |

  **CartItemInfo 的定义**：

  | 参数名称       | 参数说明               | 数据类型   |
  |------------|--------------------|--------|
  | id         | 购物车项ID             | 长整型    |
  | productId  | 商品ID               | 长整型    |
  | quantity   | 商品数量               | 整数     |
  | status     | 状态（0待支付，1已支付，2已删除） | 整数     |
  | createTime | 创建时间               | 本地日期时间 |
  | updateTime | 修改时间               | 本地日期时间 |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取购物车信息成功",
    "data": {
      "id": 987654321,
      "userId": 1892225722047713281,
      "status": 0,
      "cartItems": [
        {
          "id": 123456789,
          "productId": 123456789,
          "quantity": 2,
          "status": 0,
          "createTime": "2023-10-01T12:00:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ],
      "createTime": "2023-10-01T12:00:00",
      "updateTime": "2023-10-05T14:30:00"
    }
  }
  ```

---

### **4. 获得某个购物车商品ID和数量**
- **接口地址**：`/api/v1/carts/items/{id}`
- **请求方式**：`GET`
- **请求参数**：`@PathVariable id`

  | 参数名称 | 参数说明   | 传参位置 | 是否必须 | 数据类型 |
  |------|--------|------|------|------|
  | id   | 购物车项ID | 路径参数 | 是    | 长整型  |

- **返回对象**：`ResponseResult<CartItem>`，其中 `data` 字段的定义为：

  | 参数名称       | 参数说明   | 数据类型 |
  |------------|--------|------|
  | cartItemId | 购物车项ID | 长整型  |
  | productId  | 商品ID   | 长整型  |
  | quantity   | 商品数量   | 整数   |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取购物车项成功",
    "data": {
      "cartItemId": 123456789,
      "productId": 123456789,
      "quantity": 2
    }
  }
  ```

---

## **订单服务接口文档**

### **1. 添加地址信息**
- **接口地址**：`/api/v1/addresses`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AddressDto`

  | 参数名称          | 参数说明 | 传参位置       | 是否必须 | 数据类型 |
  |---------------|------|------------|------|------|
  | streetAddress | 街道地址 | AddressDto | 是    | 字符串  |
  | city          | 城市   | AddressDto | 是    | 字符串  |
  | province      | 省份   | AddressDto | 是    | 字符串  |
  | country       | 国家   | AddressDto | 是    | 字符串  |
  | zipCode       | 邮政编码 | AddressDto | 是    | 字符串  |

- **请求示例**：
  ```json
  {
    "streetAddress": "某街道123号",
    "city": "上海",
    "province": "上海",
    "country": "中国",
    "zipCode": "200000"
  }
  ```

- **返回对象**：`ResponseResult<Void>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "地址添加成功",
    "data": null
  }
  ```

---

### **2. 获取地址信息**
- **接口地址**：`/api/v1/addresses/{id}`
- **请求方式**：`GET`
- **请求参数**：`@PathVariable id`

  | 参数名称 | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |------|------|------|------|------|
  | id   | 地址ID | 路径参数 | 是    | 长整型  |

- **返回对象**：`ResponseResult<AddressInfoVo>`，其中 `data` 字段的定义为：

  | 参数名称          | 参数说明 | 数据类型 |
  |---------------|------|------|
  | id            | 地址ID | 长整型  |
  | streetAddress | 街道地址 | 字符串  |
  | city          | 城市   | 字符串  |
  | province      | 省份   | 字符串  |
  | country       | 国家   | 字符串  |
  | zipCode       | 邮政编码 | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取地址信息成功",
    "data": {
      "id": 123456789,
      "streetAddress": "某街道123号",
      "city": "上海",
      "province": "上海",
      "country": "中国",
      "zipCode": "200000"
    }
  }
  ```

---

### **3. 删除地址信息**
- **接口地址**：`/api/v1/addresses/{id}`
- **请求方式**：`DELETE`
- **请求参数**：`@PathVariable id`

  | 参数名称 | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |------|------|------|------|------|
  | id   | 地址ID | 路径参数 | 是    | 长整型  |

- **返回对象**：`ResponseResult<Void>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "地址删除成功",
    "data": null
  }
  ```

---

### **4. 修改地址信息**
- **接口地址**：`/api/v1/addresses`
- **请求方式**：`PUT`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AddressUpdateDto`

  | 参数名称          | 参数说明 | 传参位置             | 是否必须 | 数据类型 |
  |---------------|------|------------------|------|------|
  | id            | 地址ID | AddressUpdateDto | 是    | 长整型  |
  | streetAddress | 街道地址 | AddressUpdateDto | 否    | 字符串  |
  | city          | 城市   | AddressUpdateDto | 否    | 字符串  |
  | province      | 省份   | AddressUpdateDto | 否    | 字符串  |
  | country       | 国家   | AddressUpdateDto | 否    | 字符串  |
  | zipCode       | 邮政编码 | AddressUpdateDto | 否    | 字符串  |

- **请求示例**：
  ```json
  {
    "id": 123456789,
    "streetAddress": "某街道456号",
    "city": "北京",
    "province": "北京",
    "country": "中国",
    "zipCode": "100000"
  }
  ```

- **返回对象**：`ResponseResult<Void>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "地址修改成功",
    "data": null
  }
  ```

---

### **5. 获取某个用户所有地址信息**
- **接口地址**：`/api/v1/addresses`
- **请求方式**：`GET`
- **请求参数**：`@RequestParam pageNum, @RequestParam pageSize`

  | 参数名称     | 参数说明       | 传参位置 | 是否必须 | 数据类型 |
  |----------|------------|------|------|------|
  | pageNum  | 第几页（默认1）   | 请求参数 | 否    | 整数   |
  | pageSize | 每页大小（默认10） | 请求参数 | 否    | 整数   |

- **返回对象**：`ResponseResult<IPage<AddressInfoVo>>`，其中 `data` 字段的定义为：

  | 参数名称    | 参数说明   | 数据类型                |
  |---------|--------|---------------------|
  | records | 地址信息列表 | List<AddressInfoVo> |
  | total   | 总记录数   | 长整型                 |
  | size    | 每页大小   | 整数                  |
  | current | 当前页码   | 整数                  |
  | pages   | 总页数    | 整数                  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取地址列表成功",
    "data": {
      "records": [
        {
          "id": 123456789,
          "streetAddress": "某街道123号",
          "city": "上海",
          "province": "上海",
          "country": "中国",
          "zipCode": "200000"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

### **6. 创建订单**
- **接口地址**：`/api/v1/orders`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody PlaceOrderDto`

  | 参数名称         | 参数说明    | 传参位置          | 是否必须 | 数据类型             |
  |--------------|---------|---------------|------|------------------|
  | userCurrency | 用户使用的货币 | PlaceOrderDto | 是    | 字符串              |
  | addressId    | 用户收获地址  | PlaceOrderDto | 是    | 地址信息ID           |
  | email        | 电子邮件    | PlaceOrderDto | 是    | 字符串              |
  | cartItems    | 购物车商品列表 | PlaceOrderDto | 是    | `List<CartItem>` |

  **CartItem 的定义**：

  | 参数名称       | 参数说明    | 数据类型 |
  |------------|---------|------|
  | cartItemId | 购物车商品ID | 长整数  |
  | productId  | 商品ID    | 长整型  |
  | quantity   | 商品数量    | 整数   |

- **请求示例**：
  ```json
  {
    "userCurrency": "CNY",
    "addressId": 123,
    "email": "example@example.com",
    "cartItems": [
      {
        "cartItemId": 12345678,
        "productId": 123456789,
        "quantity": 2
      }
    ]
  }
  ```

- **返回对象**：`ResponseResult<OrderResult>`，其中 `data` 字段的定义为：

  | 参数名称          | 参数说明 | 数据类型 |
  |---------------|------|------|
  | order.orderId | 订单ID | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "订单创建成功",
    "data": {
        "order": {
			"orderId": "12345678"
		}
	}
  }
  ```

---

### **7. 修改订单信息**
- **接口地址**：`/api/v1/orders`
- **请求方式**：`PUT`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody UpdateOrderDto`

  | 参数名称         | 参数说明                         | 传参位置           | 是否必须 | 数据类型           |
  |--------------|------------------------------|----------------|------|----------------|
  | orderId      | 订单ID                         | UpdateOrderDto | 是    | 字符串            |
  | status       | 支付状态（0待确认，1已确认待支付，2已支付，3已取消） | UpdateOrderDto | 是    | 整数             |
  | userCurrency | 用户使用的货币                      | UpdateOrderDto | 否    | 字符串            |
  | addressId    | 用户收获地址ID                     | UpdateOrderDto | 否    | 长整数            |
  | cartItems    | 购物车商品列表                      | UpdateOrderDto | 否    | List<CartItem> |

- **请求示例**：
  ```json
  {
    "orderId": "ORDER123456789",
    "status": 2,
    "userCurrency": "CNY"
  }
  ```

- **返回对象**：`ResponseResult<Void>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "订单信息更新成功",
    "data": null
  }
  ```

---

### **8. 订单ID查询订单信息**
- **接口地址**：`/api/v1/orders/{orderId}`
- **请求方式**：`GET`
- **请求参数**：`@PathVariable orderId`

  | 参数名称    | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |---------|------|------|------|------|
  | orderId | 订单ID | 路径参数 | 是    | 字符串  |

- **返回对象**：`ResponseResult<OrderInfoVo>`，其中 `data` 字段的定义为：

  | 参数名称         | 参数说明                               | 数据类型             |
  |--------------|------------------------------------|------------------|
  | orderId      | 订单ID                               | 字符串              |
  | status       | 支付状态（0待支付，1已确认待支付，2已支付，3支付失败，4已取消） | 整数               |
  | userCurrency | 用户使用的货币                            | 字符串              |
  | address      | 用户收获地址                             | 地址信息对象           |
  | cartItems    | 购物车商品列表                            | `List<CartItem>` |
  | createTime   | 创建时间                               | 本地日期时间           |
  | paymentTime  | 支付时间                               | 本地日期时间           |
  | updateTime   | 修改时间                               | 本地日期时间           |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取订单信息成功",
    "data": {
      "orderId": "ORDER123456789",
      "status": 2,
      "userCurrency": "CNY",
      "address": {
        "streetAddress": "某街道123号",
        "city": "上海",
        "province": "上海",
        "country": "中国",
        "zipCode": "200000"
      },
      "cartItems": [
        {
          "cartItemId": 12345678,
          "productId": 123456789,
          "quantity": 2
        }
      ],
      "createTime": "2023-10-01T12:00:00",
      "paymentTime": "2023-10-01T12:30:00",
      "updateTime": "2023-10-05T14:30:00"
    }
  }
  ```

---

### **9. 分页查询订单信息**
- **接口地址**：`/api/v1/orders`
- **请求方式**：`GET`
- **请求参数**：`@RequestParam pageSize, @RequestParam pageNum`

  | 参数名称     | 参数说明       | 传参位置 | 是否必须 | 数据类型 |
  |----------|------------|------|------|------|
  | pageSize | 每页大小（默认10） | 请求参数 | 否    | 整数   |
  | pageNum  | 第几页（默认1）   | 请求参数 | 否    | 整数   |

- **返回对象**：`ResponseResult<Page<OrderInfoVo>>`，其中 `data` 字段的定义为：

  | 参数名称    | 参数说明   | 数据类型              |
  |---------|--------|-------------------|
  | records | 订单信息列表 | List<OrderInfoVo> |
  | total   | 总记录数   | 长整型               |
  | size    | 每页大小   | 整数                |
  | current | 当前页码   | 整数                |
  | pages   | 总页数    | 整数                |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取订单列表成功",
    "data": {
      "records": [
        {
          "orderId": "ORDER123456789",
          "status": 2,
          "userCurrency": "CNY",
          "addressInfoVo": {
            "streetAddress": "某街道123号",
            "city": "上海",
            "province": "上海",
            "country": "中国",
            "zipCode": "200000"
          },
          "cartItems": [
            {
              "cartItemId": 12345678,
              "productId": 123456789,
              "quantity": 2
            }
          ],
          "createTime": "2023-10-01T12:00:00",
          "paymentTime": "2023-10-01T12:30:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

### **10. 条件分页查询订单信息**
- **接口地址**：`/api/v1/orders/search`
- **请求方式**：`GET`
- **请求参数**：`@RequestParam pageSize, @RequestParam pageNum, @RequestBody SearchOrderDto`

  | 参数名称                  | 参数说明                    | 传参位置           | 是否必须 | 数据类型        |
  |-----------------------|-------------------------|----------------|------|-------------|
  | pageSize              | 每页大小（默认10）              | 请求参数           | 否    | 整数          |
  | pageNum               | 第几页（默认1）                | 请求参数           | 否    | 整数          |
  | paymentDateUpperBound | 支付时间上界（包含）              | SearchOrderDto | 否    | 日期          |
  | paymentDateLowerBound | 支付时间下界（包含）              | SearchOrderDto | 否    | 日期          |
  | createDateUpperBound  | 创建时间上界（包含）              | SearchOrderDto | 否    | 日期          |
  | createDateLowerBound  | 创建时间下界（包含）              | SearchOrderDto | 否    | 日期          |
  | status                | 订单状态（见OrderStatus枚举类定义） | SearchOrderDto | 否    | OrderStatus |

- **请求示例**：
  ```json
  {
    "paymentDateUpperBound": "2023-10-01T23:59:59",
    "paymentDateLowerBound": "2023-10-01T00:00:00",
    "status": 2
  }
  ```

- **返回对象**：`ResponseResult<Page<OrderInfoVo>>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "条件分页查询订单信息成功",
    "data": {
      "records": [
        {
          "orderId": "ORDER123456789",
          "status": 2,
          "userCurrency": "CNY",
          "addressInfoVo": {
            "streetAddress": "某街道123号",
            "city": "上海",
            "province": "上海",
            "country": "中国",
            "zipCode": "200000"
          },
          "cartItems": [
            {
              "cartItemId": 12345678,
              "productId": 123456789,
              "quantity": 2
            }
          ],
          "createTime": "2023-10-01T12:00:00",
          "paymentTime": "2023-10-01T12:30:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

### **11. 取消订单**
- **接口地址**：`/api/v1/orders/{orderId}`
- **请求方式**：`DELETE`
- **请求参数**：`@PathVariable orderId`

  | 参数名称    | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |---------|------|------|------|------|
  | orderId | 订单ID | 路径参数 | 是    | 字符串  |

- **返回对象**：`ResponseResult<Void>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "订单取消成功",
    "data": null
  }
  ```

---

## **结算服务接口文档**

### **1. 订单结算**
- **接口地址**：`/api/v1/checkout`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
 **请求参数**：`@RequestBody CheckoutDto`

  | 参数名称          | 参数说明    | 传参位置        | 是否必须 | 数据类型          |
  |---------------|---------|-------------|------|---------------|
  | cartId        | 购物车ID   | CheckoutDto | 是    | 整数            |
  | firstName     | 用户名字    | CheckoutDto | 否    | 字符串           |
  | lastName      | 用户姓氏    | CheckoutDto | 否    | 字符串           |
  | placeOrderDto | 创建订单的信息 | CheckoutDto | 是    | PlaceOrderDto |
  | creditId      | 银行卡ID   | CheckoutDto | 是    | 字符串           |

  **PlaceOrderDto 的定义**：

  | 参数名称         | 参数说明    | 数据类型             |
  |--------------|---------|------------------|
  | userCurrency | 用户使用的货币 | 字符串              |
  | addressId    | 用户收获地址  | 长整数              |
  | email        | 邮件地址    | 字符串              |
  | cartItems    | 购物车商品列表 | `List<CartItem>` |

  **CartItem 的定义**：

  | 参数名称       | 参数说明    | 数据类型 |
  |------------|---------|------|
   | cartItemId | 购物车物品ID | 长整数  |
  | productId  | 商品ID    | 长整型  |
  | quantity   | 商品数量    | 整数   |


- **请求示例**：
  ```json
  {
    "cartId": 987654321,
    "firstName": "张",
    "lastName": "三",
    "placeOrderDto": {
      "userCurrency": "CNY",
      "addressId": "123456",
      "email": "example@example.com",
      "cartItems": [
        {
          "cartItemId": 12345678,
          "productId": 123456789,
          "quantity": 2
        }
      ]
    },
    "creditId": "123456"
  }
  ```

- **返回对象**：`ResponseResult<CheckoutVo>`，其中 `data` 字段的定义为：

  | 参数名称          | 参数说明 | 数据类型 |
  |---------------|------|------|
  | orderId       | 订单ID | 字符串  |
  | transactionId | 交易ID | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "订单结算成功",
    "data": {
      "orderId": "ORDER123456789",
      "transactionId": "TRANS123456789"
    }
  }
  ```

---

## **支付服务接口文档**

### **1. 银行卡信息管理**

#### **1.1 添加银行卡信息**
- **接口地址**：`/api/v1/credits`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody CreditDto`

  | 参数名称       | 参数说明 | 传参位置      | 是否必须 | 数据类型 |
  |------------|------|-----------|------|------|
  | cardNumber | 银行卡号 | CreditDto | 是    | 字符串  |
  | cvv        | CVV码 | CreditDto | 是    | 整数   |

- **请求示例**：
  ```json
  {
    "cardNumber": "1234567890123456",
    "cvv": 123
  }
  ```

- **返回对象**：`ResponseResult<CreditVo>`，其中 `data` 字段的定义为：

  | 参数名称       | 参数说明 | 数据类型 |
  |------------|------|------|
  | cardNumber | 银行卡号 | 字符串  |
  | cardCvv    | CVV码 | 字符串  |
    | userId     | 用户ID | 长整数  |
   | balance    | 余额   | 浮点数  |
   | expireDate | 过期时间 | 日期   |
   | status     | 状态   | 整数   |
    | createTime | 创建时间 | 时间   |
    | updateTime | 更新时间 | 时间   |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "银行卡添加成功",
    "data": {
      "cardNumber": "1234556",
      "cardCvv": 0,
       "userId": 123456,
      "balance": 1000.00,
      "expireDate": "2026-03-05",
      "status": "0",
      "createTime": "2025-03-05T00:00:00",
      "updateTime": "2025-03-05T00:00:00"
    }
  }
  ```

---

#### **1.2 删除银行卡信息**
- **接口地址**：`/api/v1/credits`
- **请求方式**：`DELETE`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody CreditGetDto`

  | 参数名称       | 参数说明 | 传参位置         | 是否必须 | 数据类型 |
  |------------|------|--------------|------|------|
  | cardNumber | 银行卡号 | CreditGetDto | 是    | 字符串  |

- **请求示例**：
  ```json
  {
    "cardNumber": "1234567890123456"
  }
  ```

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "银行卡删除成功",
    "data": null
  }
  ```

---

#### **1.3 获取银行卡信息**
- **接口地址**：`/api/v1/credits/{cardNumber}`
- **请求方式**：`GET`
- **请求参数**：`@PathVariable cardNumber`

  | 参数名称       | 参数说明 | 传参位置 | 是否必须 | 数据类型 |
  |------------|------|------|------|------|
  | cardNumber | 银行卡号 | 路径参数 | 是    | 字符串  |

- **返回对象**：`ResponseResult<CreditVo>`，其中 `data` 字段的定义为：

  | 参数名称       | 参数说明 | 数据类型 |
    |------------|------|------|
  | cardNumber | 银行卡号 | 字符串  |
  | cardCvv    | CVV码 | 字符串  |
  | userId     | 用户ID | 长整数  |
  | balance    | 余额   | 浮点数  |
  | expireDate | 过期时间 | 日期   |
  | status     | 状态   | 整数   |
  | createTime | 创建时间 | 时间   |
  | updateTime | 更新时间 | 时间   |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取银行卡信息成功",
    "data": {
      "cardNumber": "1234556",
      "cardCvv": 0,
       "userId": 123456,
      "balance": 1000.00,
      "expireDate": "2026-03-05",
      "status": "0",
      "createTime": "2025-03-05T00:00:00",
      "updateTime": "2025-03-05T00:00:00"
    }
  }
  ```

---

#### **1.4 获取某个用户所有银行卡信息**
- **接口地址**：`/api/v1/credits`
- **请求方式**：`GET`
- **请求参数**：`@RequestParam pageNum, @RequestParam pageSize`

  | 参数名称     | 参数说明       | 传参位置 | 是否必须 | 数据类型 |
  |----------|------------|------|------|------|
  | pageNum  | 第几页（默认1）   | 请求参数 | 否    | 整数   |
  | pageSize | 每页大小（默认10） | 请求参数 | 否    | 整数   |

- **返回对象**：`ResponseResult<IPage<CreditVo>>`，其中 `data` 字段的定义为：

  | 参数名称    | 参数说明    | 数据类型             |
  |---------|---------|------------------|
  | records | 银行卡信息列表 | `List<CreditVo>` |
  | total   | 总记录数    | 长整型              |
  | size    | 每页大小    | 整数               |
  | current | 当前页码    | 整数               |
  | pages   | 总页数     | 整数               |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取银行卡列表成功",
    "data": {
      "records": [
         {
            "cardNumber": "1234556",
            "cardCvv": 0,
            "userId": 123456,
            "balance": 1000.00,
            "expireDate": "2026-03-05",
            "status": "0",
            "createTime": "2025-03-05T00:00:00",
            "updateTime": "2025-03-05T00:00:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

### **2. 支付交易管理**

#### **2.1 获取支付信息**
- **接口地址**：`/api/v1/payments/byId`
- **请求方式**：`GET`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody TransactionInfoDto`

  | 参数名称             | 参数说明  | 传参位置               | 是否必须 | 数据类型 |
  |------------------|-------|--------------------|------|------|
  | transactionId    | 交易ID  | TransactionInfoDto | 否    | 字符串  |
  | preTransactionId | 预交易ID | TransactionInfoDto | 否    | 字符串  |
  （交易ID或者预支付ID必须存在一个）

- **请求示例**：
  ```json
  {
    "transactionId": "TRANS123456789",
    "preTransactionId": "PRE123456789"
  }
  ```

- **返回对象**：`ResponseResult<TransactionInfoVo>`，其中 `data` 字段的定义为：

  | 参数名称             | 参数说明                      | 数据类型   |
  |------------------|---------------------------|--------|
  | transactionId    | 交易ID                      | 字符串    |
  | preTransactionId | 预交易ID                     | 字符串    |
  | orderId          | 订单ID                      | 字符串    |
  | creditId         | 银行卡ID                     | 字符串    |
  | amount           | 金额                        | 浮点     |
  | status           | 状态（0待确认，1支付成功，2支付失败，3已取消） | 整数     |
  | reason           | 失败原因                      | 字符串    |
  | createTime       | 创建时间                      | 本地日期时间 |
  | updateTime       | 修改时间                      | 本地日期时间 |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取支付信息成功",
    "data": {
      "transactionId": "TRANS123456789",
      "preTransactionId": "PRE123456789",
      "orderId": "ORDER123456789",
      "creditId": "CREDIT123456789",
      "amount": 2999.99,
      "status": 1,
      "reason": null,
      "createTime": "2023-10-01T12:00:00",
      "updateTime": "2023-10-05T14:30:00"
    }
  }
  ```

---

#### **2.2 获取用户所有支付信息**
- **接口地址**：`/api/v1/payments`
- **请求方式**：`GET`
- **请求参数**：`@RequestParam pageNum, @RequestParam pageSize`

  | 参数名称     | 参数说明       | 传参位置 | 是否必须 | 数据类型 |
  |----------|------------|------|------|------|
  | pageNum  | 第几页（默认1）   | 请求参数 | 否    | 整数   |
  | pageSize | 每页大小（默认10） | 请求参数 | 否    | 整数   |

- **返回对象**：`ResponseResult<IPage<TransactionInfoVo>>`，其中 `data` 字段的定义为：

  | 参数名称    | 参数说明   | 数据类型                      |
  |---------|--------|---------------------------|
  | records | 支付信息列表 | `List<TransactionInfoVo>` |
  | total   | 总记录数   | 长整型                       |
  | size    | 每页大小   | 整数                        |
  | current | 当前页码   | 整数                        |
  | pages   | 总页数    | 整数                        |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "获取支付信息列表成功",
    "data": {
      "records": [
        {
          "transactionId": "TRANS123456789",
          "preTransactionId": "PRE123456789",
          "orderId": "ORDER123456789",
          "creditId": "CREDIT123456789",
          "amount": 2999.99,
          "status": 1,
          "reason": null,
          "createTime": "2023-10-01T12:00:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "pages": 1
    }
  }
  ```

---

#### **2.3 支付接口**
- **接口地址**：`/api/v1/payments`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody ChargeDto`

  | 参数名称     | 参数说明  | 传参位置      | 是否必须 | 数据类型 |
  |----------|-------|-----------|------|------|
  | orderId  | 订单ID  | ChargeDto | 是    | 字符串  |
  | creditId | 银行卡ID | ChargeDto | 是    | 字符串  |

- **请求示例**：
  ```json
  {
    "orderId": "ORDER123456789",
    "creditId": "CREDIT123456789"
  }
  ```

  - **返回对象**：`ResponseResult<ChargeVo>`，其中 `data` 字段的定义为：

    | 参数名称             | 参数说明  | 数据类型 |
    |------------------|-------|------|
    | preTransactionId | 预交易ID | 字符串  |
    | transactionId    | 交易ID  | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "支付成功",
    "data": {
      "preTransactionId": "PRE123456789",
      "transactionId": "TRAN123456789"
    }
  }
  ```

---

#### **2.4 取消支付**
- **接口地址**：`/api/v1/payments`
- **请求方式**：`DELETE`
- **请求参数**：`@RequestParam preTransactionId`

  | 参数名称             | 参数说明  | 传参位置 | 是否必须 | 数据类型 |
  |------------------|-------|------|------|------|
  | preTransactionId | 预交易ID | 请求参数 | 是    | 字符串  |

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "支付取消成功",
    "data": null
  }
  ```

---

#### **2.5 确认支付**
- **接口地址**：`/api/v1/payments/confirm`
- **请求方式**：`POST`
- **请求参数**：`@RequestParam preTransactionId`

  | 参数名称             | 参数说明  | 传参位置 | 是否必须 | 数据类型 |
  |------------------|-------|------|------|------|
  | preTransactionId | 预交易ID | 请求参数 | 是    | 字符串  |

- **返回对象**：`ResponseResult<Object>`

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "支付确认成功",
    "data": null
  }
  ```

---

## **AI服务接口文档**

### **1. 智能订单查询**
- **接口地址**：`/api/v1/ai/orders/query`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AiOrderQueryDto`

  | 参数名称           | 参数说明          | 传参位置            | 是否必须 | 数据类型 |
  |----------------|---------------|-----------------|------|------|
  | queryContent   | 查询内容（自然语言文本）  | AiOrderQueryDto | 是    | 字符串  |
  | maxResultCount | 最大返回结果数（默认10） | AiOrderQueryDto | 否    | 整数   |

- **请求示例**：
  ```json
  {
    "queryContent": "查询最近的订单",
    "maxResultCount": 5
  }
  ```

- **返回对象**：`ResponseResult<List<OrderInfoVo>>`，其中 `data` 字段的定义为：

  | 参数名称      | 参数说明      | 数据类型              |
  |-----------|-----------|-------------------|
  | orderList | 结构化订单数据列表 | List<OrderInfoVo> |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "查询成功",
    "data": {
      "answer": "以下是您最近的订单：",
      "orderList": [
        {
          "orderId": "ORDER123456789",
          "status": 2,
          "userCurrency": "CNY",
          "addressInfoVo": {
            "streetAddress": "某街道123号",
            "city": "上海",
            "province": "上海",
            "country": "中国",
            "zipCode": "200000"
          },
          "cartItems": [
            {
              "cartItemId": 123456789,
              "productId": 123456789,
              "quantity": 2
            }
          ],
          "createTime": "2023-10-01T12:00:00",
          "paymentTime": "2023-10-01T12:30:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ]
    }
  }
  ```

---

### **2. 模拟自动下单**
- **接口地址**：`/api/v1/ai/orders/auto-place`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AiPlaceOrderDto`

  | 参数名称           | 参数说明          | 传参位置            | 是否必须 | 数据类型 |
  |----------------|---------------|-----------------|------|------|
  | queryContent   | 查询内容（自然语言文本）  | AiPlaceOrderDto | 是    | 字符串  |
  | maxResultCount | 最大返回结果数（默认10） | AiPlaceOrderDto | 否    | 整数   |
    | needConfirm    | 是否需要用户手动确认    | AiPlaceOrderDto | 是    | 布尔值  |

- **请求示例**：
  ```json
  {
    "queryContent": "查询最近的订单",
    "maxResultCount": 5,
    "needConfirm": false
  }
  ```

- **返回对象**：`ResponseResult<List<OrderInfoVo>>`，其中 `data` 字段的定义为：

  | 参数名称              | 参数说明                           | 数据类型 |
  |-------------------|--------------------------------|------|
  | List<OrderInfoVo> | 需要确认时返回符合要求的订单，不需要确认时返回下单的订单列表 | 

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "自动下单成功",
    "data": {
      "orderList": [
        {
          "orderId": "ORDER123456789",
          "status": 2,
          "userCurrency": "CNY",
          "addressInfoVo": {
            "streetAddress": "某街道123号",
            "city": "上海",
            "province": "上海",
            "country": "中国",
            "zipCode": "200000"
          },
          "cartItems": [
            {
              "cartItemId": 123456789,
              "productId": 123456789,
              "quantity": 2
            }
          ],
          "createTime": "2023-10-01T12:00:00",
          "paymentTime": "2023-10-01T12:30:00",
          "updateTime": "2023-10-05T14:30:00"
        }
      ]
    }
  }
---

### **3. 智能聊天助手**
- **接口地址**：`/api/v1/ai/chat/assistant`
- **请求方式**：`POST`
- **请求数据类型**：`application/json`
- **请求参数**：`@RequestBody AiChatAssistantDto`

  | 参数名称        | 参数说明         | 传参位置               | 是否必须 | 数据类型 |
  |-------------|--------------|--------------------|------|------|
  | chatContent | 聊天内容（自然语言文本） | AiChatAssistantDto | 是    | 字符串  |

- **请求示例**：
  ```json
  {
    "chatContent": "帮我查询最近的订单"
  }
  ```

- **返回对象**：`ResponseResult<String>`，其中 `data` 字段的定义为：

  | 参数名称   | 参数说明            | 数据类型 |
  |--------|-----------------|------|
  | answer | 自然语言形式的回答（AI生成） | 字符串  |

- **响应示例**：
  ```json
  {
    "code": 200,
    "msg": "聊天成功",
    "data": {
      "answer": "以下是您最近的订单：..."
    }
  }
  ```

---
