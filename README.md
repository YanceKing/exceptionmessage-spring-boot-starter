# 一个异常通知的spring-boot-start框架 prometheus-spring-boot-starter


#### 更新预告：

0.3版本已基本完成，主要针对团队合作进行的改进，后续0.2版本的升级与0.3版本的升级分开，0.2版本主要针对个人用户，0.3版本主要针对团队开发

#### 前言

对于工程的开发，必然会伴随着各种bug，工程量越大，出现bug的频率也会越高。一般对于代码量较小的工程来说，一个人可能就足够去做开发与维护；但是对于代码量较大的工程往往是需要一个小团队协作开发。当工程基本完成，开始部署测试环境或者生产环境时，这些环境并不能像开发环境一样能快速的调试与维护，线上的工程一旦出现异常时，开发团队就需要主动感知异常并协调处理，当然人不能一天24小时去盯着线上工程，所以就需要一种机制来自动化的对异常进行通知，并精确到谁负责的那块代码。这样会极大地方便后续的运维。因此，本项目的团队版上线

#### 系统需求

![jdk版本](https://img.shields.io/badge/java-1.8%2B-red.svg?style=for-the-badge&logo=appveyor)
![maven版本](https://img.shields.io/badge/maven-3.2.5%2B-red.svg?style=for-the-badge&logo=appveyor)
![spring boot](https://img.shields.io/badge/spring%20boot-2.0.0.RELEASE%2B-red.svg?style=for-the-badge&logo=appveyor)

#### 当前版本

![目前工程版本](https://img.shields.io/badge/version-0.3.3--team-green.svg?style=for-the-badge&logo=appveyor)


#### 最快上手

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

