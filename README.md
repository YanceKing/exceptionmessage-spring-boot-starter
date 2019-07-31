# 一个异常通知的spring-boot-start框架 prometheus-spring-boot-starter

#### 前言的前言：


此分支适合于个人开发的工程，团队版分支请移步团队版分支，个人版版本号由发布版本号加**personal**后缀组成。



#### 前言

首先，本人很懒（orz），虽然说日常写完项目代码后是都需要进行相关代码段的测试的，但事与愿违，有很多情况你可能会忘了测试，或者测试了感觉没问题实际上问题没暴露（主观测试很容易让测试结果按照你的想法输出），然后把代码merge，然后提交到测试服务器，然后就去睡大觉了，第二天醒来以后，发现程序运行各种bug，这时候你就开始把前一天的代码重新拉出来开始看……等等，我先收集一下bug吧，然后打开服务器，打开输出日志……天哪，由于是测试服务器，你可能会开很多的日志（比如：sql日志，格式化了还带参数；接口调用日志等等等等），有可能某个同事由于闲的蛋疼，特意对于某个出错的功能试了十几遍……面对成千上万行的日志，针对性的找出相应的异常实在是一件令人头疼的事。所以就需要每当工程出异常了，直接通知我不就好了嘛？

#### 系统需求

![jdk版本](https://img.shields.io/badge/java-1.8%2B-red.svg?style=for-the-badge&logo=appveyor)



#### 2019-07-31更新

1. **集成了团队版所带的所有新特性（web-mvc模式、策略等）**

2. 工程名的替代方案

3. 对说明文档进行详细的修改

#### 2019-04-25更新

1. 将spring的版本升级为最新版本（2.1.4），本工程的版本升级为0.2.1

2. 在``ExceptionNoticeProperty``配置中加入了新的注解``openNotice``(默认状态为false)，方便开启或关闭整个异常通知框架（感谢网友支持）

3. 关于工程名的问题：``exceptionnotice.project-name``建议添上，之前想过用``spring.application.name``做替代方案，之后再加


#### 2019-03-14更新

1. 对``ExceptionNoticeConfig``中aop的配置：``exceptionNoticeAop(ExceptionHandler exceptionHandler)``的一个条件中添加了``matchIfMissing = true``来保证默认情况下的aop对象的正常加载

```
        @Bean
	@ConditionalOnProperty(name = "exceptionnotice.enable-check-annotation", havingValue = "true", matchIfMissing = true)
	@ConditionalOnMissingBean(ExceptionNoticeAop.class)
	public ExceptionNoticeAop exceptionNoticeAop(ExceptionHandler exceptionHandler) {
		ExceptionNoticeAop aop = new ExceptionNoticeAop(exceptionHandler);
		return aop;
	}
```

#### 2019-01-14更新

1. 修改了一些bug（修改了aop处理注解的路径拼写问题linstener... orz），之前用注解报错的童鞋请重新拉取本工程，并重新再本地maven仓库打包……对于大家的困扰我很抱歉，最近由于很忙，也没顾上测试我就提交了……
2. 这个工程的issue已经开放，pr也是开放的，我欢迎大家多提问题，多做改善。

#### 更新（重要）

1. 修改了一些bug（dingdingAt字段的位置问题，注解拼写的问题），去除了ExexceptionNotice中的phoneNum字段（没有用）
2. 新增邮件通知（详情参照下面的文档）
3. 配置文件的结构改变（重要）：钉钉相关配置之前有两个：``exceptionnotice.phone-num``和``exceptionnotice.web-hook``，新版本改为``exceptionnotice.dingding.phone-num``和``exceptionnotice.dingding.web-hook``;同理邮件的配置前缀为``exceptionnotice.email``
4. 钉钉的配置``exceptionnotice.phone-num``现在可以添加多个手机号了（逗号分隔）
5. 自定义配置NoticeSendComponent：现在可以自定义配置发送方式，只需要继承INoticeSendComponet接口即可：

```

import org.springframework.stereotype.Component;

import com.kuding.content.ExceptionNotice;
import com.kuding.message.INoticeSendComponent;

@Component
public class MyNoticeSendComponent implements INoticeSendComponent{

	@Override
	public void send(ExceptionNotice exceptionNotice) {
		// TODO 你自己的通知处理
	}	
}

```

#### 最快上手

1. 将此工程通过``mvn clean install``打包到本地仓库中。

2. 在你的工程中的``pom.xml``中做如下依赖

```
		<dependency>
			<groupId>com.kuding</groupId>
			<artifactId>prometheus-spring-boot-starter</artifactId>
			<version>0.2.2-personal</version>
		</dependency>
```

3. 在``application.properties``或者``application.yml``中做如下的配置：（至于以上的配置说明后面的章节会讲到）

```
spring:
  application:
    name: 普罗米修斯-demo
exceptionnotice:
  dinding:
    phone-num: 钉钉注册时的手机号
    web-hook: 设置的钉钉机器人的web-hook
  included-trace-package: 异常追踪的包路径
  notice-type: dingding
  listen-type: common
  open-notice: true
  exclude-exceptions:
  - java.lang.IllegalArgumentException

```

4. 至于钉钉的配置请移步：[钉钉机器人](https://open-doc.dingtalk.com/microapp/serverapi2/krgddi "自定义机器人")

5. 以上配置好以后就可以写demo测试啦，首先创建一个bean：

```
@Component
@ExceptionListener //异常通知的监控来自这个注解
public class NoticeComponents {

	public void someMethod(String name) {
		System.out.println("这是一个参数：" + name);
		throw new NullPointerException("第一个异常");
	}

	public void anotherMethod(String name, int age) {
		System.out.println("这又是一个参数" + age);
		throw new IllegalArgumentException(name + ":" + age);
	}
}

```

6. 以上都建立好了以后，就可以写单元测试了，首先上第一个测试：

```
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private NoticeComponents noticeComponents;

	@Test
	public void contextLoads() {
		noticeComponents.someMethod("这是个参数");
	}
}
```

当运行单元测试后，假如钉钉配置没有问题的话，你的钉钉中就会出现如下类似的消息：

![效果](/src/main/resources/demo1.png)

你也可以做另一个测试``noticeComponents.anotherMethod("赵四" , 55);``，但是可以明确的告诉你没有任何结果通知这是因为配置了

```

exclude-exceptions:
  - java.lang.IllegalArgumentException
  
```

而且方法抛出的也恰恰是此异常：

```
public void anotherMethod(String name, int age) {
		System.out.println("这又是一个参数" + age);
		throw new IllegalArgumentException(name + ":" + age);
	}
```

所以自然不会有结果。

综上，一个简单的例子就完成了


## 咋做的

本框架遵循spring boot starter的自动化配置规范而开发的自动化异常通知框架，整体业务流程如下：
![架构](/src/main/resources/new.png)


### 配置

本框架配置主要分为4部分：
1. 全局配置
2. 通知配置
3. 策略配置
4. 外援配置

#### 全局配置

- 全局配置主要包含在``ExceptionNoticeProperty``类下，最根本的配置便是是否开启异常通知配置``exceptionnotice.open-notice``，在开发环境，由于测试与调试都是实时反馈，有bug就即时的改掉了，并不需要进行异常通知的配置，但是在线上测试或者是生产环境还是需要进行开启的
- 每次抛出的异常的时候，异常的追踪信息非常的长，``exceptionnotice.included-trace-package``就是为了解决这个问题而存在的，一般情况下，此配置项就是配置你工程的包路径就可以了，当你的工程中出现异常时，``exceptionnotice.included-trace-package``就会把包含此包路径的追踪信息给过滤出来，并且去掉代理产生的追踪信息，这样就一目了然的知道是哪里出错了。
- 每一个工程都会有工程名，毕竟我需要知道是哪个工程出错了，配置工程名的就是``exceptionnotice.project-name``，假如工程名没有配置，框架就会优先去找``spring.application.name``，假如这个也没配置，那么这个工程我也不知道叫啥了，所以其名曰：无名工程
- 框架配置里面 **最重要的配置是** ：``exceptionnotice.listen-type``表示的是此工程的监听方式，目前有两种监听方式：**普通监听（common）** ；**mvc监听（web-mvc）** 。这两种监听方式各有千秋，普通监听方式主要运用aop的方式对有注解的方法或类进行监听，可以加在任何类与方法上。但是mvc监听只能对controller层进行监听，对其它层无效，不过异常通知的信息更丰富，不仅仅包括了普通监听的所有信息（不包含参数），还包含了请求中的参数信息（param）、请求中的请求体信息（body）和请求体中的头信息（header）：![请求异常通知](/src/main/resources/QQ图片20190606151751.png)
- 配合``exceptionnotice.listen-type=web-mvc``，可以对请求头进行筛选，默认情况下会把所有的请求头返回

```
exceptionnotice.include-header-name=headerName1,headerName2
```

- ``exceptionnotice.default-notice``是用来进行默认背锅侠的配置，用于``@ExceptionListener``的缺省参数
- 项目中的异常一般分类两大类：第一类为未捕获异常，第二类为业务异常。业务异常一般由用户自己定义的异常，在javaweb项目中，假如用户的请求不满足返回结果的条件，一般是需要主动抛出自定义异常的，所以这类异常并不需要进行通知。排除不需要异常通知的配置如下：

```
 exceptionnotice.exclude-exceptions=java.lang.IllegalArgumentException,com.yourpackage.SomeException
```

- 为了方便进行扩展开发，本框架还支持异常信息的持久化，目前只支持进行redis的持久化，开启redis持久化需要进行如下配置

```
exceptionnotice.enable-redis-storage=true
exceptionnotice.redis-key=你自己的redis键
```

存储redis的结构为redis的HASH接口，HASH的键是异常通知信息中的算出的一个唯一id，HASH的值是对应的异常信息，唯一id的算法也很简单：

```
private String calUid() {
		String md5 = DigestUtils.md5DigestAsHex(
				String.format("%s-%s", exceptionMessage, traceInfo.size() > 0 ? traceInfo.get(0) : "").getBytes());
		return md5;
	}
```

**这里开启redis存储需要依赖spring-boot-starter-data-redis，需要用户自行配置**





本框架遵循spring-boot-starter的配置原则通过``ExceptionNoticeConfig``来进行自动化配置，当然会有相应的配置类``ExceptionNoticeProperty``。

**(0.2.1版本)**,本框架开启需要在``application.properties（yml）``中配置``exceptionnotice.open-notice=true``方能开启异常通知

**(0.2版本)**，异常通知的方式目前有*钉钉*和*邮件*两种

   a). 钉钉的配置类为``DingDingExceptionNoticeProperty``，在``application.properties``中配置的前缀为``exceptionnotice.dingding``
   
   b). 邮件的配置类为``EmailExceptionNoticeProperty``,在``application.properties``中的前缀为``exceptionnotice.email``

本架构核心类为``ExceptionHandler``,此类用于搜集被引用工程中的异常信息；搜集信息有两种方式，第一种方式是直接调用``ExceptionHandler``中的``createNotice``方法，例如在线程池中处理异常时：
 

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

另外一种是通过``@ExceptionListener``注解的方式来进行使用，例如：

```
@Service
@Transactional
@ExceptionListener
public class ManagerTopUpStrategyService extends BaseService<ManagerTopUpStrategy, ManagerTopUpStrategyDao> {

	@Override
	protected Class<ManagerTopUpStrategy> getType() {
		return ManagerTopUpStrategy.class;
	}

}
```

通过注解的方式需要通过``application.properties``(或者在``application.yml``)将``exceptionnotice.enable-check-annotation``配置为`true`

在处理异常时，``ExceptionHandler``会将异常中的``stackTrace``的追踪信息按照包路径进行过滤，需要过滤的包路径可以在``application.properties``配置``exceptionnotice.filter-trace=***``(***表示某个包路径)即可

**重要**：``exceptionnotice.filter-trace``属于必填项！

在``ExceptionHandler``整理好相关的异常数据后，就可以通过实现``INoticeSendComponent``的相关类来进行通知了；


   * 钉钉通知的实现比较简单，只需要在``application.properties``添加如下配置：
   
```

exceptionnotice.notice-type=dingding

exceptionnotice.dingding.phone-num=手机号
exceptionnotice.dingding.web-hook=https://oapi.dingtalk.com/robot/send?access_token=.........

```

其中``web-hook``表示的是钉钉机器人的的地址， 至于钉钉如何配置钉钉机器人请点此链接：[钉钉机器人](https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.21364a97PQbww2&treeId=257&articleId=105735&docType=1 "自定义机器人")



   * 邮件通知是基于``spring-boot-starter-mail``来实现的，所以需要用到spring boot的[email相关配置](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#boot-features-email "、email相关配置")，当然，你可能还需要开启邮箱的stmp服务。
   
```
spring.mail.host=smtp.163.com（各家的不一样）
spring.mail.username=登陆邮箱
spring.mail.password=密码
spring.mail.port=端口号（一般是25）

```

   * spring的email配置好后便可以配置邮件异常通知的相关配置了：
   
```

exceptionnotice.notice-type=email

exceptionnotice.email.from=发件人邮箱
exceptionnotice.email.to=收件人邮箱（复）
exceptionnotice.email.cc=抄送人邮箱（复）
exceptionnotice.email.bcc=秘密抄送人邮箱（复）

```


异常信息也可以做一层数据存储，存储的方式是Redis存储，redis需要spring-boot的[redis自动化配置(自行查找相关配置)](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#appendix)，当然，也可以不开启redis存储。

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
			<version>0.2</version>
		</dependency>
```

3. 在``application.properties``中需要配置前缀为``exceptionnotice``的相关属性

#### 说在最后

1. 写文档确实是很麻烦的一件事，后面会继续完善这个架子与文档orz

2. 使用该框架后，假如啥东西配对了，一旦有异常出现，效果应该会是这个样子：

![效果](/src/main/resources/QQ图片20181207210829.png)

3. 邮箱的话应该是这个样子

![效果2](/src/main/resources/}PMKHZO1E3WJHU`9@C@BX8E.png)
更新中.....