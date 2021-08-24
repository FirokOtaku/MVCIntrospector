# MVC Introspector

基于 Annotation Processing Tool 技术, 工作于编译期,  
为 SpringBoot 生成一整套 MVC CRUD 结构.

## 使用方式

* 在项目中引入 MVC Introspector 依赖
* 在 `META-INF/services/` 目录下建立文件 `javax.annotation.processing.Processor`  
  内容为
  ```text
  firok.spring.mvci.MVCIntrospectProcessor
  ```
* 启用开发环境中的 ADT 功能
* 为数据库表创建对应的 JavaBean 实体类, 并标注 `@BeanIntrospective` 注解
* 重新编译并启动项目

### 自定义生成模板

修改 `@MVCIntrospective` 注解中的 `xxxTemplate` 值即可调整相关结构的生成模板

> 将值设为 `firok.spring.mvci.Constants.DISABLE` (`"##DISABLE##"`) 会禁用相关结构生成

> 如果不想重复写多次冗长的生成模板, 可以将模板字符串定义为静态常量, 再在注解中使用

默认的模板定义在 `firok.spring.mvci.internal.DefaultXXXTemplate` 下.

创建自定义模板时, 如下键会在结构生成时被替换:  

键|含义|示例替换值
-|-|-
`##BEAN_NAME_FULL##` | 实体完整名 | `TestBean`, `TestEntity`, `BeanTest`, `EntityTest`
`##BEAN_NAME_SHORT##` | 实体简称 | `Test`
`##MAPPER_NAME##` | Mapper 名称 | `TestMapper`
`##SERVICE_NAME##` | Service 名称 | `TestService`
`##SERVICE_IMPL_NAME##` | Service Impl 名称 | `TestServiceImpl`
`##CONTROLLER_NAME##` | Controller 名称 | `TestController`
`##BEAN_PACKAGE##` | 实体位置 | `firok.spring.demo.bean`, `firok.spring.demo.entity`
`##MAPPER_PACKAGE##` | Mapper 位置 | `firok.spring.demo.mapper`
`##SERVICE_PACKAGE##` | Service 位置 | `firok.spring.demo.service`
`##SERVICE_IMPL_PACKAGE##` | Service Impl 位置 | `firok.spring.demo.service.impl`
`##CONTROLLER_PACKAGE##` | Controller 位置 | `firok.spring.demo.controller`

### 自定义生成位置和名称

修改 `@MVCIntrospective` 注解中的 `xxxPackage` 值即可调整相关结构的生成位置; 修改 `xxxName` 即可调整相关结构名称.

## 注意

**MVCI 本身** 不基于 SpringBoot 和 MybatisPlus, 但是 **MVCI 默认的结构代码模板** 基于 SpringBoot 和 MybatisPlus.  
所以默认情况下需为编译环境引入相关依赖, 否则项目无法通过编译.

一个可用的依赖配置为:  

```xml
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.4.3.2</version>
</dependency>
```

此外, 您需要正确提供数据库驱动等依赖, 否则项目可能无法正常运行.

MVCI 仅于 **Java16 环境** 下经过测试. 更低 Java 版本中仍可能使用, 但是您需要手动调整 `firok.spring.mvci.MVCIntrospectProcessor` 上的 `@SupportedSourceVersion` 注解值.
