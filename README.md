# 一个异常通知的spring-boot-start框架 prometheus-spring-boot-starter


## 更新预告：

0.3版本已基本完成，主要针对团队合作进行的改进，后续0.2版本的升级与0.3版本的升级分开，0.2版本主要针对个人用户，0.3版本主要针对团队开发

## 前言

对于工程的开发，必然会伴随着各种bug，工程量越大，出现bug的频率也会越高。一般对于代码量较小的工程来说，一个人可能就足够去做开发与维护；但是对于代码量较大的工程往往是需要一个小团队协作开发。当工程基本完成，开始部署测试环境或者生产环境时，这些环境并不能像开发环境一样能快速的调试与维护，线上的工程一旦出现异常时，开发团队就需要主动感知异常并协调处理，当然人不能一天24小时去盯着线上工程，所以就需要一种机制来自动化的对异常进行通知，并精确到谁负责的那块代码。这样会极大地方便后续的运维。因此，本项目的团队版上线

## 系统需求

![jdk版本](https://img.shields.io/badge/java-1.8%2B-red.svg?style=for-the-badge&logo=appveyor)
![maven版本](https://img.shields.io/badge/maven-3.2.5%2B-red.svg?style=for-the-badge&logo=appveyor)
![spring boot](https://img.shields.io/badge/spring%20boot-2.0.0.RELEASE%2B-red.svg?style=for-the-badge&logo=appveyor)

## 当前版本

![目前工程版本](https://img.shields.io/badge/version-0.3.3--team-green.svg?style=for-the-badge&logo=appveyor)


## 最快上手

1. 将此工程通过``mvn clean install``打包到本地仓库中。
2. 在你的工程中的``pom.xml``中做如下依赖
```
		<dependency>
			<groupId>com.kuding</groupId>
			<artifactId>prometheus-spring-boot-starter</artifactId>
			<version>0.3.3-team</version>
		</dependency>
```
3. 在``application.properties``或者``application.yml``中做如下的配置：（至于以上的配置说明后面的章节会讲到）
```
exceptionnotice:
  dingding:
    user1: 
      phone-num: user1的手机号
      web-hook: user1设置的钉钉机器人的web-hook
    user2:
      phone-num: user2的手机号
      web-hook: user2设置的钉钉机器人的web-hook
  included-trace-package: 异常最终信息包含的包路径
  listen-type: common
  open-notice: true
  default-notice: user1
  exclude-exceptions:
  - java.lang.IllegalArgumentException

```
4. 至于钉钉的配置请移步：[钉钉机器人](https://open-doc.dingtalk.com/microapp/serverapi2/krgddi "自定义机器人")
5. 以上配置好以后就可以写demo测试啦，首先创建第一个bean：
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
当然还需要另外一个比较的bean：
```
@Component
public class AnotherComponent {

	@ExceptionListener("user2") //注意注解位置与参数
	public void giveMeError() {
		throw new NullPointerException("又是一个有故事的异常");
	}
}
```
6. 以上都建立好了以后，就可以写单元测试了，首先上第一个测试：
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private AnotherComponent anotherComponent;

	@Test
	public void contextLoads() {
		anotherComponent.giveMeError();
	}
}
```
当运行单元测试后，假如钉钉配置没有问题的话，你的钉钉中就会出现如下类似的消息：
![效果](/src/main/resources/QQ图片20190606135538.png)
当然，还需要测试另一个bean的效果：
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private NoticeComponents noticeComponents;

	@Test
	public void contextLoads() {
		noticeComponents.someMethod("赵四");
	}

}
```
然后你的钉钉的另一个机器人又会出现如下消息：
![效果](/src/main/resources/QQ图片20190606140534.png)
最后一个测试当然你可以做测试``noticeComponents.anotherMethod("赵四" , 55);``，但是可以明确的告诉你没有任何结果通知这是因为配置了
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

综上，一个简单的例子就完成了。

## 咋做的

本框架遵循spring boot starter的自动化配置规范而开发的自动化异常通知框架，在原有的单人版基础上进行了多处改进并升级成团队版，整体业务流程如下：
![流程](/src/main/resources/liucheng2.png)


### 配置

本框架配置主要分为4部分：
1. 全局配置
2. 背锅用户配置
3. 策略配置
4. 外援配置

#### 全局配置

- 全局配置主要包含在``ExceptionNoticeProperty``类下，最根本的配置便是是否开启异常通知配置``exceptionnotice.open-notice``，在开发环境，由于测试与调试都是实时反馈，有bug就即时的改掉了，并不需要进行异常通知的配置，但是在线上测试或者是生产环境还是需要进行开启的
- 每次抛出的异常的时候，异常的追踪信息非常的长，``exceptionnotice.included-trace-package``就是为了解决这个问题而存在的，一般情况下，此配置项就是配置你工程的包路径就可以了，当你的工程中出现异常时，``exceptionnotice.included-trace-package``就会把包含此包路径的追踪信息给过滤出来，并且去掉代理产生的追踪信息，这样就一目了然的知道是哪里出错了。
- 每一个工程都会有工程名，毕竟我需要知道是哪个工程出错了，配置工程名的就是``exceptionnotice.project-name``，假如工程名没有配置，框架就会优先去找``spring.application.name``，假如这个也没配置，那么这个工程我也不知道叫啥了，所以其名曰：无名工程
- 框架配置里面最重要的配置是：``exceptionnotice.listen-type``表示的是此工程的监听方式，目前有两种监听方式：**普通监听（common）** ；**mvc监听（web-mvc）** 。这两种监听方式各有千秋，普通监听方式主要运用aop的方式对有注解的方法或类进行监听，可以加在任何类与方法上。但是mvc监听只能对controller层进行监听，对其它层无效，不过异常通知的信息更丰富，不仅仅包括了普通监听的所有信息（不包含参数），还包含了请求中的参数信息（param）、请求中的请求体信息（body）和请求体中的头信息（header）：![请求异常通知](/src/main/resources/QQ图片20190606151751.png)
- 配合``exceptionnotice.listen-type=web-mvc``，可以对请求头进行筛选，默认情况下会把所有的请求头返回
```
exceptionnotice.include-header-name=headerName1,headerName2
```
- ```exceptionnotice.default-notice```是用来进行默认背锅侠的配置，用于``@ExceptionListener``的缺省参数
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


#### 背锅用户配置

背锅用户的配置分为两种类型：
    1. 背锅钉钉通知用户
    2. 背锅邮件通知用户

以上两种类型都通过map类型进行配置：
```
/**
	 * 发送钉钉异常通知给谁
	 */
	Map<String, DingDingExceptionNoticeProperty> dingding;

	/**
	 * 发送邮件异常通知给谁
	 */
	Map<String, EmailExceptionNoticeProperty> email;
```
**其中map的key表示的是谁需要来背锅，也是注解``@ExceptionListener``所需要填写的参数的值**

- 背锅钉钉通知还延续了原来的钉钉配置：
``
exceptionnotice:
  dingding:
    user1: 
      phone-num: user1的手机号
      web-hook: user1设置的钉钉机器人的web-hook
``
- 背锅邮件通知同样也延续了原来的邮件配置，同样依赖``spring-boot-starter-mail``及其配置
```
spring:
  mail:
    host: smtp.xxx.com
    port: 25
    username: 开启smtp权限的邮箱用户名
    password: 密码

exceptionnotice:
  email:
    user3:
      from: 发件人
      to: 给谁发
      cc: 抄送给谁
      bcc: 秘密抄送给谁
```

