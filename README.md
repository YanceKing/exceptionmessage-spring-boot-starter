# 一个异常通知的spring-boot-start框架 prometheus-spring-boot-starter

#### 前言

首先，本人很懒（orz），虽然说日常写完项目代码后是都需要进行相关代码段的测试的，但事与愿违，有很多情况你可能会忘了测试，或者测试了感觉没问题实际上问题没暴露（主观测试很容易让测试结果按照你的想法输出），然后把代码merge，然后提交到测试服务器，然后就去睡大觉了，第二天醒来以后，发现程序运行各种bug，这时候你就开始把前一天的代码重新拉出来开始看……等等，我先收集一下bug吧，然后打开服务器，打开输出日志……天哪，由于是测试服务器，你可能会开很多的日志（比如：sql日志，格式化了还带参数；接口调用日志等等等等），有可能某个同事由于闲的蛋疼，特意对于某个出错的功能试了十几遍……面对成千上万行的日志，针对性的找出相应的异常实在是一件令人头疼的事。所以就需要每当工程出异常了，直接通知我不就好了嘛？

#### 如何做


**整体流程如下图**

![架构](/src/main/resources/jiage.jpg)

1. 本框架遵循spring-boot-starter的配置原则通过``ExceptionNoticeConfig``来进行自动化配置，当然会有相应的配置类``ExceptionNoticeProperty``

2. 本架构核心类为``ExceptionHandler``,此类用于搜集被引用工程中的异常信息；搜集信息有两种方式，第一种方式是直接调用``ExceptionHandler``中的``createNotice``方法，例如在线程池中处理异常时：

```
public class ThreadExceptionHandler implements AsyncUncaughtExceptionHandler {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ExceptionHandler exceptionHandler;
	@Autowired
	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		logger.error("线程错误" + method.getName(), ex);
		exceptionHandler.createNotice(ex,
				String.format("%s:%s", currentTenantIdentifierResolver.currentTenantId(), method.getName()), params);
	}

}
```

**重要**：同一天内的相同方法抛出的相同异常每天只处理一次，当一天结束后，异常将会重新做处理

另外一种是通过``@ExceptionLinstener``注解的方式来进行使用，例如：

```
@Service
@Transactional
@ExceptionLinstener
public class ManagerTopUpStrategyService extends BaseService<ManagerTopUpStrategy, ManagerTopUpStrategyDao> {

	@Override
	protected Class<ManagerTopUpStrategy> getType() {
		return ManagerTopUpStrategy.class;
	}

}
```

通过注解的方式需要通过``application.properties``(或者在``application.yml``)将``exceptionnotice.enable-check-annotation``配置为`true`

在处理异常时，``ExceptionHandler``会将异常中的``stackTrace``的追踪信息按照包路径进行过滤，需要过滤的包路径可以在``application.properties``配置``exceptionnotice.filter-trace=***``(***表示某个包路径)即可

3. 在``ExceptionHandler``整理好相关的异常数据后，就可以通过实现``INoticeSendComponent``的相关类来进行通知了，目前只实现了钉钉机器人的异常通知（webhook），后续还会添加更多的实现方式，要实现钉钉自机器人通知只需要做如下的配置：

```
exceptionnotice.phone-num=手机号
exceptionnotice.project-name=工程名
exceptionnotice.notice-type=dingding
exceptionnotice.web-hook=https://oapi.dingtalk.com/robot/send?access_token=.........

```

至于钉钉如何配置钉钉机器人请点此链接：[钉钉机器人](https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.21364a97PQbww2&treeId=257&articleId=105735&docType=1 "自定义机器人")



4. 异常信息也可以做一层数据存储，存储的方式是Redis存储，redis需要spring-boot的[redis自动化配置(自行查找相关配置)](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#appendix)，当然，也可以不开启redis存储。

异常的redis配置需要再``application.properties``中做如下配置

```
exceptionnotice.redis-key=存储的键
exceptionnotice.enable-redis-storage=是否开启redis配置
```


#### 咋安装

1. 将本工程通过maven打包（maven install）到本地工程

2. 再其他maven工程的``pom.xml``文件中做如下配置

```
		<dependency>
			<groupId>com.kuding</groupId>
			<artifactId>prometheus-spring-boot-starter</artifactId>
			<version>0.1</version>
		</dependency>
```

3. 在``application.properties``中需要配置前缀为``exceptionnotice``的相关属性

#### 说在最后

1. 写文档确实是很麻烦的一件事，后面会继续完善这个架子与文档orz

2. 使用该框架后，假如啥东西配对了，一旦有异常出现，效果应该会是这个样子：

![效果](/src/main/resources/QQ图片20181207210829.png)

ps:因为我做的是SaaS所以在异常处理上保留了租户信息的处理（``createNotice``）
