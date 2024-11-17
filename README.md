# 秒杀项目

- [项目视频](https://www.bilibili.com/video/BV1sf4y1L7KE?spm_id_from=333.788.videopod.episodes&vd_source=4f40c99cfbf223a3b2d12ae621a8bebf)
- 项目地址：
- 项目笔记：[秒杀项目 | houWenk's Blog (houwenke.top)](https://houwenke.top/2024/11/17/秒杀项目/)

## 介绍

秒杀，对我们来说，都不是一个陌生的东西。每年的双11,618以及时下流行的直播等等。秒杀然而，这对于我们系统而言是一个巨大的考验。那么，如何才能更好地理解秒杀系统呢？

我觉得作为一个程序员，你首先需要从高维度出发，从整体上思考问题。在我看来，**秒杀其实主要解决两个问题，一个是并发读，一个是并发写**。并发读的核心优化理念是尽量减少用户到服务端来“读”数据，或者让他们读更少的数据；并发写的处理原则也一样，它要求我们在数据库层面独立出来一个库，做特殊的处理。另外，我们还要针对秒杀系统做一些保护，针对意料之外的情况设计兜底方案，以防止最坏的情况发生。

## 软件架构

| 技术         | 版本              |
| ------------ | ----------------- |
| SpringBoot   | 2.6.13            |
| MyBatis Plus | 3.5.2             |
| MySql        | 8                 |
| Redis        | 7.2               |
| RebbitMQ     | 3.13.3-management |

## 数据库表

![Diagram 1](../../Blog/source/_posts/秒杀项目/Diagram 1.jpg)

## 基础功能

### 登录页

LoginController

![image-20241117210337181](../../Blog/source/_posts/秒杀项目/image-20241117210337181.png)

### 商品列表页

GoodsController

![image-20241117210400328](../../Blog/source/_posts/秒杀项目/image-20241117210400328.png)

### 商品详情页

SeckillOrderController

### ![image-20241117210854857](../../Blog/source/_posts/秒杀项目/image-20241117210854857.png)

### 订单详情页

OrderController

![image-20241117212646739](../../Blog/source/_posts/秒杀项目/image-20241117212646739.png)

## 优化效果

**/seckillorder/doseckill**接口  压测结果     1955.2

![image-20241117224108590](../../Blog/source/_posts/秒杀项目/image-20241117224108590.png)

**/seckillorder/doseckill1**接口  压测结果        5841.1

![image-20241117224351092](../../Blog/source/_posts/秒杀项目/image-20241117224351092.png)

可以看到提升还是比较明显的，这得益于我们使用**redis预减库存** + **内存标记** + **MQ**提高了qps