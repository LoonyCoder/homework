### tomcat顶层架构

![tomcat顶层架构](https://upload-images.jianshu.io/upload_images/16447097-fcfc58ce319d570d.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/803)

#### Connector和Container - 两个Tomcat的心脏：

Connector 用于处理连接相关的事情，并提供 Socket 与 Request 和 Response 相关的转化;
Container 用于封装和管理 Servlet ，以及具体处理 Request请求；

#### Service - 面向外界的连接体

主要结合Connector和Container，实现封装，向外提供功能
一个service只能有一个container，但可以有多个connector

#### Server

管理Service，管理整个tomcat生命周期

---

#### Connector
Connector最底层使用的是（tcp/ip）Socket来进行连接的，Request和Response是按照HTTP协议来封装的。
![connector](https://upload-images.jianshu.io/upload_images/16447097-eb95bcf20ff5af33.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1148)

- 通过不同类型的ProtocolHandler，Connector可以处理类型的连接请求，比如socket、nio等连接
- 每个ProtocolHandler都包含了三个组件：Endpoint、Processor、Adapter
- Endpoint实现TCP/IP协议，Processor实现HTTP协议的并将Endpoint接收到的Socket封装成Request，Adapter将Request适配Container进行具体的处理
- 在Endpoint中，其抽象实现AbstractEndpoint里包含Acceptor（监听请求）和AsyncTimeout（检查异步Request的超时）两个内部类和Handler（处理接收到的Socket，并调用Processor进行处理）接口。

#### Container
管理Servlet，处理Request请求
![container](https://upload-images.jianshu.io/upload_images/16447097-94ccb9b9467c3e96.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/972)

- Engine可以管理多个主机，一个Service最多只能有一个Engine；
- Host是虚拟主机，通过配置Host就可以添加多台主机；
- Context就意味着一个WEB应用程序；
- Wrapper是Tomcat对Servlet的一层封装；



项目访问路径:
localhost:8080/demo1/lagou
localhost:8080/demo2/lagou

