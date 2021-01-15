# 主要模块

- IOC
- DI
- AOP
- MVC
- JDBC
- http-client
***
### 配置阶段

##### web.xml

> * servlet
> * Dispatcher
> * 设定 init-param： contextConfigLocation = applicationContext.properties
> * servlet-mapping
> * 设定 url-pattern： /*
> * 配置 Annotation： @XController @XService @XAutowired @XRequestMapping @Bean @Component
***
### 初始化

##### IOC

> * 调用init(): 加载配置
> * IOC容器初始化: Map<String, Object>
> * 扫描相关类: scan-package = "com.example"
> * 创建实例保存至容器
> > Bean Scope: Singleton, 通过三级缓存解决循环依赖<br/>
> > 　　　　　　Prototype，每次从容器中获得新对象，循环依赖报：BeanCurrentlylnCreationException

##### DI

> * DI操作：扫描IOC容器中的实例，给未赋值的属性赋值（populate）

##### MVC

> 初始化HandlerMapping: 将URL与Method一一关联放入容器：Map<String, Object>

***
### 运行阶段

> * 调用 doGet() / doPost(): Web 容器调用 doGet() / doPost() 方法，获得 request/response 对象
> * 匹配 HandleMapping: 从 request 对象中获得用户输入的 url，找到其对应的 Method
> * 反射调用method.invoker()： 利用反射调用方法并返回结果
> * response.getWrite().write()： 将返回结果输出到浏览器