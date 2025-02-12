# 简易版抖音电商

目前项目的技术栈为：SpringBoot+MySQL+MyBatisPlus+Redis+SpringCloud+Nacos+OpenFeign+Seata+RabbitMQ+Elasticsearch

## 项目现在完成的情况

根据功能要求，我定义了7个微服务，对应功能需求的后七点，认证中心的功能就交由网关处理。每个微服务已经定义了应该实现的接口，查看[接口文档](项目说明/接口文档.md)即可。同时每个微服务都设计了[对应的数据库](项目说明/数据库表说明.md)。

每个微服务项目都导入了MybatisPlus、Redis、Nacos、OpenFeign、Seata和RabbitMQ，可以根据微服务的具体需求对依赖进行删减。每个微服务都写完了向Nacos注册服务、从Nacos获取配置信息、Mybatis二级缓存使用Redis集群、SpringMVC全局控制通知类（拦截Controller异常）、OpenFeign远程服务调用的相关定义以及拦截器从请求头中获取用户ID（该请求头由网关或者FeignClient设置）。

根目录的`pom.xml`里定义了所有第三方技术的配置信息，并定义了dev1——dev5不同的profile，该处配置对全局所有项目都有效，每个人测试时可以使用自己的环境。

## docker容器的搭建

第三方依赖的搭建用的是docker-compose，可以根据`docker-compose.yaml`构建docker容器。所有容器应该在同一个`network`里。

`docker-compose.yaml`里有一个环境变量`HOST_IP`，在运行`docker compose`前需要对这个环境变量进行赋值，或者直接换成当前主机IP：

```bash
export $HOST_IP=your_ip
```

注意nacos要提供环境配置文件，文件内容为：

```
PREFER_HOST_MODE=hostname
MODE=standalone
SPRING_DATASOURCE_PLATFORM=mysql
MYSQL_SERVICE_HOST=mysql
MYSQL_SERVICE_DB_NAME=nacos
MYSQL_SERVICE_PORT=3306
MYSQL_SERVICE_USER=root
MYSQL_SERVICE_PASSWORD=root
MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
```

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

## 任务要求

每个人把本仓库fork一份，然后在自己的分支仓库里进行代码编写，写到一定程度来提交pr。每个人只能编写自己的微服务部分，如果遇到了必须修改公共模块（ecommerce-feign-api和ecommerce-common）的情况（比如这两部分模块有重大bug或者不符合业务逻辑的编码），请先在群里说明，避免因改动公共模块给别人造成困扰。

微服务项目的接口按照[接口文档](项目说明/接口文档.md)来写，每一个Controller类和POJO类都应该使用`knife4j`写接口文档，使用`@Slf4j`做必要的日志。每一个Controller的方法都应该返回`com.example.common.domain.ResponseResult`对象，且code字段值为`com.example.common.domain.ResultCode`中的定义。

如果要有修改接口文档和数据库表的定义，也请现在群里说明。

优先完成基本任务，有时间再去完成可选、高级任务。每个人应于2月26日晚之前完成所有的基本任务。（留一天测试已经是很极限了，如果能再早点完成就更好了）

希望我们的项目能够顺利完成！
