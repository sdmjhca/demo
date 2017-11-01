# Vertx demo and Netty demo
NIO框架的测试demo
   近一段时间正在进行一个IM相关的项目，所以针对这个项目搜集了一些相关的框架，学习了官方资料，着手写了一点demo，加深理解以及备忘。
demo中包括几个部分：

1、netty框架的demo

2、vert.x框架的demo

通过demo能你可以理解框架的大部分组件、大概怎么使用以及框架的思想。

### Vertx

1、vertx core：

Verticle EventBus CallBack EventLoop Worker Future

TCPServer and TCPClient

HTTPServer and HTTPClient

UDPServer and UDPClient

2、vertx web & vertx web client

WebClient 

Router

APNS OVER TLS SSL
[https://github.com/sdmjhca/vertx-http2-apns]

3、vertx circuit-breaker

circuit dashboard --HystrixMetricHandler

circuit-breaker execute --fall back
