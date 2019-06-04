# 一个异常通知的spring-boot-start框架 prometheus-spring-boot-starter

#### 更新预告：

0.3版本已基本完成，主要针对团队合作进行的改进，后续0.2版本的升级与0.3版本的升级分开，0.2版本主要针对个人用户，0.3版本主要针对团队开发


#### 前言

首先，本人很懒（orz），虽然说日常写完项目代码后是都需要进行相关代码段的测试的，但事与愿违，有很多情况你可能会忘了测试，或者测试了感觉没问题实际上问题没暴露（主观测试很容易让测试结果按照你的想法输出），然后把代码merge，然后提交到测试服务器，然后就去睡大觉了，第二天醒来以后，发现程序运行各种bug，这时候你就开始把前一天的代码重新拉出来开始看……等等，我先收集一下bug吧，然后打开服务器，打开输出日志……天哪，由于是测试服务器，你可能会开很多的日志（比如：sql日志，格式化了还带参数；接口调用日志等等等等），有可能某个同事由于闲的蛋疼，特意对于某个出错的功能试了十几遍……面对成千上万行的日志，针对性的找出相应的异常实在是一件令人头疼的事。所以就需要每当工程出异常了，直接通知我不就好了嘛？

#### 系统需求

![jdk版本](https://img.shields.io/badge/java-1.8%2B-red.svg?style=for-the-badge&logo=appveyor)


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

#### 如何做


**整体流程如下图**

![架构](/src/main/resources/jiage.jpg)

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
