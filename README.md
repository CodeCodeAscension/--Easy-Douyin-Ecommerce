# 字节跳动青训营抖音商城项目

“简易版”抖音商城

本项目涉及到的技术框架为：SpringBoot、SpringAI、SpringCloudGateway、SpringCloud、SpringCloudAlibaba、MySQL、MyBatisPlus、Redis、Nacos、OpenFeign、Seata、RabbitMQ、Elasticsearch、Sentinel

可以参考[docker-compose](project-description/docker-compose.yaml)文件的配置来快速搭建第三方技术框架。

本项目的接口文档定义可以参考[接口文档.md](project-description/interface-documentation.md)

## 项目结构

```
easy-douyin-ecommerce-project
├── ecommerce-ai-service        # AI微服务
├── ecommerce-auth-util         # 权限认证工具
├── ecommerce-cart-service      # 购物车微服务
├── ecommerce-checkout-service  # 结算微服务
├── ecommerce-common            # 微服务共用模块（如异常拦截、公共配置等）
├── ecommerce-feign-api         # 微服务远程调用接口定义
├── ecommerce-gateway           # 网关
├── ecommerce-order-service     # 订单微服务
├── ecommerce-payment-service   # 支付微服务
├── ecommerce-product-service   # 商品微服务
├── ecommerce-user-service      # 用户微服务
├── pom.xml                     # 整体项目Maven配置
├── project-description         # 项目说明文件夹
└── README.md
```

本项目的RabbitMQ支付消息队列模型为：

![img.png](project-description/images/img.png)

定时取消订单和定时取消支付都是使用的RabbitMQ延迟队列机制，即为：`交换机->队列->死信交换机->死信队列`的结构

本项目的elasticsearch索引结构的定义为：

`PUT /products`
```json
{
  "mappings": {
    "properties": {
      "id": { "type": "keyword" },
      "name": { "type": "text", "analyzer": "ik_max_word" },
      "description": { "type": "text", "analyzer": "ik_max_word" },
      "price": { "type": "float" },
      "sold": { "type": "integer" },
      "stock": { "type": "integer" },
      "merchantName": { "type": "keyword" },
      "categories": { "type": "keyword" },
      "status": { "type": "integer" },
      "createTime": { 
        "type": "date",
        "format": "yyyy-MM-dd'T'HH:mm:ss"
      },
      "updateTime": { 
        "type": "date",
        "format": "yyyy-MM-dd'T'HH:mm:ss" 
      }
    }
  }
}
```

## 项目启动方式

可以通过修改父项目的`pom.xml`文件的配置来定义所有第三方框架的相关信息，配置的格式示例：

```xml
<properties>
    <env>dev1</env>
    <database.host>vlsmb-kotori.local</database.host>
    <database.port>3306</database.port>
    <database.user>root</database.user>
    <database.pwd>root</database.pwd>
    <sentinel.dashboard>vlsmb-kotori.local:8090</sentinel.dashboard>
    <nacos.dashboard>vlsmb-kotori.local:8848</nacos.dashboard>
    <redis.cluster-addr>vlsmb-kotori.local:7001,vlsmb-kotori.local:7002,vlsmb-kotori.local:7003,vlsmb-kotori.local:7004,vlsmb-kotori.local:7005,vlsmb-kotori.local:7006</redis.cluster-addr>
    <rabbitmq.host>vlsmb-kotori.local</rabbitmq.host>
    <rabbitmq.port>5672</rabbitmq.port>
    <rabbitmq.user>guest</rabbitmq.user>
    <rabbitmq.pwd>guest</rabbitmq.pwd>
    <elasticsearch.host>vlsmb-kotori.local</elasticsearch.host>
    <elasticsearch.port>9200</elasticsearch.port>
</properties>
```

所有的微服务都会继承父项目中的框架配置信息，不需要额外改动微服务的配置文件。

项目所使用的第三方框架可以使用[docker-compose](project-description/docker-compose.yaml)搭建。`docker-compose`文件里有一个环境变量`HOST_IP`，在运行`docker compose`前需要对这个环境变量进行赋值，或者直接换成当前主机IP：`export $HOST_IP=your_ip`。

sentinel构建的是它的发行版jar包，对应的DockerFile为：

```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /app
COPY sentinel-dashboard-1.8.6.jar sentinel-dashboard.jar
EXPOSE 8090
ENTRYPOINT ["java", "-Dserver.port=8090", "-Dcsp.sentinel.dashboard.server=localhost:8090", "-Dproject.name=sentinel-dashboard", "-jar", "sentinel-dashboard.jar"]
```

准备好以上信息就可以进行打包了：

```bash
docker compose up -d
```

可能存在redis集群未正常搭建的情况，这时候先运行

```bash
docker exec -it redis-node-1 redis-cli -p 6379 cluster nodes
```

查看集群信息，如果只有一个节点，或者六个节点里有fail、not connected情况，需要手动重新创建集群

```bash
docker exec -it redis-node-1 redis-cli --cluster create \
  ${HOST_IP}:7001 \
  ${HOST_IP}:7002 \
  ${HOST_IP}:7003 \
  ${HOST_IP}:7004 \
  ${HOST_IP}:7005 \
  ${HOST_IP}:7006 \
  --cluster-replicas 1 --cluster-yes
```

启动之后需要将本项目使用的[nacos配置信息](project-description/nacos/configs)添加到nacos的`DEFAULT_GROUP`中，以及将[数据库表](project-description/sqls)导入到容器内的mysql中。

## 项目完成的功能

认证中心
- 分发身份令牌
- 续期身份令牌
- 校验身份令牌

用户服务
- 创建用户
- 登录
- 用户登出
- 删除用户
- 更新用户
- 获取用户身份信息

商品服务
- 创建商品
- 修改商品信息
- 删除商品
- 查询商品信息（单个商品、批量商品）

购物车服务
- 创建购物车
- 清空购物车
- 获取购物车信息

订单服务
- 创建订单
- 修改订单信息
- 订单定时取消

结算
- 订单结算

支付
- 取消支付
- 定时取消支付
- 支付

AI大模型
- 订单查询
- 模拟自动下单