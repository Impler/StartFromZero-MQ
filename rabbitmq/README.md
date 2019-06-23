# RabbitMQ
RabbitMQ是AMQP协议的一个开源实现，使用Erlang语言编写的消息代理和队列服务器。用来通过普通协议在完全不同的应用中间共享数据。

## AMQP核心概念
AMQP是一个提供统一消息服务的应用层标准高级消息队列协议,是应用层协议的一个开放标准,为面向消息的中间件设计。主要包含以下组件：
- Server：又称broker，接收客户端的连接，实现AMQP实体服务
- Connection：连接，应用程序与broker的网络连接
- Channel：进行消息读写的通道。客户端可建立多个channel，每个channel代表一个会话任务
- Message：消息，服务器与应用程序之间传送的数据。由properties和body组成
- Virtual Host：虚拟主机，用于进行逻辑隔离，最上层的消息路由。一个vhost里面可以有若干个不同名称的Exchange和Queue
- Exchange：交换机，接收消息，根据路由键转发消息到绑定的队列
- Binding：exchange和queue的虚拟连接，binding中包含routing key
- Routing key：一个路由规则，虚拟机可用他来确定如何路由一个特定消息
- Queue：消息队列，保存消息，并将他们转发给消费者

## RabbitMQ特性
- 性能高：使用Erlang语言编写，Erlang语言有着和原生Socket一样的延迟特性

## RabbitMQ安装
### RabbitMQ docker集群安装
- 运行rabbitmq docker镜像，创建容器
```
docker run -d --hostname rabbitmq1 --name rabbitmq1 -p 15672:15672 -p 5672:5672 -e RABBITMQ_ERLANG_COOKIE='rabbitcookie' rabbitmq:management
docker run -d --hostname rabbitmq2 --name rabbitmq2 -p 5673:5672 --link rabbitmq1:rabbitmq1 -e RABBITMQ_ERLANG_COOKIE='rabbitcookie' rabbitmq:management
docker run -d --hostname rabbitmq3 --name rabbitmq3 -p 5674:5672 --link rabbitmq1:rabbitmq1 --link rabbitmq2:rabbitmq2 -e RABBITMQ_ERLANG_COOKIE='rabbitcookie' rabbitmq:management
```
- 设置节点1
```
docker exec -it rabbitmq1 bash
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app
exit
```
- 设置节点2
```
docker exec -it rabbitmq2 bash
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster --ram rabbit@rabbitmq1
rabbitmqctl start_app
exit
```

- 设置节点3
```
docker exec -it rabbitmq3 bash
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster --ram rabbit@rabbitmq1
rabbitmqctl start_app
exit
```

## Exchange 交换机
接收消息，根据路由键转发消息至绑定的队列。
交换机属性：
- Name：交换机名称
- Type：交换机类型 direct topic fanout headers
- Durability：是否需要持久化
- Auto Delete：当最后一个绑定到Exchange上的队列删除后，自动删除改Exchange
- Internal：当前Exchange是否用于RabbitMQ内部使用，默认为false
- Arguments：扩展参数

### Dirct Exchange
所有发送到Direct Exchange的消息被转发到routing key中指定的Queue。Direct模式可以使用RabbitMQ自带的Exchange：default Exchange，所以不需要将Exchange进行任何绑定（binding）操作，消息传递时，routing key必须完全匹配才会被队列接收，否则消息会被抛弃。

### Topic Exchange
所有发送到Topic Exchange的消息被转发到所有关心routing key中指定Topic的Queue上。
Exchange将routing Key和某topic进行模糊匹配，模糊匹配通配符：
- `#` 匹配一个或多个词
- `*` 匹配不多不少一个词  
例如：`log.#`能匹配到`log.info.oa`，`log.*`只会匹配到`log.error`

### Fanout Exchange
不处理任何routing Key，只需要简单的将队列绑定到交换机上。发送到交换机的消息都会被转发到与该交换机绑定的所有队列上。转发消息最快。